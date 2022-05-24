package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Play;
import pl.aswit.theatre.model.entity.User;
import pl.aswit.theatre.model.entity.UserPlay;

import java.util.List;

public interface UserPlayRepository extends JpaRepository<UserPlay, Integer> {
    UserPlay findByPlayAndUser(Play play, User user);
    List<UserPlay> findAllByUser(User user);
}
