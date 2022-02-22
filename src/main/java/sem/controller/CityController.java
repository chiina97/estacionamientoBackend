package sem.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sem.dto.CityDTO;
import sem.dto.Message;
import sem.model.City;
import sem.serviceImpl.CityService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityController {
	
	private Logger logger = LoggerFactory.getLogger(CityController.class);

	@Autowired
	private CityService cityService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource msg;

	// Create a new city
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CityDTO cityDTO, BindingResult result) {
		// validaciones:

		this.logger.debug("executing CityController._create()");
		try {
		if (result.hasErrors()) {
			this.logger.debug("there are errors in the validations, error:"+ result.getFieldError().getDefaultMessage());
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}

		// convert DTO to entity
		City cityRequest = modelMapper.map(cityDTO, City.class);

		cityService.save(cityRequest);

		return new ResponseEntity<Message>(
				new Message(msg.getMessage("city.create", null, LocaleContextHolder.getLocale())), HttpStatus.OK);
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	// Read all citys
	@GetMapping
	public ResponseEntity<?> getAll() {
		this.logger.debug("executing CityController._getAll()");
		try {
		return ResponseEntity.ok(cityService.findAll());
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	// Read an city
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable(value = "id") Long cityId) {
		this.logger.debug("executing CityController._getById()");
		try {
		Optional<City> city = cityService.findById(cityId);

		// convert entity to DTO
		CityDTO cityResponse = modelMapper.map(city.get(), CityDTO.class);
		if (city.isPresent()) {
			return ResponseEntity.ok(cityResponse);
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

	// Update an city
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody CityDTO cityDTO, BindingResult result,
			@PathVariable(value = "id") Long cityId) {
		this.logger.debug("executing CityController._update()");
		try {
		// validaciones:
		if (result.hasErrors()) {
			this.logger.debug("there are errors in the validations, error:"+ result.getFieldError().getDefaultMessage());
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}
		// convert DTO to Entity
		City cityRequest = modelMapper.map(cityDTO, City.class);

		City city = cityService.update(cityRequest, cityId);

		if (city == null) {
			return ResponseEntity.notFound().build();
		} else {

			return new ResponseEntity<Message>(
					new Message(msg.getMessage("city.update", null, LocaleContextHolder.getLocale())),
					HttpStatus.CREATED);
		}
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }

	}

}
