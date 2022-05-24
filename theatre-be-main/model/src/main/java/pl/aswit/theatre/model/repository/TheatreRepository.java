package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Theatre;


public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    Theatre findByName(String name);
}