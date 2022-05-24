package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Registration;
import pl.aswit.theatre.model.entity.User;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
}
