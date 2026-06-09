package ru.tennis.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import ru.tennis.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerDaoImpl extends BaseDaoImpl<Player> implements PlayerDao {

    private static final String NAME = "name";
    private static final String GET_PLAYER_BY_NAME = """
            SELECT p
                        FROM Player p
                        WHERE p.name = :name""";

    @Override
    public Player save(Session session, Player player) {
        return super.save(session, player);
    }

    @Override
    public Optional<Player> findByName(Session session, String name) {
        return session.createQuery(GET_PLAYER_BY_NAME, Player.class)
                .setParameter(NAME, name)
                .uniqueResultOptional();
    }
}
