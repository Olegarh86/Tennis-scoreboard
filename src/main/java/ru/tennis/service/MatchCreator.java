package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.dto.MatchCreateDto;
import ru.tennis.exceptions.CreateNewMatchException;
import ru.tennis.model.Player;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public class MatchCreator {
    private static final FinishedMatchesPersistenceService service =
            new FinishedMatchesPersistenceService(new TennisDaoImpl());

    public static MatchCreateDto createNewCurrentMatch(String playerName1, String playerName2) {
        Player player1;
        Player player2;
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
//            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(session, 21);
            player1 = getPlayer(playerName1, session);
            player2 = getPlayer(playerName2, session);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new CreateNewMatchException(e.getMessage());
        }
        CurrentMatch currentMatch = new CurrentMatch(player1, player2);
        OngoingMatchesService.addMatch(currentMatch);
        return new MatchCreateDto(currentMatch.uuid);
    }

    private static Player getPlayer(String playerName, Session session) {
        Optional<Player> mayBePlayer = service.getPlayerByName(session, playerName);
        return mayBePlayer.orElseGet(() -> service.createNewPlayer(session, playerName));
    }
}
