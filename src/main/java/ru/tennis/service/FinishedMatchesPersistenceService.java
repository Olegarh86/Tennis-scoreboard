package ru.tennis.service;

import org.hibernate.Session;
import ru.tennis.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public class FinishedMatchesPersistenceService {

    public static Player getPlayer(Session session, String name) {

        Player player = session.createQuery("select p from Player p where p.name = :name", Player.class)
                .setParameter("name", name)
                .uniqueResult();

        if (player == null) {
            player = Player.builder().name(name).build();
            session.persist(player);
        }

        return player;
    }

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

    public static List<Match> getAllMatches(Session session, Optional<String> playerName, int pageSize, int offset) {
        String query;
        if (playerName.isEmpty()) {
            query = "select m from Match m order by id LIMIT :pageSize OFFSET :offset";
            return session.createQuery(query, Match.class)
                    .setParameter("pageSize", pageSize)
                    .setParameter("offset", offset)
                    .list();
        } else {
            String name = playerName.get();
            query = "select m from Match m where m.player1.name = :playerName OR m.player2.name = :playerName " +
                    "order by id LIMIT :pageSize OFFSET :offset";
            return session.createQuery(query, Match.class)
                    .setParameter("playerName", name)
                    .setParameter("pageSize", pageSize)
                    .setParameter("offset", offset)
                    .list();
        }
    }

    public static Long getTotalNumberOfMatches(Session session, Optional<String> playerName) {
        String query;
        if (playerName.isEmpty()) {
            query = "select count(*) from Match";
            return session.createQuery(query, Long.class).uniqueResult();
        } else {
            String name = playerName.get();
            query = "select count(*) from Match where player1.name = :playerName OR player2.name = :playerName";
            return session.createQuery(query, Long.class)
                    .setParameter("playerName", name)
                    .uniqueResult();
        }
    }
}
