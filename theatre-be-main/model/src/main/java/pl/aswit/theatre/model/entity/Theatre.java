package pl.aswit.theatre.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Theatre {
    @Id
    private Long id;
    private String name;
    @OneToMany(mappedBy="theatre")
    private List<Play> playList;
}
