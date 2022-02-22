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

import sem.model.Holiday;
import sem.serviceImpl.HolidayService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/holiday", produces = MediaType.APPLICATION_JSON_VALUE)
public class HolidayController {
	
	private Logger logger = LoggerFactory.getLogger(HolidayController.class);
	
	@Autowired
	HolidayService holidayService;

	@ApiIgnore
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Holiday holiday) {

		this.logger.debug("executing HolidayController._create()");
		try {
		holidayService.save(holiday);

		return new ResponseEntity<Holiday>(holiday, HttpStatus.CREATED);
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}

	@ApiIgnore
	@GetMapping(path = "/{date}")
	public boolean isWorkingDate(@PathVariable("date") String date) {
		this.logger.debug("executing HolidayController._isWorkingDate()");
		String formattedDate = date.split("-")[0] + "/" + date.split("-")[1];
		Optional<Holiday> holiday = holidayService.findByDate(formattedDate);
		if (holiday.isEmpty()) {
			// es un dia habil
			return false;
		} else {
			// no es un dia habil
			return true;
		}
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		this.logger.debug("executing HolidayController._getAll()");
		try {
		return ResponseEntity.ok(holidayService.findAll());
	
		}
		catch (Exception e) {
	        e.printStackTrace();
	        this.logger.error("Error found: {}", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      
	    }
	}
}
