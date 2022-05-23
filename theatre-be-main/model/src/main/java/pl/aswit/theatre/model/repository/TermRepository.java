package pl.aswit.theatre.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aswit.theatre.model.entity.Term;

public interface TermRepository extends JpaRepository<Term, Integer> {
}
