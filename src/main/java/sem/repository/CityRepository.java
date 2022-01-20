package sem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sem.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
