package pl.aswit.theatre.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Play {
    @Id
    private Long id;
    private String name;
    private String link;
    @OneToMany(mappedBy="play")
    private List<Term> termList;
    @ManyToOne
    @JoinColumn(name="theatre_id")
    private Theatre theatre;

}
