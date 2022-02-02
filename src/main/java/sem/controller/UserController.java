package sem.controller;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.i18n.LocaleContextHolder;

import sem.dto.CurrentAccountDTO;
import sem.dto.JwtDTO;
import sem.dto.LoginDTO;
import sem.dto.Message;
import sem.dto.UserDTO;
import sem.enums.RoleName;
import sem.jwt.JwtProvider;
import sem.model.CurrentAccount;
import sem.model.History;
import sem.model.Role;
import sem.model.User;
import sem.serviceImpl.CurrentAccountService;
import sem.serviceImpl.RoleService;
import sem.serviceImpl.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)

public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private CurrentAccountService acountService;
	@Autowired
	private ModelMapper modelMapper;
	// token:
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	RoleService roleService;
	@Autowired
	JwtProvider jwtProvider;
	@Autowired
	HistoryController historyController;
	@Autowired
	private MessageSource msg;

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

		// validaciones:

		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}
		if (userService.existsByPhone(userDTO.getPhone())) {
			return new ResponseEntity<Message>(new Message(msg.getMessage("user.existPhone", null, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		}
		if (userService.existsByMail(userDTO.getMail())) {
			return new ResponseEntity<Message>(new Message(msg.getMessage("user.existEmail", null, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		}
		// convert DTO to entity
		User userRequest = modelMapper.map(userDTO, User.class);

		// creo un user con la pass hasheada
		User user = new User(passwordEncoder.encode(userRequest.getPassword()), userRequest.getMail(),
				userRequest.getPhone());

		// agrego rol y guardo el user
		Set<Role> roles = new HashSet<>();
		roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
		user.setRoles(roles);
		userService.save(user);

		// obtengo el id de user
		Optional<User> userId = userService.findById(user.getId());
		// creo el object account;
		CurrentAccount account = new CurrentAccount(userId.get().getPhone(), 0);
		account.setUser(user);
		acountService.save(account);
		// convert entity to DTO
		CurrentAccountDTO accountReponse = modelMapper.map(account, CurrentAccountDTO.class);

		UserDTO userResponse = modelMapper.map(user, UserDTO.class);
		userResponse.setCurrentAccount(accountReponse);

		return new ResponseEntity<Message>(new Message(msg.getMessage("user.create", null, LocaleContextHolder.getLocale())), HttpStatus.OK);

	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginDto, BindingResult result) {
		// validaciones:
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(msg.getMessage("user.notValid", null, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getPhone(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		JwtDTO jwtDto = new JwtDTO(jwt);

		return new ResponseEntity<JwtDTO>(jwtDto, HttpStatus.OK);

	}

	// Read all users
	@GetMapping
	public ResponseEntity<Iterable<User>> getAll() {
		return ResponseEntity.ok(userService.findAll());
	}

	// Read an user
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") Long userId) {
		Optional<User> user = userService.findById(userId);

		// convert entity to DTO
		UserDTO userResponse = modelMapper.map(user.get(), UserDTO.class);
		if (user.isPresent()) {
			return ResponseEntity.ok(userResponse);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/currentAccount/{id}")
	public ResponseEntity<?> findCurrentAccountById(@PathVariable(value = "id") Long userId) {
		Optional<User> user = userService.findById(userId);

		// convert entity to DTO
		UserDTO userResponse = modelMapper.map(user.get(), UserDTO.class);
		if (user.isPresent()) {
			return ResponseEntity.ok(userResponse.getCurrentAccount());
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	// Update an User
	@PutMapping("/account/{id}")
	public ResponseEntity<?> updateAmount(@Valid @RequestBody CurrentAccountDTO accountDTO, BindingResult result,
			@PathVariable(value = "id") Long userId) {
		// validaciones:
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<User> user = userService.findByPhone(accountDTO.getPhone());

		user.get().getCurrentAccount()
				.setBalance(user.get().getCurrentAccount().getBalance() + accountDTO.getBalance());

		if (user.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			// si todo sale bien informe exitosamente el resultado
			userService.updateAccount(user.get());

			History history = new History("Carga", accountDTO.getBalance(), user.get().getCurrentAccount().getBalance(),
					user.get().getCurrentAccount());
			historyController.create(history);
			return new ResponseEntity<Message>(new Message(msg.getMessage("user.update.amount", null, LocaleContextHolder.getLocale())), HttpStatus.OK);
		}

	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtDTO> refresh(@RequestBody JwtDTO jwtDto) throws ParseException {
		String token = jwtProvider.refreshToken(jwtDto);
		JwtDTO jwt = new JwtDTO(token);
		return new ResponseEntity<JwtDTO>(jwt, HttpStatus.OK);
	}

}
