package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.CurrentMatch;
import ru.tennis.dto.MatchCreateDto;
import ru.tennis.exceptions.CreateNewMatchException;
import ru.tennis.model.Player;
import ru.tennis.util.DataBaseUtil;
import ru.tennis.util.HibernateUtil;

public class MatchCreator {

    public static MatchCreateDto createNewCurrentMatch(String playerName1, String playerName2) {
        Player player1;
        Player player2;
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(session, 21);
            player1 = FinishedMatchesPersistenceService.getPlayerByName(session, playerName1);
            player2 = FinishedMatchesPersistenceService.getPlayerByName(session, playerName2);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new CreateNewMatchException(e.getMessage());
        }
        CurrentMatch currentMatch = new CurrentMatch(player1, player2);
        OngoingMatchesService.addMatch(currentMatch);
        return new MatchCreateDto(currentMatch.uuid);
    }
}
