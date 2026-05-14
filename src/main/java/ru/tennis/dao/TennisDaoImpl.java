package ru.tennis.dao;

import org.hibernate.Session;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public class TennisDaoImpl implements TennisDao {
    private static final String GET_PLAYER_BY_NAME = "select p from Player p where p.name = :name";
    private static final String GET_ALL_MATCHES_QUERY = "select m from Match m order by id LIMIT :pageSize OFFSET :offset";
    private static final String GET_ALL_MATCHES_BY_NAME_QUERY = "select m from Match m where m.player1.name = " +
                                                                ":playerName OR m.player2.name = :playerName order by id" +
                                                                " LIMIT :pageSize OFFSET :offset";
    private static final String GET_TOTAL_NUMBER_MATCHES = "select count(*) from Match";
    private static final String GET_TOTAL_NUMBER_MATCHES_BY_NAME = "select count(*) from Match where player1.name = " +
                                                                   ":playerName OR player2.name = :playerName";

    @Override
    public Player createNewTennisPlayer(Session session, String name) {
        Player player = Player.builder().name(name).build();
        session.persist(player);
        return player;
    }

    @Override
    public Optional<Player> getTennisPlayerByName(Session session, String name) {
        return Optional.ofNullable(session.createQuery(GET_PLAYER_BY_NAME, Player.class)
                .setParameter("name", name)
                .uniqueResult());
    }

    @Override
    public void saveFinishedTennisMatch(Session session, CurrentMatch currentMatch) {
        Player player1 = Player.builder().id(currentMatch.firstPlayer.id).name(currentMatch.firstPlayer.name).build();
        Player player2 = Player.builder().id(currentMatch.secondPlayer.id).name(currentMatch.secondPlayer.name).build();
        Player winner = Player.builder().id(currentMatch.winner.getWinnerId()).name(currentMatch.winner.getWinnerName()).build();
        Match match = Match.builder().player1(player1).player2(player2).winner(winner).build();
        session.persist(match);
    }

    @Override
    public List<Match> getAllTennisMatches(Session session, Optional<String> playerName, int pageSize, int offset) {
        if (playerName.isEmpty()) {
            return session.createQuery(GET_ALL_MATCHES_QUERY, Match.class)
                    .setParameter("pageSize", pageSize)
                    .setParameter("offset", offset)
                    .list();
        } else {
            return session.createQuery(GET_ALL_MATCHES_BY_NAME_QUERY, Match.class)
                    .setParameter("playerName", playerName.get())
                    .setParameter("pageSize", pageSize)
                    .setParameter("offset", offset)
                    .list();
        }
    }

    @Override
    public Long getTotalNumberAllTennisMatches(Session session, Optional<String> playerName) {
        if (playerName.isEmpty()) {
            return session.createQuery(GET_TOTAL_NUMBER_MATCHES, Long.class).uniqueResult();
        } else {
            return session.createQuery(GET_TOTAL_NUMBER_MATCHES_BY_NAME, Long.class)
                    .setParameter("playerName", playerName.get())
                    .uniqueResult();
        }
    }
}
