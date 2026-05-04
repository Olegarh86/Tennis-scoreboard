import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tennis.*;
import ru.tennis.model.Player;
import ru.tennis.service.MatchScoreCalculationService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchScoreCalculationTest {
    private CurrentMatch currentMatch;

    @BeforeEach
    public void createCurrentMatch() {
        currentMatch = new CurrentMatch(
                Player.builder().id(1).name("Ivan").build(),
                Player.builder().id(2).name("John").build());
    }

    @Test
    public void firstPlayerWinPointTest() {
        currentMatch.firstPlayer.setScore(new Score(0));
        currentMatch.secondPlayer.setScore(new Score(0));
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(15, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void playerWinGameTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(0));
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(1, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void playerWinSetTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(0));
        currentMatch.firstPlayer.setGame(Game.SIX);
        currentMatch.secondPlayer.setGame(Game.FIVE);
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(1, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void startTieBreakTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(0));
        currentMatch.firstPlayer.setGame(Game.FIVE);
        currentMatch.secondPlayer.setGame(Game.SIX);
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(6, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(6, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(true, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void winTieBreakTest() {
        currentMatch.firstPlayer.setScore(new TieBreak(6));
        currentMatch.secondPlayer.setScore(new TieBreak(5));
        currentMatch.firstPlayer.setGame(Game.SIX);
        currentMatch.secondPlayer.setGame(Game.SIX);
        currentMatch.firstPlayer.setSet(Set.ZERO);
        currentMatch.secondPlayer.setSet(Set.ZERO);
        currentMatch.tieBreak = true;
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.firstPlayer.score.getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.score.getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(1, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void notWinTieBreakTest() {
        currentMatch.firstPlayer.setScore(new TieBreak(12345));
        currentMatch.secondPlayer.setScore(new TieBreak(12345));
        currentMatch.firstPlayer.setGame(Game.SIX);
        currentMatch.secondPlayer.setGame(Game.SIX);
        currentMatch.firstPlayer.setSet(Set.ZERO);
        currentMatch.secondPlayer.setSet(Set.ZERO);
        currentMatch.tieBreak = true;
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(12346, currentMatch.firstPlayer.score.getScore()),
                () -> assertEquals(12345, currentMatch.secondPlayer.score.getScore()),
                () -> assertEquals(6, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(6, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(true, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void winMatchTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(0));
        currentMatch.firstPlayer.setGame(Game.SIX);
        currentMatch.secondPlayer.setGame(Game.FIVE);
        currentMatch.firstPlayer.setSet(Set.FOUR);
        currentMatch.secondPlayer.setSet(Set.ZERO);
        currentMatch.tieBreak = false;
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.firstPlayer.score.getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.score.getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(5, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(1, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(true, currentMatch.endMatch)
        );
    }

    @Test
    public void scoreEqualAndFirstPlayerWinPointTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(40));
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(50, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(40, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void scoreEqualAndSecondPlayerWinPointTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(40));
        MatchScoreCalculationService.updateMatchState(currentMatch, "2");
        Assertions.assertAll(
                () -> assertEquals(40, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(50, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void firstPlayerHaveAdvantageTest() {
        currentMatch.firstPlayer.setScore(new Score(40));
        currentMatch.secondPlayer.setScore(new Score(40));
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(50, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(40, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void firstPlayerWinGameWithAdvantageTest() {
        currentMatch.firstPlayer.setScore(new Score(50));
        currentMatch.secondPlayer.setScore(new Score(40));
        MatchScoreCalculationService.updateMatchState(currentMatch, "1");
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(0, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(1, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }

    @Test
    public void firstPlayerLosesAdvantageTest() {
        currentMatch.firstPlayer.setScore(new Score(50));
        currentMatch.secondPlayer.setScore(new Score(40));
        MatchScoreCalculationService.updateMatchState(currentMatch, "2");
        Assertions.assertAll(
                () -> assertEquals(40, currentMatch.firstPlayer.getScore().getScore()),
                () -> assertEquals(40, currentMatch.secondPlayer.getScore().getScore()),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getGame().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.firstPlayer.getSet().toString())),
                () -> assertEquals(0, Integer.parseInt(currentMatch.secondPlayer.getSet().toString())),
                () -> assertEquals(0, currentMatch.winnerId),
                () -> assertEquals(false, currentMatch.tieBreak),
                () -> assertEquals(false, currentMatch.endMatch)
        );
    }
}
