package brazilianchampionship.dto;

import lombok.Data;

@Data
public class ClassificationTeamDto implements Comparable<ClassificationTeamDto> {
    private String team;
    private Integer idTeam;
    private Integer position;
    private Integer points;
    private Integer matches;
    private Integer wins;
    private Integer tiedMatch;
    private Integer defeats;
    private Integer goalsScored;
    private Integer goalsConceded;

    @Override
    public int compareTo(ClassificationTeamDto o) {
        return this.getPoints().compareTo(o.getPoints());
    }
}
