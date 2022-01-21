package sem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // para el token;
    Optional<User> findByPhone(String phone);
    
    boolean existsByPhone(String Phone);

    boolean existsByMail(String mail);

}
