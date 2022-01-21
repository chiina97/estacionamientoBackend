package sem.serviceImpl;

import java.util.ArrayList;
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
	public  ArrayList<Holiday>  findAll() {
		return  (ArrayList<Holiday>)holidayRepo.findAll();
	}

	@Override
	@Transactional
	public Holiday save(Holiday f) {
		return holidayRepo.save(f);
	}

	@Transactional(readOnly = true)
	public Optional<Holiday> findByDate(String fecha) {
		return holidayRepo.findByDate(fecha);
	}

}
