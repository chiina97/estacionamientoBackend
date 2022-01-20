package sem.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.model.Holiday;
import sem.repository.HolidayRepository;
import sem.service.IHoliday;

@Service
public class HolidayService implements IHoliday {

	@Autowired
	HolidayRepository holidayRepo;

	@Override
	@Transactional(readOnly = true)
	public Iterable<Holiday> findAll() {
		return holidayRepo.findAll();
	}

	@Override
	public Holiday save(Holiday f) {
		return holidayRepo.save(f);
	}

	@Transactional(readOnly = true)
	public Optional<Holiday> findByDate(String fecha) {
		return holidayRepo.findByDate(fecha);
	}

}
