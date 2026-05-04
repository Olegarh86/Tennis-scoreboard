package ru.tennis.service;

import org.hibernate.Session;
import ru.tennis.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;

public class FinishedMatchesPersistenceService {

    public static void persist(Session session, CurrentMatch currentMatch) {
        int playerId1 = currentMatch.firstPlayer.id;
        Player player1 = session.createQuery("select p from Player p where p.id = :playerId1", Player.class)
                .setParameter("playerId1", playerId1)
                .uniqueResult();

        int playerId2 = currentMatch.secondPlayer.id;
        Player player2 = session.createQuery("select p from Player p where p.id = :playerId2", Player.class)
                .setParameter("playerId2", playerId2)
                .uniqueResult();

        Integer winnerId = currentMatch.winnerId;
        Player winner = null;
        if (winnerId.equals(playerId1)) {
            winner = player1;
        }

        if (winnerId.equals(playerId2)) {
            winner = player2;
        }

        Match match = session.createQuery("select m from Match m where m.player1 = :player1 and m.player2 = " +
                                          ":player2 and m.winner = :winner", Match.class)
                .setParameter("player1", player1)
                .setParameter("player2", player2)
                .setParameter("winner", winner)
                .uniqueResult();

        if (match == null) {
            match = Match.builder().player1(player1).player2(player2).winner(winner).build();
            session.persist(match);
        }
    }

    public static List<Match> getAllMatches(Session session, int pageSize, int offset) {
        String query = "from Match order by id LIMIT %d OFFSET %d";
        return session.createQuery(String.format(query, pageSize, offset), Match.class).list();
    }

    public static List<Match> getMatchesByPlayerName(Session session, String playerName, int pageSize, int offset) {
        String query = "select m from Match m where m.player1.name = :playerName OR m.player2.name = :playerName " +
                       "order by id LIMIT %d OFFSET %d";
        String finalQuery = String.format(query, pageSize, offset);
        return session.createQuery(finalQuery, Match.class)
                .setParameter("playerName", playerName)
                .list();
    }

    public static Long getTotalCountWithName(Session session, String playerName) {
        String hql = "select count(*) from Match where player1.name = :playerName";
        return session.createQuery(hql, Long.class)
                .setParameter("playerName", playerName)
                .uniqueResult();
    }

    public static Long getTotalCount(Session session) {
        String hql = "select count(*) from Match";
        return session.createQuery(hql, Long.class).uniqueResult();
    }
}
