package brazilianchampionship.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "team1")
    private Team team1;
    @ManyToOne
    @JoinColumn(name = "team2")
    private Team team2;
    private Integer team1goals;
    private Integer team2goals;
    private Integer payingPublic;
    private LocalDateTime data;
    private Integer round;
    private Boolean finished;
}
