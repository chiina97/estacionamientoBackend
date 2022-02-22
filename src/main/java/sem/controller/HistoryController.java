package sem.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sem.model.History;
import sem.model.User;
import sem.serviceImpl.HistoryService;
import sem.serviceImpl.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoryController {
	
	private Logger logger = LoggerFactory.getLogger(HistoryController.class);

	@Autowired
	HistoryService historyService;
	@Autowired
	UserService userService;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody History history) {
		this.logger.debug("executing HistoryController._create()");
		try {
		historyService.save(history);

		return new ResponseEntity<History>(history, HttpStatus.CREATED);
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		// listo una estacionamiento por id
		this.logger.debug("executing HistoryController._findById()");
		try {
		Optional<User> usuario = userService.findById(id);
		long idCuenta = usuario.get().getCurrentAccount().getId();
		return new ResponseEntity<Iterable<History>>(historyService.findById(idCuenta), HttpStatus.OK);
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		this.logger.debug("executing HistoryController._getAll()");
		try {
		return ResponseEntity.ok(historyService.findAll());
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}
}
