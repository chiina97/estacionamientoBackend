package sem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sem.model.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
	Optional<Holiday> findByDate(String date);

}
