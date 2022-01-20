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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sem.dto.CityDTO;
import sem.model.City;
import sem.serviceImpl.CityService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityController {

	@Autowired
	private CityService cityService;

	@Autowired
	private ModelMapper modelMapper;

	// Create a new city
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody CityDTO cityDTO, BindingResult result) {
		// validaciones:
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		// convert DTO to entity
		City cityRequest = modelMapper.map(cityDTO, City.class);

		City city = cityService.save(cityRequest);

		// convert entity to DTO
		CityDTO cityResponse = modelMapper.map(city, CityDTO.class);

		// si todo sale bien informe exitosamente el resultado
		response.put("mensaje", "La ciudad ha sido creada exitosamente!");
		response.put("city", cityResponse);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// Read all citys
	@GetMapping
	public ResponseEntity<Iterable<City>> getAll() {
		return ResponseEntity.ok(cityService.findAll());
	}

	// Read an city
	@GetMapping("/{id}")
	public ResponseEntity<CityDTO> getById(@PathVariable(value = "id") Long cityId) {
		Optional<City> city = cityService.findById(cityId);

		// convert entity to DTO
		CityDTO cityResponse = modelMapper.map(city.get(), CityDTO.class);
		if (city.isPresent()) {
			return ResponseEntity.ok(cityResponse);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	// Update an city
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody CityDTO cityDTO, BindingResult result,
			@PathVariable(value = "id") Long cityId) {
		// validaciones:
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}
		// convert DTO to Entity
		City cityRequest = modelMapper.map(cityDTO, City.class);

		City city = cityService.update(cityRequest, cityId);

		// entity to DTO
		CityDTO cityResponse = modelMapper.map(city, CityDTO.class);
		if (city == null) {
			return ResponseEntity.notFound().build();
		} else {
			// si todo sale bien informe exitosamente el resultado
			response.put("mensaje", "El usuario ha sido actualizado exitosamente!");
			response.put("city", cityResponse);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		}

	}

}
