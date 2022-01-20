package sem.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sem.model.Patent;

public interface IPatent {

	public Iterable<Patent> findAll();

	public Page<Patent> findAll(Pageable pageable);

	public Optional<Patent> findById(Long id);

	public Patent save(Patent p);

	public Patent update(Patent p, Long id);

	public void deleteById(Long id);

}
