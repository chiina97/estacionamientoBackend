package sem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sem.model.Parking;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
	@Query(value = "SELECT p FROM Parking p WHERE p.patent =?1 and p.startedParking=true")
	Parking findByPatentStarted(String patent);

	@Query(value = "SELECT * FROM Parking p WHERE p.started_parking = 1 and p.user_id=?1", nativeQuery = true)
	Optional<Parking> findStartedParkingBy(Long id);

}
