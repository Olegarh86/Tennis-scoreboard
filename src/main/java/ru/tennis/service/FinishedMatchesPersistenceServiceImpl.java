package ru.tennis.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.tennis.dao.MatchDao;
import ru.tennis.dao.PlayerDao;
import ru.tennis.entity.Match;
import ru.tennis.entity.Player;
import ru.tennis.exception.DataAccessException;
import ru.tennis.exception.MatchNotFoundException;
import ru.tennis.model.CurrentMatch;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FinishedMatchesPersistenceServiceImpl implements FinishedMatchesPersistenceService {
    private final PlayerDao playerDao;
    private final MatchDao matchDao;
    private final SessionFactory sessionFactory;

    public Player createNewPlayer(String playerName) {
        Player player = new Player(playerName);
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            player = playerDao.save(session, player);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return player;
    }

    @Override
    public Optional<Player> getPlayerByName(String name) {
        Optional<Player> optionalPlayer;
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            optionalPlayer = playerDao.findByName(session, name);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return optionalPlayer;
    }

    @Override
    public Match saveFinishedMatch(CurrentMatch currentMatch) {
        Match match = buildFinishedMatch(currentMatch);
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            match = matchDao.save(session, match);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return match;
    }

    @Override
    public List<Match> findAll(String playerName, int pageSize, int offset) {
        List<Match> list;
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            list = matchDao.findAll(session, playerName, pageSize, offset);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return list;
    }

    @Override
    public List<Match> findAll(int pageSize, int offset) {
        List<Match> list;
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            list = matchDao.findAll(session, pageSize, offset);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return list;
    }

    @Override
    public Long countAll(String playerName) {
        Long number;
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            number = matchDao.countAll(session, playerName);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return number;
    }

    @Override
    public Long countAll() {
        Long number;
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            number = matchDao.countAll(session);
            transaction.commit();
        } catch (Exception ex) {
            safeRollback(transaction, ex);
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return number;
    }

    private Match buildFinishedMatch(CurrentMatch currentMatch) {
        Optional<Player> firstPlayer = getPlayerByName(currentMatch.getFirstPlayer().getName());
        Optional<Player> secondPlayer = getPlayerByName(currentMatch.getSecondPlayer().getName());
        Optional<Player> winner = getPlayerByName(currentMatch.getWinner().getName());

        if (firstPlayer.isPresent() && secondPlayer.isPresent() && winner.isPresent()) {
            return new Match(firstPlayer.get(), secondPlayer.get(), winner.get());
        }
        throw new MatchNotFoundException(currentMatch.getUuid().toString());
    }

    private void safeRollback(Transaction transaction, Exception originalException) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }
}
