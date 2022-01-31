package sem.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sem.dto.Message;
import sem.dto.TimePriceDTO;
import sem.model.City;
import sem.model.Holiday;
import sem.model.Parking;
import sem.model.User;
import sem.serviceImpl.CityService;
import sem.serviceImpl.HolidayService;
import sem.serviceImpl.ParkingService;
import sem.serviceImpl.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/parking", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParkingController {

	@Autowired
	ParkingService parkingService;
	@Autowired
	UserService userService;
	@Autowired
	CityService cityService;
	@Autowired
	HolidayService holidayService;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Parking parking) {
		Parking startedParking = parkingService.findByPatentStarted(parking.getPatent());
		if (startedParking != null) {
			return new ResponseEntity<Message>(
					new Message("La patente " + parking.getPatent() + " ya tiene un parking iniciado"),
					HttpStatus.BAD_REQUEST);
		} else {

			Optional<City> city = cityService.findById(Long.parseLong("1"));
			Optional<User> user = userService.findById(parking.getUser().getId());
			Iterable<Holiday> holidays = holidayService.findAll();

			Message msg = Parking.validations(city.get(), holidays);
			// saldo de cuenta:
			double accountBalance = user.get().getCurrentAccount().getBalance();

			if (accountBalance >= city.get().getValueByHour()) {
				if (msg == null) {
					parkingService.save(parking);
					return new ResponseEntity<Parking>(parking, HttpStatus.CREATED);
				}

				else
					return new ResponseEntity<Message>(msg, HttpStatus.BAD_REQUEST);
			} else
				return new ResponseEntity<Message>(new Message("El saldo de la cuenta es insuficiente"),
						HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Optional<Parking>> findById(@PathVariable("id") Long id) {
		// listo una parking por id

		Optional<Parking> parking = parkingService.findById(id);
		return new ResponseEntity<Optional<Parking>>(parking, HttpStatus.OK);
	}

	@GetMapping(path = "/finishParking/{id}")
	public ResponseEntity<?> finishParking(@PathVariable("id") Long idUser) {
		// listo una parking por id

		Optional<Parking> parking = parkingService.findStartedParkingBy(idUser);
		if (parking.isEmpty()) {
			return new ResponseEntity<Parking>(HttpStatus.NOT_FOUND);

		} else {

			Optional<City> city = cityService.findById(Long.parseLong("1"));
			Iterable<Holiday> holidays = holidayService.findAll();
			Message msg = Parking.validations(city.get(), holidays);

			if (msg == null) {

				parking.get().setStartedParking(false);
				parkingService.updateAmount(parking.get(), parking.get().getId());

				return new ResponseEntity<Parking>(parking.get(), HttpStatus.OK);
			} else
				return new ResponseEntity<Message>(msg, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/existParkingOfUser/{id}")
	public boolean existParkingOfUser(@PathVariable("id") Long idUser) {
		// listado de parkings
		Optional<Parking> parking = parkingService.findStartedParkingBy(idUser);
		if (parking.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}
	 @GetMapping("/getTime/{id}")
	    public TimePriceDTO getTime(@PathVariable("id") Long id){
			//retorna en milisegundos el tiempo transcurrido.
			System.out.println("Metodo: /getTime");
			Optional<User> user = this.userService.findById(id);
			Optional<Parking> queryResult = parkingService.findStartedParkingBy(id);
			Optional<City> city = cityService.findById(Long.parseLong("1"));
			
			if(user.isEmpty()) {	
				return null;
			}else {
				TimePriceDTO data = queryResult.get().getCurrentPaymentDetails(city.get());
				return data;
			}
			
	    }
	

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody Parking parking, @PathVariable(value = "id") Long parkingId) {

		Parking p = parkingService.updateAmount(parking, parkingId);

		if (p == null) {
			return ResponseEntity.notFound().build();
		} else {

			return new ResponseEntity<Parking>(p, HttpStatus.CREATED);
		}

	}

}
