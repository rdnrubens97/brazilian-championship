package brazilianchampionship.rest;

import brazilianchampionship.dto.ClassificationDto;
import brazilianchampionship.dto.FinishedMatchDto;
import brazilianchampionship.dto.MatchDto;
import brazilianchampionship.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/matches" )
public class MatchRestController {
    @Autowired
    public MatchService matchService;

    @PostMapping(value = "/generate-matches")
    public ResponseEntity<Void> generateMatches(){
        matchService.generateMatches(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<MatchDto>> getMatches(){
        return ResponseEntity.ok().body(matchService.getAllMatches());
    }

    @PostMapping(value = "/finish-match/{id}")
    public ResponseEntity<MatchDto> finishMatch(@PathVariable Integer id, @RequestBody FinishedMatchDto finishedMatchDto) throws Exception{
        return ResponseEntity.ok().body(matchService.finishMatch(id, finishedMatchDto));
    }

    @GetMapping(value = "/classification")
    public ResponseEntity<ClassificationDto> classification(){
        return ResponseEntity.ok().body(matchService.getClassification());
    }

    @GetMapping(value = "/match/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable Integer id){
        return ResponseEntity.ok().body(matchService.getMatchById(id));
    }


}

