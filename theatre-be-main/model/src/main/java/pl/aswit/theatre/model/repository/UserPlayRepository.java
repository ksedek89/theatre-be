package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.*;

public interface UserPlayRepository extends JpaRepository<UserPlay, Integer> {
    UserPlay findByPlayAndUser(Play play, User user);
}
