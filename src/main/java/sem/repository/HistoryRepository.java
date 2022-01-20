package sem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sem.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
	@Query(value = "SELECT * FROM History h WHERE h.current_account_id =?1 ORDER BY h.date desc", nativeQuery = true)
	List<History> existsByCurrentAccount(Long id);

}
