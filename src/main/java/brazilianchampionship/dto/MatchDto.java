package brazilianchampionship.dto;

import brazilianchampionship.entities.Team;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MatchDto {
    private Integer id;
    private TeamDto team1;
    private TeamDto team2;
    private Integer team1goals;
    private Integer team2goals;
    private Integer payingPublic;
    private LocalDateTime data;
    private Integer round;
    private Boolean finished;
}
