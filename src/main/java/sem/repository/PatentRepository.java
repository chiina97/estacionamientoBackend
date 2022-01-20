package sem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sem.model.Patent;

@Repository
public interface PatentRepository extends JpaRepository<Patent, Long> {
	@Query(value = "SELECT p FROM Patent p WHERE p.user.id =?1")
	List<Patent> findAllPatentsByUser(Long id);

	@Query(value = "SELECT p FROM Patent p WHERE p.patent=?1 and p.user.id =?2")
	Patent existsByPatentAndUser(String patent, Long idUser);
}
