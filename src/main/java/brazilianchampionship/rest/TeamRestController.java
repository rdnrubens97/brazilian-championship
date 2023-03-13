package brazilianchampionship.rest;

import brazilianchampionship.dto.TeamDto;
import brazilianchampionship.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/teams" )
public class TeamRestController {
    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDto> registerTeam(@RequestBody TeamDto team) throws Exception{
        return ResponseEntity.ok().body(teamService.registerTeam(team));
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAll(){
        return ResponseEntity.ok().body(teamService.getAllTeams());
    }

    @GetMapping(value = "{id}" )
    public ResponseEntity<TeamDto> getById(@PathVariable Integer id){
        return ResponseEntity.ok().body(teamService.getById(id));
    }



}
