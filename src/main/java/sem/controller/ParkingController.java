package sem.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import org.springframework.context.i18n.LocaleContextHolder;

import sem.dto.Message;
import sem.dto.ParkingDTO;
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
	
	@Autowired
	private MessageSource msg;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ParkingDTO parkingDTO) {
		// convert DTO to entity
		Parking parkingRequest = modelMapper.map(parkingDTO, Parking.class);
		
		Parking startedParking = parkingService.findByPatentStarted(parkingRequest.getPatent());
		if (startedParking != null) {
			return new ResponseEntity<Message>(
					new Message(msg.getMessage("patent.parking.started", new String[]{parkingRequest.getPatent()}, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST);
		} else {

			Optional<City> city = cityService.findById(Long.parseLong("1"));
			Optional<User> user = userService.findById(parkingRequest.getUser().getId());
			Iterable<Holiday> holidays = holidayService.findAll();

			// convert entity to DTO
			ParkingDTO parkingResponse = modelMapper.map(parkingRequest, ParkingDTO.class);
			parkingResponse.setMsg(msg);

			Message result = parkingResponse.validations(city.get(), holidays);
			// saldo de cuenta:
			double accountBalance = user.get().getCurrentAccount().getBalance();

			if (accountBalance >= city.get().getValueByHour()) {
				if (result == null) {
					parkingService.save(parkingRequest);
					return new ResponseEntity<Parking>(parkingRequest, HttpStatus.CREATED);
				}

				else
					return new ResponseEntity<Message>(result, HttpStatus.BAD_REQUEST);
			} else
				return new ResponseEntity<Message>(new Message(msg.getMessage("currentAccount.balance.insufficient", null, LocaleContextHolder.getLocale())),
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
			// convert entity to DTO
			ParkingDTO parkingResponse = modelMapper.map(parking.get(), ParkingDTO.class);
			
			Optional<City> city = cityService.findById(Long.parseLong("1"));
			Iterable<Holiday> holidays = holidayService.findAll();
			Message result = parkingResponse.validations(city.get(), holidays);

			if (result == null) {

				parking.get().setStartedParking(false);
				parkingService.updateAmount(parking.get(), parking.get().getId());
				
				String monto=String.valueOf(parking.get().getAmount());
				
				return new ResponseEntity<Message>(
						new Message(msg.getMessage("parking.balace.acredit", new String[]{monto}, LocaleContextHolder.getLocale())),
						HttpStatus.OK);
			} else
				return new ResponseEntity<Message>(result, HttpStatus.BAD_REQUEST);
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
			
			Optional<User> user = this.userService.findById(id);
			Optional<Parking> queryResult = parkingService.findStartedParkingBy(user.get().getId());
			Optional<City> city = cityService.findById(Long.parseLong("1"));
			
			if(queryResult.isEmpty()) {	
				return null;
			}else {
				// convert entity to DTO
				ParkingDTO parkingResponse = modelMapper.map(queryResult.get(), ParkingDTO.class);
				
				TimePriceDTO data = parkingResponse.getCurrentPaymentDetails(city.get());
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
