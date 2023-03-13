package brazilianchampionship.service;
import brazilianchampionship.dto.TeamDto;
import brazilianchampionship.entities.Team;
import brazilianchampionship.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    private TeamRepository repository;
    public TeamDto registerTeam(TeamDto team) throws Exception{
        Team entity = toEntity(team);
        if (team.getId() == null) {
            Integer newId = Math.toIntExact(repository.count() + 1);
            entity.setId(newId);
            entity = repository.save(entity);
            return toDto(entity);
        }
        else {
            throw new Exception("Team already exist");
        }
    }

    public TeamDto getById(Integer id){
        return toDto(repository.findById(id).get());
    }

    public List<TeamDto> getAllTeams(){
        return repository.findAll()
                .stream()
                .map(entity -> toDto(entity)).
                collect(Collectors.toList());
    }

    private Team toEntity(TeamDto team){
        Team entity = new Team();
        entity.setId(team.getId());
        entity.setName(team.getName());
        entity.setInitials(team.getInitials());
        entity.setUfEstate(team.getUfEstate());
        entity.setStadium(team.getStadium());
        return entity;
    }

    public TeamDto toDto(Team team){
        TeamDto teamDTO = new TeamDto();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setInitials(team.getInitials());
        teamDTO.setUfEstate(team.getUfEstate());
        teamDTO.setStadium(team.getStadium());
        return teamDTO;
    }

    public List<Team> findAll() {
        return repository.findAll();
    }
}
