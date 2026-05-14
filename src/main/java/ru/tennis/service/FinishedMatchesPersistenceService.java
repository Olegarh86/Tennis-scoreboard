package ru.tennis.service;

import org.hibernate.Session;
import ru.tennis.dao.TennisDao;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public class FinishedMatchesPersistenceService {
    private final TennisDao tennisDao = new TennisDaoImpl();

    public Player createNewPlayer(Session session, String name) {
        return tennisDao.createNewTennisPlayer(session, name);
    }

    public Optional<Player> getPlayerByName(Session session, String name) {
        return tennisDao.getTennisPlayerByName(session, name);
    }

    public void saveFinishedMatch(Session session, CurrentMatch currentMatch) {
        tennisDao.saveFinishedTennisMatch(session, currentMatch);
    }

    public List<Match> getAllMatches(Session session, Optional<String> playerName, int pageSize, int offset) {
        return tennisDao.getAllTennisMatches(session, playerName, pageSize, offset);
    }

    public Long getTotalNumberOfMatches(Session session, Optional<String> playerName) {
        return tennisDao.getTotalNumberAllTennisMatches(session, playerName);
    }
}
