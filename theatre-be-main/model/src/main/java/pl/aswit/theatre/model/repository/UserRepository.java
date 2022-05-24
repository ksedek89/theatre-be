package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findAllByActive(Boolean active);
}
