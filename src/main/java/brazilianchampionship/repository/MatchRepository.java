package brazilianchampionship.repository;

import brazilianchampionship.entities.Match;
import brazilianchampionship.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByTeam1AndFinished(Team team1, Boolean finished);
    List<Match> findByTeam2AndFinished(Team team2, Boolean finished);
    List<Match> findByFinished(Boolean finished);
}
