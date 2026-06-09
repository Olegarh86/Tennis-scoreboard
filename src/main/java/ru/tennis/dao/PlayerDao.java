package ru.tennis.dao;

import org.hibernate.Session;
import ru.tennis.entity.Player;

import java.util.Optional;

public interface PlayerDao {

    Player save(Session session, Player player);

    Optional<Player> findByName(Session session, String name);
}
