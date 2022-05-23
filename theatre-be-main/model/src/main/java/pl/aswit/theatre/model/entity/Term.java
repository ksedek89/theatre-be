package pl.aswit.theatre.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@Data
public class Term {

    @Id
    private Long id;
    private Timestamp performanceDate;
    @ManyToOne
    @JoinColumn(name="play_id")
    private Play play;
    private Timestamp createDate;
    private boolean newsletterSent;
}
