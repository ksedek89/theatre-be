package pl.aswit.theatre.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Date performanceDate;
    @ManyToOne
    @JoinColumn(name="play_id")
    private Play play;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


}
