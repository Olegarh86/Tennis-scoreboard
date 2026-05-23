package ru.tennis.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.Optional;

@UtilityClass
public class DataBaseUtil {

    public static void addNFinishedMatchesWithRandomPlayers(FinishedMatchesPersistenceService persistenceService, Session session, int n) {
        for (int i = 0; i < n; i++) {
            Player player1 = createRandomPlayer(persistenceService, session);
            Player player2 = createRandomPlayer(persistenceService, session);
            Player winner = changeRandomWinner(player1, player2);
            Match match = Match.builder().player1(player1).player2(player2).winner(winner).build();
            session.persist(match);
        }
    }

    private static Player createRandomPlayer(FinishedMatchesPersistenceService persistenceService, Session session) {
        String randomName = null;
        Optional<Player> player = Optional.of(new Player());

        while (player.isPresent()) {
            randomName = getRandomName();
            player = persistenceService.getPlayerByName(session, randomName);
        }
        return persistenceService.createNewPlayer(session, randomName);
    }

    private static String getRandomName() {
        int random = (int) (Math.random() * 10000);
        return String.valueOf(random);
    }

    private static Player changeRandomWinner(Player player1, Player player2) {
        if (Math.random() < 0.5) {
            return player2;
        } else {
            return player1;
        }
    }
}
