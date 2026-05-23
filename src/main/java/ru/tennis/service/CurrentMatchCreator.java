package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.CreateNewMatchException;
import ru.tennis.model.Player;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public class CurrentMatchCreator {
    private final FinishedMatchesPersistenceService persistenceService;
    private final OngoingMatchesService ongoingMatchesService;

    public CurrentMatchCreator(FinishedMatchesPersistenceService persistenceService, OngoingMatchesService ongoingMatchesService) {
        this.persistenceService = persistenceService;
        this.ongoingMatchesService = ongoingMatchesService;
    }

    public String createNewCurrentMatch(String playerName1, String playerName2) {
        CurrentMatch currentMatch;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
//            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(persistenceService, session, 21);
            Player player1 = getOrCreatePlayer(playerName1, session);
            Player player2 = getOrCreatePlayer(playerName2, session);
            currentMatch = new CurrentMatch(player1, player2);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CreateNewMatchException(e);
        }
        ongoingMatchesService.addMatch(currentMatch);
        return currentMatch.getUuid();
    }

    private Player getOrCreatePlayer(String playerName, Session session) {
        Optional<Player> mayBePlayer = persistenceService.getPlayerByName(session, playerName);
        return mayBePlayer.orElseGet(() -> persistenceService.createNewPlayer(session, playerName));
    }
}
