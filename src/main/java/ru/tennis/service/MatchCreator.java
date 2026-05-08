package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.CurrentMatch;
import ru.tennis.model.Player;
import ru.tennis.util.DataBaseUtil;
import ru.tennis.util.HibernateUtil;

public class MatchCreator {

    public static CurrentMatch createMatch(String playerName1, String playerName2) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Player player1;
        Player player2;
        try {
            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(session, 21);
            player1 = FinishedMatchesPersistenceService.getPlayerByName(session, playerName1);
            player2 = FinishedMatchesPersistenceService.getPlayerByName(session, playerName2);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
        return new CurrentMatch(player1, player2);
    }
}
