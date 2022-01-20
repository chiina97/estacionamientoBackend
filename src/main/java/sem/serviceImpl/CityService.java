package sem.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sem.model.City;
import sem.repository.CityRepository;
import sem.service.ICity;

@Service
public class CityService implements ICity {
	@Autowired
	private CityRepository cityRepo; // inyectamos

	@Override
	@Transactional(readOnly = true)
	public Iterable<City> findAll() {
		return cityRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<City> findAll(Pageable pageable) {
		return cityRepo.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<City> findById(Long id) {
		return cityRepo.findById(id);
	}

	@Override
	@Transactional
	public City save(City c) {
		return cityRepo.save(c);
	}

	@Override
	public City update(City cRequest, Long id) {
		Optional<City> city = cityRepo.findById(id);
		if (city.isPresent()) {
			city.get().setStartTime(cRequest.getStartTime());
			city.get().setEndTime(cRequest.getEndTime());
			city.get().setValueByHour(cRequest.getValueByHour());
			return cityRepo.save(city.get());
		} else
			return null;
	}

}
