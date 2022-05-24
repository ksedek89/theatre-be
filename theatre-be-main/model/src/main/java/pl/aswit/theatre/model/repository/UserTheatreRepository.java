package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Theatre;
import pl.aswit.theatre.model.entity.User;
import pl.aswit.theatre.model.entity.UserTheatre;

public interface UserTheatreRepository extends JpaRepository<UserTheatre, Integer> {
    UserTheatre findByTheatreAndUser(Theatre theatre, User user);
}
