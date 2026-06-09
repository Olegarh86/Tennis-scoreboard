package ru.tennis.service;

import ru.tennis.entity.Match;
import ru.tennis.entity.Player;
import ru.tennis.model.CurrentMatch;

import java.util.List;
import java.util.Optional;

public interface FinishedMatchesPersistenceService {

    Player createNewPlayer(String playerName);

    Optional<Player> getPlayerByName(String name);

    Match saveFinishedMatch(CurrentMatch currentMatch);

    List<Match> findAll(String playerName, int pageSize, int offset);

    List<Match> findAll(int pageSize, int offset);

    Long countAll(String playerName);

    Long countAll();
}


