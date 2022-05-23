package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Play;

public interface PlayRepository extends JpaRepository<Play, Integer> {
}
