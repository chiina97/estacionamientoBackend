package sem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import sem.dto.Message;
import sem.dto.PatentDTO;
import sem.model.Patent;
import sem.model.User;
import sem.serviceImpl.PatentService;
import sem.serviceImpl.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // lo comente para usar el postman
@RequestMapping(value = "/patent", produces = MediaType.APPLICATION_JSON_VALUE)

public class PatentController {
	@Autowired
	private PatentService patentService;
	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	// Read all patents
	@GetMapping("/user/{id}")
	public ResponseEntity<Iterable<Patent>> getPatentsOfUser(@PathVariable(value = "id") Long userId) {
		return ResponseEntity.ok(patentService.findAllByUser(userId));

	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PatentDTO patentDTO, BindingResult result) {
		// validaciones:
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (patentService.existsByPatentAndUser(patentDTO.getPatent(), patentDTO.getUser().getId()) != null) {
			return new ResponseEntity<Message>(
					new Message("la patente " + patentDTO.getPatent() + " ya existe en su listado de patentes"),
					HttpStatus.BAD_REQUEST);
		}

		// convert DTO to entity
		Patent patentRequest = modelMapper.map(patentDTO, Patent.class);

		// acá deberia pasarle el id del User a partir de su token:
		Optional<User> idUser = userService.findById(patentRequest.getUser().getId());
		patentRequest.setUser(idUser.get());

		Patent patent = patentService.save(patentRequest);
		// convert entity to DTO
		PatentDTO patentResponse = modelMapper.map(patent, PatentDTO.class);

		// si todo sale bien informe exitosamente el resultado
		response.put("mensaje", "La patente ha sido creado exitosamente!");
		response.put("patent", patentResponse);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// Read an patent
	@GetMapping("/{id}")
	public ResponseEntity<PatentDTO> findById(@PathVariable(value = "id") Long patentId) {
		Optional<Patent> patent = patentService.findById(patentId);

		// convert entity to DTO
		PatentDTO patentResponse = modelMapper.map(patent.get(), PatentDTO.class);
		if (patent.isPresent()) {
			return ResponseEntity.ok(patentResponse);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	// Update an patent
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody PatentDTO patentDTO, BindingResult result,
			@PathVariable(value = "id") Long patentId) {

		// validaciones:
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		// convert DTO to Entity
		Patent patentRequest = modelMapper.map(patentDTO, Patent.class);

		// busco el User y asocio la relacion
		Optional<User> idUser = userService.findById(patentRequest.getUser().getId());
		patentRequest.setUser(idUser.get());

		Patent patent = patentService.update(patentRequest, patentId);

		// entity to DTO
		PatentDTO patentResponse = modelMapper.map(patent, PatentDTO.class);
		if (patent == null) {
			return ResponseEntity.notFound().build();
		} else {
			response.put("mensaje", "La patente ha sido actualizado exitosamente!");
			response.put("patent", patentResponse);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		}

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(value = "id") Long patentId) {
		patentService.deleteById(patentId);

	}

}
