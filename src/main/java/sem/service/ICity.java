package sem.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sem.model.City;

public interface ICity {
	public Iterable<City> findAll();

	public Page<City> findAll(Pageable pageable);

	public Optional<City> findById(Long id);

	public City save(City c);

	public City update(City c, Long id);
}
