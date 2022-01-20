package sem.service;

import java.util.Optional;

import sem.model.Parking;

public interface IParking {

	public Iterable<Parking> findAll();

	public Optional<Parking> findById(Long id);

	public Parking save(Parking e);

	public Parking updateAmount(Parking e, Long id);

}
