package sem.serviceImpl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		//return -> true -> hay un estacionamiento iniciado con esa patente.
		return !parkingRepo.getParkingStartedWithPatent(patent,userId).isEmpty();
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

			@SuppressWarnings("deprecation")
			Date startDate = new Date(p.getStartTime());
			Date currentDate = new Date();

			// tiempo trasncurrido (hora actual - hora inicio):
			Long timeElapsed = currentDate.getTime() - startDate.getTime();

			// lo paso a segundos:
			double seconds = timeElapsed / 1000;
			double hour = Math.floor(seconds / 3600);

			double rest = Math.floor(((seconds % 3600) / 60) % 15);
			double minutes = Math.floor(((seconds % 3600) / 60) / 15);
			double valueByFraction = city.get().getValueByHour() / 4;

			if ((rest == 0) && (minutes != 0)) {

				// si pasaron extactamente de a 15 minutos
				parking.get().setAmount((minutes * valueByFraction) + (hour * city.get().getValueByHour()));

			} else {

				// si pasa de a 15 minutos y pico ,te cobra los minutos que pasaron como si
				// fueran 15
				double account = (minutes * valueByFraction) + (hour * city.get().getValueByHour()) + valueByFraction;
				parking.get().setAmount(account);

			}

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
