package pl.aswit.theatre.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPlay {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private boolean active;
    @ManyToOne
    private User user;
    @ManyToOne
    private Play play;
    private Date statusChangeDate;
    private boolean notificationSent;

}
