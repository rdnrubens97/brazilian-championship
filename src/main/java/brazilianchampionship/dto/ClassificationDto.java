package brazilianchampionship.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClassificationDto {
    private List<ClassificationTeamDto> teams= new ArrayList<>();
}
