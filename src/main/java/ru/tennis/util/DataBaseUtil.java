package ru.tennis.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

@UtilityClass
public class DataBaseUtil {


    public static void addNMatches(Session session, int n) {

        for (int i = 0; i < n; i++) {
            Player player1 = createRandomPlayer(session);
            Player player2 = createRandomPlayer(session);
            Player winner = changeRandomWinner(player1, player2);
            Match match = Match.builder().player1(player1).player2(player2).winner(winner).build();
            session.persist(match);
        }
    }

    private static Player changeRandomWinner(Player player1, Player player2) {
        if (Math.random() < 0.5) {
            return player2;
        } else {
            return player1;
        }
    }

    private static Player createRandomPlayer(Session session) {
        String randomName = null;
        Player player = new Player();

        while (player != null) {
            randomName = getRandomName();
            player = findRandomPlayerInDB(session, randomName);
        }
        Player randomPlayer = Player.builder().name(randomName).build();
        session.persist(randomPlayer);
        return randomPlayer;
    }

    private static Player findRandomPlayerInDB(Session session, String randomName) {
        return session.createQuery("select p from Player p where p.name = :name", Player.class)
                .setParameter("name", randomName)
                .uniqueResult();
    }

    private static String getRandomName() {
        double random = Math.random() * 100;
        return String.valueOf(random);
    }
}
