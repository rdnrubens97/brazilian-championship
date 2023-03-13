package brazilianchampionship.service;

import brazilianchampionship.dto.ClassificationDto;
import brazilianchampionship.dto.ClassificationTeamDto;
import brazilianchampionship.dto.FinishedMatchDto;
import brazilianchampionship.dto.MatchDto;
import brazilianchampionship.entities.Match;
import brazilianchampionship.entities.Team;
import brazilianchampionship.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamService teamService;

    public void generateMatches(LocalDateTime firstRound){
        final List<Team> teams = teamService.findAll();
        List<Team> all1 = new ArrayList<>();
        List<Team> all2 = new ArrayList<>();
        all1.addAll(teams);
        all2.addAll(teams);
        matchRepository.deleteAll();
        List<Match> matches = new ArrayList<>();
        int t = teams.size();
        int m = teams.size() / 2;
        LocalDateTime matchDate = firstRound;
        Integer round = 0;
        for (int i = 0; i < t - 1; i += 1){
            round = i + 1;
            for (int j = 0; j < m ; j +=1 ){
                //teste para ajustar mando de campo:
                Team team1;
                Team team2;
                if (j % 2 == 1 || i % 2 == 1 && j == 0){
                    team1 = teams.get(t - j - 1);
                    team2 = teams.get(j);
                }
                else{
                    team1 = teams.get(j);
                    team2 = teams.get(t - j - 1);
                }
                if (team1 == null){
                    System.out.println("Team 1 null");
                }
                matches.add(generateMatche(matchDate, round, team1, team2));
                matchDate = matchDate.plusDays(7);
            }
            //Gira os times no sentido horário mantendo o primeiro no lugar
            teams.add(1, teams.remove(teams.size() - 1));
        }
        matches.forEach(match -> System.out.println(match));
        matchRepository.saveAll(matches);
        List<Match> matches2 = new ArrayList<>();
        matches.forEach(match -> {
            Team team1 = match.getTeam2();
            Team team2 = match.getTeam1();
            matches2.add(generateMatche(match.getData().plusDays(7 * matches.size()), match.getRound() + matches.size(), team1, team2));
        });
        matchRepository.saveAll(matches2);
    }

    private Match generateMatche(LocalDateTime matchDate, Integer round, Team team1, Team team2) {
        Match match = new Match();
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setRound(round);
        match.setData(matchDate);
        match.setFinished(false);
        match.setTeam1goals(0);
        match.setTeam2goals(0);
        match.setPayingPublic(0);
        return match;
    }

    private MatchDto entityToDto(Match entity){
        MatchDto matchDto = new MatchDto();
        matchDto.setId(entity.getId());
        matchDto.setData(entity.getData());
        matchDto.setFinished(entity.getFinished());
        matchDto.setTeam1goals(entity.getTeam1goals());
        matchDto.setTeam2goals(entity.getTeam2goals());
        matchDto.setPayingPublic(entity.getPayingPublic());
        matchDto.setRound(entity.getRound());
        matchDto.setTeam1(teamService.toDto(entity.getTeam1()));
        matchDto.setTeam2(teamService.toDto(entity.getTeam2()));
        return matchDto;
    }

    public List<MatchDto> getAllMatches(){
        return matchRepository.findAll()
                .stream()
                .map(entity -> entityToDto(entity)).
                collect(Collectors.toList());
    }

    public MatchDto finishMatch(Integer id, FinishedMatchDto matchDto) throws Exception {
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (optionalMatch.isPresent()){
            final Match match = optionalMatch.get();
            match.setTeam1goals(matchDto.getTeam1goals());
            match.setTeam2goals(matchDto.getTeam2goals());
            match.setFinished(true);
            match.setPayingPublic(matchDto.getPayingPublic());
            return entityToDto(matchRepository.save(match));
        }
        else {
            throw new Exception("The match does not exist");
        }
    }

    public ClassificationDto getClassification() {
        // ((qtde de vitórias * 3) + qtde de empates)
        ClassificationDto classificationDto = new ClassificationDto();
        final List<Team> teams = teamService.findAll();
        teams.forEach(team -> {
            final List<Match> homeMatches = matchRepository.findByTeam1AndFinished(team, true);
            final List<Match> awayMatches = matchRepository.findByTeam2AndFinished(team, true);
            AtomicReference<Integer> wins = new AtomicReference<>(0);
            AtomicReference<Integer> tiedMatch = new AtomicReference<>(0);
            AtomicReference<Integer> defeats = new AtomicReference<>(0);
            AtomicReference<Integer> goalsScored = new AtomicReference<>(0);
            AtomicReference<Integer> goalsConceded = new AtomicReference<>(0);
            homeMatches.forEach(match -> {
                if (match.getTeam1goals() > match.getTeam2goals()){
                    wins.getAndSet(wins.get() + 1);
                }
                else if (match.getTeam1goals() < match.getTeam2goals()){
                    defeats.getAndSet(defeats.get() + 1);
                }
                else{
                    tiedMatch.getAndSet(tiedMatch.get() + 1);
                }
                goalsScored.set(goalsScored.get() + match.getTeam1goals());
                goalsConceded.set(goalsConceded.get() + match.getTeam1goals());
            });
            awayMatches.forEach(match -> {
                if (match.getTeam2goals() > match.getTeam1goals()){
                    wins.getAndSet(wins.get() + 1);
                }
                else if (match.getTeam2goals() < match.getTeam1goals()){
                    defeats.getAndSet(defeats.get() + 1);
                }
                else{
                    tiedMatch.getAndSet(tiedMatch.get() + 1);
                }
                goalsScored.set(goalsScored.get() + match.getTeam2goals());
                goalsConceded.set(goalsConceded.get() + match.getTeam2goals());
            });
            ClassificationTeamDto classificationTeamDto = new ClassificationTeamDto();
            classificationTeamDto.setIdTeam(team.getId());
            classificationTeamDto.setTeam(team.getName());
            classificationTeamDto.setPoints((wins.get() * 3) + tiedMatch.get());
            classificationTeamDto.setDefeats(defeats.get());
            classificationTeamDto.setTiedMatch(tiedMatch.get());
            classificationTeamDto.setWins(wins.get());
            classificationTeamDto.setGoalsScored(goalsScored.get());
            classificationTeamDto.setGoalsConceded(goalsConceded.get());
            classificationTeamDto.setMatches(defeats.get() + tiedMatch.get() + wins.get());
            classificationDto.getTeams().add(classificationTeamDto);
        });
        Collections.sort(classificationDto.getTeams(), Collections.reverseOrder());
        int position = 0;
        for (ClassificationTeamDto team: classificationDto.getTeams()){
            team.setPosition(position += 1);
        }
        return classificationDto;
    }

    public MatchDto getMatchById(Integer id) {
        return entityToDto(matchRepository.findById(id).get());
    }
}
