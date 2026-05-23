package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.CreateNewMatchException;
import ru.tennis.model.Player;
import ru.tennis.util.DataBaseUtil;
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
        Player player1;
        Player player2;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(persistenceService, session, 21);
            player1 = getPlayer(playerName1, session);
            player2 = getPlayer(playerName2, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CreateNewMatchException(e);
        }
        CurrentMatch currentMatch = new CurrentMatch(player1, player2);
        ongoingMatchesService.addMatch(currentMatch);
        return currentMatch.uuid;
    }

    private Player getPlayer(String playerName, Session session) {
        Optional<Player> mayBePlayer = persistenceService.getPlayerByName(session, playerName);
        return mayBePlayer.orElseGet(() -> persistenceService.createNewPlayer(session, playerName));
    }
}
