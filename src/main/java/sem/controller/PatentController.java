package sem.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import sem.dto.Message;
import sem.dto.PatentDTO;
import sem.model.Patent;
import sem.model.User;
import sem.serviceImpl.ParkingService;
import sem.serviceImpl.PatentService;
import sem.serviceImpl.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // lo comente para usar el postman
@RequestMapping(value = "/patent", produces = MediaType.APPLICATION_JSON_VALUE)

public class PatentController {
	
	private Logger logger = LoggerFactory.getLogger(PatentController.class);
	
	@Autowired
	private PatentService patentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ParkingService parkingService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource msg;


	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PatentDTO patentDTO, BindingResult result) {
		this.logger.debug("executing PatentController._create()");
		try {
		// validaciones:
		if (result.hasErrors()) {
			this.logger.debug("there are errors in the validations, error:"+ result.getFieldError().getDefaultMessage());
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}

		if (patentService.existsByPatentAndUser(patentDTO.getPatent(), patentDTO.getUser().getId()) != null) {
			this.logger.debug("The patent already exists in this user's list");
			return new ResponseEntity<Message>(
					new Message(msg.getMessage("patent.existPatent", new String[] { patentDTO.getPatent() },
							LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		}

		// convert DTO to entity
		Patent patentRequest = modelMapper.map(patentDTO, Patent.class);

		// acá deberia pasarle el id del User a partir de su token:
		Optional<User> idUser = userService.findById(patentRequest.getUser().getId());
		patentRequest.setUser(idUser.get());
		patentRequest.setPatent(patentRequest.getPatent().toUpperCase());

		patentService.save(patentRequest);

		return new ResponseEntity<Message>(
				new Message(msg.getMessage("patent.create", null, LocaleContextHolder.getLocale())), HttpStatus.OK);
	
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}
	// Read all patents
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getPatentsOfUser(@PathVariable(value = "id") Long userId) {
		this.logger.debug("executing PatentController._getPatentsOfUser()");
		try {
		return ResponseEntity.ok(patentService.findAllByUser(userId));
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	// Read an patent
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(value = "id") Long patentId) {
		this.logger.debug("executing PatentController._findById()");
		try {
		Optional<Patent> patent = patentService.findById(patentId);

		// convert entity to DTO
		PatentDTO patentResponse = modelMapper.map(patent.get(), PatentDTO.class);
		if (patent.isPresent()) {
			return ResponseEntity.ok(patentResponse);
		} else {
			return ResponseEntity.notFound().build();
		}

		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	// Update an patent
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody PatentDTO patentDTO, BindingResult result,
			@PathVariable(value = "id") Long patentId) {

		this.logger.debug("executing PatentController._update()");
		
		try {
		// validaciones:¿
		if (result.hasErrors()) {
			this.logger.debug("there are errors in the validations, error:"+ result.getFieldError().getDefaultMessage());
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);

		}
		if (patentService.existsByPatentAndUser(patentDTO.getPatent(), patentDTO.getUser().getId()) != null) {
			this.logger.debug("The patent already exists in this user's list");
			return new ResponseEntity<Message>(
					new Message(msg.getMessage("patent.existPatent", new String[] { patentDTO.getPatent() },
							LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		}
		Optional<Patent> patentOriginal = patentService.findById(patentId);

		Boolean startedPatent = parkingService.parkingStartedWithPatent(patentOriginal.get().getPatent(),
				patentOriginal.get().getUser().getId());
		// si existe la patente iniciada para ese usuario
		if (startedPatent) {
			this.logger.debug("Unable to edit patent because parking started.");
			return new ResponseEntity<Message>(new Message(msg.getMessage("patent.update.parking.started",
					new String[] { patentOriginal.get().getPatent() }, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		}
		// convert DTO to Entity
		Patent patentRequest = modelMapper.map(patentDTO, Patent.class);

		// busco el User y asocio la relacion
		Optional<User> idUser = userService.findById(patentRequest.getUser().getId());
		patentRequest.setUser(idUser.get());
		patentRequest.setPatent(patentRequest.getPatent().toUpperCase());

		Patent patent = patentService.update(patentRequest, patentId);

		if (patent == null) {
			this.logger.debug("there is no patent with that id"); 
			return ResponseEntity.notFound().build();
		} else {
			this.logger.debug("the patent with that id exists"); 
			return new ResponseEntity<Message>(
					new Message(msg.getMessage("patent.update", null, LocaleContextHolder.getLocale())),
					HttpStatus.CREATED);
		}
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long patentId) {
		this.logger.debug("executing PatentController._delete()");
		try {
		Optional<Patent> patent = patentService.findById(patentId);
		if (this.parkingService.parkingStartedWithPatent(patent.get().getPatent(), patent.get().getUser().getId()))
			
			return new ResponseEntity<Message>(
					new Message(msg.getMessage("patent.delete.parking.started",
							new String[] { patent.get().getPatent() }, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		patentService.deleteById(patentId);
		return new ResponseEntity<Message>(new Message(msg.getMessage("patent.delete",
				new String[] { patent.get().getPatent() }, LocaleContextHolder.getLocale())), HttpStatus.OK);

	}
	catch (Exception e) {
        e.printStackTrace();
        this.logger.error("Error found: {}", e);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      
    }
	}
}
