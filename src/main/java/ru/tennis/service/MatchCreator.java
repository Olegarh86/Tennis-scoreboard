package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.dto.MatchCreateDto;
import ru.tennis.exceptions.CreateNewMatchException;
import ru.tennis.model.Player;
import ru.tennis.util.DataBaseUtil;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public class MatchCreator {

    public MatchCreateDto createNewCurrentMatch(OngoingMatchesService ongoingMatchesService, FinishedMatchesPersistenceService service, String playerName1, String playerName2) {
        Player player1;
        Player player2;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(service, session, 21);
            player1 = getPlayer(service, playerName1, session);
            player2 = getPlayer(service, playerName2, session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CreateNewMatchException("Can't create new match", e);
        }
        CurrentMatch currentMatch = new CurrentMatch(player1, player2);
        ongoingMatchesService.addMatch(currentMatch);
        return new MatchCreateDto(currentMatch.uuid);
    }

    private Player getPlayer(FinishedMatchesPersistenceService service, String playerName, Session session) {
        Optional<Player> mayBePlayer = service.getPlayerByName(session, playerName);
        return mayBePlayer.orElseGet(() -> service.createNewPlayer(session, playerName));
    }
}
