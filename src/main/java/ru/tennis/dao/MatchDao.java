package ru.tennis.dao;

import org.hibernate.Session;
import ru.tennis.entity.Match;

import java.util.List;

public interface MatchDao {

    Match save(Session session, Match match);

    List<Match> findAll(Session session,int pageSize, int offset);

    List<Match> findAll(Session session,String playerName, int pageSize, int offset);

    Long countAll(Session session,String playerName);

    Long countAll(Session session);
}
