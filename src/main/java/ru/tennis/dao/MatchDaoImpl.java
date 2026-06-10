package ru.tennis.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import ru.tennis.entity.Match;

import java.util.List;

@RequiredArgsConstructor
public class MatchDaoImpl extends BaseDaoImpl<Match> implements MatchDao {
    private static final String PLAYER_NAME = "playerName";
    private static final String GET_ALL_MATCHES_QUERY = """
            SELECT m
                        FROM Match m
                        JOIN FETCH m.player1
                        JOIN FETCH m.player2
                        JOIN FETCH m.winner
                        ORDER BY m.id DESC""";
    private static final String GET_ALL_MATCHES_BY_NAME_QUERY = """
            SELECT m
                        FROM Match m
                        JOIN FETCH m.player1
                        JOIN FETCH m.player2
                        JOIN FETCH m.winner
                        WHERE m.player1.name = :playerName
                           OR m.player2.name = :playerName
                        ORDER BY m.id DESC""";
    private static final String GET_TOTAL_NUMBER_MATCHES = """
            SELECT count(*)
                        FROM Match""";
    private static final String GET_TOTAL_NUMBER_MATCHES_BY_NAME = """
            SELECT count(*)
                                FROM Match
                                WHERE player1.name = :playerName
                                OR player2.name = :playerName""";

    @Override
    public Match save(Session session, Match match) {
        return super.save(session, match);
    }

    @Override
    public List<Match> findAll(Session session, String playerName, int pageSize, int offset) {
        return session.createQuery(GET_ALL_MATCHES_BY_NAME_QUERY, Match.class)
                .setParameter(PLAYER_NAME, playerName)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<Match> findAll(Session session, int pageSize, int offset) {
        return session.createQuery(GET_ALL_MATCHES_QUERY, Match.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Long countAll(Session session, String playerName) {
        return session.createQuery(GET_TOTAL_NUMBER_MATCHES_BY_NAME, Long.class)
                .setParameter(PLAYER_NAME, playerName)
                .uniqueResult();
    }

    @Override
    public Long countAll(Session session) {
        return session.createQuery(GET_TOTAL_NUMBER_MATCHES, Long.class).uniqueResult();
    }
}
