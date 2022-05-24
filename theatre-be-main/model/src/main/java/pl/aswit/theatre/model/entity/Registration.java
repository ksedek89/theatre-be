package pl.aswit.theatre.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
