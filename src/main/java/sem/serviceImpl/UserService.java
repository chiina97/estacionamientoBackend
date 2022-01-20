package sem.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.enums.RoleName;
import sem.model.Role;
import sem.model.User;
import sem.repository.UserRepository;
import sem.service.IUser;

@Service
public class UserService implements IUser {

	@Autowired
	private UserRepository userRepo; // inyectamos

	@Autowired
	private CurrentAccountService accountService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleService roleService;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userRepo.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		return userRepo.findById(id);
	}

	@Override
	@Transactional
	public User save(User user) {
		return userRepo.save(user);
	}

	@Transactional
	public User updateAccount(User userRequest) {
		accountService.update(userRequest.getCurrentAccount(), userRequest.getCurrentAccount().getId());
		return userRepo.save(userRequest);

	}

	// REVISAR METODOS;
	@Transactional(readOnly = true)
	public Optional<User> getByPhone(String Phone) {
		return userRepo.findByPhone(Phone);
	}

	@Transactional(readOnly = true)
	public boolean existsByPhone(String Phone) {
		return userRepo.existsByPhone(Phone);
	}

	@Transactional(readOnly = true)
	public boolean existsByMail(String correo) {
		return userRepo.existsByMail(correo);
	}

	public Optional<User> findByPhone(String phone) {
		return userRepo.findByPhone(phone);

	}

	@Override
	@Transactional
	public User update(User userRequest, Long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			Set<Role> roles = new HashSet<>();
			roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
			user.get().setPhone(userRequest.getPhone());
			;
			user.get().setMail(userRequest.getMail());

			return userRepo.save(user.get());
		} else
			return null;
	}

}
