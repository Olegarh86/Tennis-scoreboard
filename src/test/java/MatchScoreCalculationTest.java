import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tennis.context.ApplicationContext;
import ru.tennis.exception.InvalidWinnerIdException;
import ru.tennis.exception.MatchNotFoundException;
import ru.tennis.model.CurrentMatch;
import ru.tennis.model.GameScore;
import ru.tennis.model.TennisPlayer;
import ru.tennis.service.MatchScoreService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchScoreCalculationTest {
    private static ApplicationContext applicationContext;
    private CurrentMatch currentMatch;
    private MatchScoreService matchScoreService;

    @BeforeEach
    public void createCurrentMatch() {
        applicationContext = new ApplicationContext();
        this.matchScoreService = applicationContext.getMatchScoreService();
        currentMatch = new CurrentMatch(new TennisPlayer(1, "Ivan"), new TennisPlayer(2, "John"));
    }

    @AfterEach
    public void destroyCurrentMatch() {
        applicationContext.close();
    }

    @Test
    public void firstPlayerWinPointTest() {
        currentMatch.updateMatchState(1);
        Assertions.assertAll(
                () -> assertEquals(GameScore.FIFTEEN, currentMatch.getFirstPlayer().getGameScore()),
                () -> assertEquals(GameScore.LOVE, currentMatch.getSecondPlayer().getGameScore())
        );
    }

    @Test
    public void firstPlayerWinGameTest() {
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(1);
        }
        Assertions.assertAll(
                () -> assertEquals(1, currentMatch.getFirstPlayer().getTennisSet().getPoints()),
                () -> assertEquals(0, currentMatch.getSecondPlayer().getTennisSet().getPoints())
        );
    }

    @Test
    public void firstPlayerWinSetTest() {
        for (int i = 0; i < 24; i++) {
            currentMatch.updateMatchState(1);
        }
        Assertions.assertAll(
                () -> assertEquals(GameScore.LOVE, currentMatch.getFirstPlayer().getGameScore()),
                () -> assertEquals(0, currentMatch.getFirstPlayer().getTennisSet().getPoints()),
                () -> assertEquals(1, currentMatch.getFirstPlayer().getTennisMatch().getPoints())
        );
    }

    @Test
    public void startTieBreakTest() {
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(2);
        }
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 5; i++) {
            currentMatch.updateMatchState(2);
        }
        Assertions.assertAll(
                () -> assertEquals("0", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("1", currentMatch.showSecondPlayerScore()),
                () -> assertEquals(6, currentMatch.getFirstPlayer().getTennisSet().getPoints()),
                () -> assertEquals(6, currentMatch.getSecondPlayer().getTennisSet().getPoints()),
                () -> assertEquals(0, currentMatch.getFirstPlayer().getTennisMatch().getPoints()),
                () -> assertEquals(0, currentMatch.getSecondPlayer().getTennisMatch().getPoints())
        );
    }

    @Test
    public void secondPlayerWinTieBreakTest() {
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(2);
        }
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 6; i++) {
            currentMatch.updateMatchState(2);
        }
        Assertions.assertAll(
                () -> assertEquals("0", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("0", currentMatch.showSecondPlayerScore()),
                () -> assertEquals(0, currentMatch.getFirstPlayer().getTennisSet().getPoints()),
                () -> assertEquals(0, currentMatch.getSecondPlayer().getTennisSet().getPoints()),
                () -> assertEquals(0, currentMatch.getFirstPlayer().getTennisMatch().getPoints()),
                () -> assertEquals(1, currentMatch.getSecondPlayer().getTennisMatch().getPoints())
        );
    }

    @Test
    public void notWinTieBreakTest() {
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(2);
        }
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(2);
        }
        for (int i = 0; i < 50; i++) {
            currentMatch.updateMatchState(1);
            currentMatch.updateMatchState(2);
        }
        currentMatch.updateMatchState(1);
        Assertions.assertAll(
                () -> assertEquals("51", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("50", currentMatch.showSecondPlayerScore()),
                () -> assertEquals(6, currentMatch.getFirstPlayer().getTennisSet().getPoints()),
                () -> assertEquals(6, currentMatch.getSecondPlayer().getTennisSet().getPoints()),
                () -> assertEquals(0, currentMatch.getFirstPlayer().getTennisMatch().getPoints()),
                () -> assertEquals(0, currentMatch.getSecondPlayer().getTennisMatch().getPoints())
        );
    }

    @Test
    public void winMatchWithScoreTwoZeroTest() {
        for (int i = 0; i < 48; i++) {
            currentMatch.updateMatchState(1);
        }
        assertEquals(currentMatch.getFirstPlayer(), currentMatch.getWinner());
    }

    @Test
    public void winMatchWithScoreTwoOneTest() {
        for (int i = 0; i < 24; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 24; i++) {
            currentMatch.updateMatchState(2);
        }
        for (int i = 0; i < 24; i++) {
            currentMatch.updateMatchState(1);
        }
        assertEquals(currentMatch.getFirstPlayer(), currentMatch.getWinner());
    }

    @Test
    public void scoreEqualAndFirstPlayerWinPointTest() {
        for (int i = 0; i < 3; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 3; i++) {
            currentMatch.updateMatchState(2);
        }
        currentMatch.updateMatchState(1);
        Assertions.assertAll(
                () -> assertEquals("AD", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("40", currentMatch.showSecondPlayerScore())
        );
    }

    @Test
    public void scoreEqualAndSecondPlayerWinPointTest() {
        for (int i = 0; i < 3; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 3; i++) {
            currentMatch.updateMatchState(2);
        }
        currentMatch.updateMatchState(2);
        Assertions.assertAll(
                () -> assertEquals("40", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("AD", currentMatch.showSecondPlayerScore())
        );
    }

    @Test
    public void firstPlayerDoTieTest() {
        for (int i = 0; i < 3; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(2);
        }
        currentMatch.updateMatchState(1);
        Assertions.assertAll(
                () -> assertEquals("40", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("40", currentMatch.showSecondPlayerScore())
        );
    }

    @Test
    public void firstPlayerWinWithoutTieBreakTest() {
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 20; i++) {
            currentMatch.updateMatchState(2);
        }
        for (int i = 0; i < 8; i++) {
            currentMatch.updateMatchState(1);
        }
        Assertions.assertAll(
                () -> assertEquals(1, currentMatch.getFirstPlayer().getTennisMatch().getPoints()),
                () -> assertEquals(0, currentMatch.getSecondPlayer().getTennisMatch().getPoints())
        );
    }

    @Test
    public void secondPlayerWinGameWithAdvantageTest() {
        for (int i = 0; i < 3; i++) {
            currentMatch.updateMatchState(1);
        }
        for (int i = 0; i < 4; i++) {
            currentMatch.updateMatchState(2);
        }
        currentMatch.updateMatchState(2);
        Assertions.assertAll(
                () -> assertEquals("0", currentMatch.showFirstPlayerScore()),
                () -> assertEquals("0", currentMatch.showSecondPlayerScore()),
                () -> assertEquals(0, currentMatch.getFirstPlayer().getTennisSet().getPoints()),
                () -> assertEquals(1, currentMatch.getSecondPlayer().getTennisSet().getPoints())
        );
    }

    @Test
    public void matchScoreCalculationServiceThrowInvalidWinnerIdException() {
        assertThrows(InvalidWinnerIdException.class,
                () -> currentMatch.updateMatchState(1000));
    }

    @Test
    public void matchScoreCalculationServiceThrowSaveFinishedMatchException() {
        assertThrows(MatchNotFoundException.class,
                () -> matchScoreService.updateCurrentMatch(1, UUID.randomUUID()));
    }
}
