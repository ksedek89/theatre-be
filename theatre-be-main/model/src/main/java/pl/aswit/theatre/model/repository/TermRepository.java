package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Play;
import pl.aswit.theatre.model.entity.Term;

import java.util.Date;

public interface TermRepository extends JpaRepository<Term, Integer> {
    Term findByPlayAndPerformanceDate(Play play, Date performanceName);
}
