package sem.serviceImpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.dto.ParkingDTO;
import sem.dto.TimePriceDTO;
import sem.model.City;
import sem.model.History;
import sem.model.Parking;
import sem.model.User;
import sem.repository.CityRepository;
import sem.repository.ParkingRepository;
import sem.service.IParking;

@Service
public class ParkingService implements IParking {
	@Autowired
	ParkingRepository parkingRepo;
	@Autowired
	UserService userService;

	@Autowired
	HistoryService historyService;

	@Autowired
	CityRepository cityRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional(readOnly = true)
	public Iterable<Parking> findAll() {
		return parkingRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Parking> findById(Long id) {
		return parkingRepo.findById(id);
	}

	@Transactional(readOnly = true)
	public Parking findByPatentStarted(String patente) {
		return parkingRepo.findByPatentStarted(patente);
	}

	public boolean parkingStartedWithPatent(String patent, Long userId) {
		// return -> true -> hay un estacionamiento iniciado con esa patente.
		return !parkingRepo.getParkingStartedWithPatent(patent, userId).isEmpty();
	}

	@Transactional(readOnly = true)
	public Optional<Parking> findStartedParkingBy(Long id) {
		return parkingRepo.findStartedParkingBy(id);
	}

	@Override
	@Transactional
	public Parking save(Parking parking) {
		return parkingRepo.save(parking);
	}

	@Override
	@Transactional // no se usa
	public Parking updateAmount(Parking p, Long id) {
		Optional<Parking> parking = parkingRepo.findById(id);
		Optional<City> city = cityRepo.findById(Long.valueOf(1));

		if (parking.isPresent()) {
			parking.get().setPatent(p.getPatent());
			parking.get().setUser(p.getUser());
			parking.get().setStartTime(p.getStartTime());

			// convert entity to DTO
			ParkingDTO parkingResponse = modelMapper.map(parking.get(), ParkingDTO.class);
			TimePriceDTO timePrice = parkingResponse.getCurrentPaymentDetails(city.get());
			parking.get().setAmount(timePrice.getPrice());

			parking.get().setStartedParking(p.isStartedParking());

			User user = parking.get().getUser();

			user.getCurrentAccount().setBalance(user.getCurrentAccount().getBalance() - parking.get().getAmount());

			userService.updateAccount(user);

			History h = new History("Consumo", parking.get().getAmount(), user.getCurrentAccount().getBalance(),
					user.getCurrentAccount());
			historyService.save(h);
			return parkingRepo.save(parking.get());

		} else
			return null;
	}

}
