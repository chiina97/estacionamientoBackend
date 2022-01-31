package sem.controller;

import java.util.Optional;

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
import sem.model.Parking;
import sem.model.Patent;
import sem.model.User;
import sem.serviceImpl.ParkingService;
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
	private ParkingService parkingService;

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
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
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

		patentService.save(patentRequest);

		return new ResponseEntity<Message>(new Message("patente creada!"), HttpStatus.OK);
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

		// validaciones:¿
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);

		}
		Optional<Patent>patentOriginal=patentService.findById(patentId);
        Parking startedPatent=parkingService.findByPatentStarted(patentOriginal.get().getPatent());
        
		if(startedPatent!=null){
    		return new ResponseEntity<Message>(new Message("No se puede editar la patente "+patentOriginal.get().getPatent()+" porque tiene estacionamiento iniciado"), HttpStatus.BAD_REQUEST);
		}
		// convert DTO to Entity
		Patent patentRequest = modelMapper.map(patentDTO, Patent.class);

		// busco el User y asocio la relacion
		Optional<User> idUser = userService.findById(patentRequest.getUser().getId());
		patentRequest.setUser(idUser.get());

		Patent patent = patentService.update(patentRequest, patentId);

		if (patent == null) {
			return ResponseEntity.notFound().build();
		} else {
			
			return new ResponseEntity<Message>(new Message("patente actualizada!"), HttpStatus.CREATED);
		}

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long patentId) {
		Optional<Patent>patent=patentService.findById(patentId);
		if(this.parkingService.parkingStartedWithPatent(patent.get().getPatent(),patent.get().getUser().getId())) 
    		return new ResponseEntity<Message>(new Message("No se puede eliminar una patente con estacionamiento iniciado"), HttpStatus.BAD_REQUEST);
		patentService.deleteById(patentId);
		return new ResponseEntity<Message>(new Message("Se elimino la patente"+patent.get().getPatent()+"correctamente!") , HttpStatus.OK);
		

	}

}
