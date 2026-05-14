package ru.tennis.dao;

import org.hibernate.Session;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public interface TennisDao {
    Player createNewTennisPlayer(Session session, String name);
    Optional<Player> getTennisPlayerByName(Session session, String name);
    void saveFinishedTennisMatch(Session session, CurrentMatch currentMatch);
    List<Match> getAllTennisMatches(Session session, Optional<String> playerName, int pageSize, int offset);
    Long getTotalNumberAllTennisMatches(Session session, Optional<String> playerName);
}
