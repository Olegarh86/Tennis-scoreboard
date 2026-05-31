import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tennis.dao.TennisDao;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.InvalidWinnerIdException;
import ru.tennis.exceptions.MatchNotFoundException;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.gameState.Game;
import ru.tennis.gameState.Score;
import ru.tennis.gameState.GameSet;
import ru.tennis.gameState.TieBreak;
import ru.tennis.model.Player;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.service.MatchScoreCalculationService;
import ru.tennis.service.MatchScoreService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchScoreCalculationTest {

    // После проведения декомпозиции и рефакторинга доменных моделей, также следует изменить тесты для этой части логики.

    // Невозможность протестировать MatchScoreService без классов других сервисов и их зависимостей превращает юнит-тест в интеграционный.
        // Это исправится после переноса основной бизнес-логики в классы моделей.

    // Текущий набор тестов покрывает лишь несколько базовых "счастливых путей". Отсутствуют тесты для многих важных сценариев:
        // - Игра "Больше/Меньше": `40-40` -> `AD-40` -> `40-40` -> `40-AD` -> `40-Game`.
        // - Завершение сета: Выигрыш сета со счётом `7-5` (без тай-брейка).
        // - Тай-брейк: Начало тай-брейка при счёте `6-6`, выигрыш со счётом `7-5`, продолжение игры при счёте `6-6`, `7-7`, выигрыш `9-7`.
        // - Матч: Корректное завершение матча при счёте `2-0` и `2-1`.

    private CurrentMatch currentMatch;
    private MatchScoreCalculationService calculationService;
    private OngoingMatchesService ongoingMatchesService;
    private FinishedMatchesPersistenceService persistenceService;
    private MatchScoreService matchScoreService;

    @BeforeEach
    public void createCurrentMatch() {
        HibernateUtil.init("hibernate.cfg.xml");

        currentMatch = new CurrentMatch(
                Player.builder().id(1).name("Ivan").build(),
                Player.builder().id(2).name("John").build());
        TennisDao tennisDao = new TennisDaoImpl();
        this.calculationService = new MatchScoreCalculationService();
        this.ongoingMatchesService = new OngoingMatchesService();
        this.persistenceService = new FinishedMatchesPersistenceService(tennisDao);
        this.matchScoreService = new MatchScoreService(ongoingMatchesService, persistenceService, calculationService);
    }

    @AfterEach
    public void destroyCurrentMatch() {
        HibernateUtil.destroy();
    }

    @Test
    public void firstPlayerWinPointTest() {
        currentMatch.getFirstPlayer().setScore(new Score(0));
        currentMatch.getSecondPlayer().setScore(new Score(0));
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(15, currentMatch.getFirstPlayer().getScore().getValue());
        assertEquals(0, currentMatch.getSecondPlayer().getScore().getValue());
    }

    @Test
    public void firstPlayerWinGameTest() {
        currentMatch.getFirstPlayer().setScore(new Score(40));
        currentMatch.getSecondPlayer().setScore(new Score(0));
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(Game.ONE, currentMatch.getFirstPlayer().getGame());
        assertEquals(Game.ZERO, currentMatch.getSecondPlayer().getGame());
    }

    @Test
    public void firstPlayerWinSetTest() {
        currentMatch.getFirstPlayer().setScore(new Score(40));
        currentMatch.getSecondPlayer().setScore(new Score(0));
        currentMatch.getFirstPlayer().setGame(Game.SIX);
        currentMatch.getSecondPlayer().setGame(Game.FIVE);
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(GameSet.ONE, currentMatch.getFirstPlayer().getGameSet());
    }

    @Test
    public void startTieBreakTest() {
        currentMatch.getFirstPlayer().setScore(new Score(40));
        currentMatch.getSecondPlayer().setScore(new Score(0));
        currentMatch.getFirstPlayer().setGame(Game.FIVE);
        currentMatch.getSecondPlayer().setGame(Game.SIX);
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(Game.SIX, currentMatch.getFirstPlayer().getGame());
        assertEquals(Game.SIX, currentMatch.getSecondPlayer().getGame());
    }

    @Test
    public void firstPlayerWinTieBreakTest() {
        currentMatch.getFirstPlayer().setScore(new TieBreak(6));
        currentMatch.getSecondPlayer().setScore(new TieBreak(5));
        currentMatch.getFirstPlayer().setGame(Game.SIX);
        currentMatch.getSecondPlayer().setGame(Game.SIX);
        currentMatch.getFirstPlayer().setGameSet(GameSet.ZERO);
        currentMatch.getSecondPlayer().setGameSet(GameSet.ZERO);
        currentMatch.setTieBreak(true);
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(GameSet.ONE, currentMatch.getFirstPlayer().getGameSet());
    }

    @Test
    public void notWinTieBreakTest() {
        currentMatch.getFirstPlayer().setScore(new TieBreak(12345));
        currentMatch.getSecondPlayer().setScore(new TieBreak(12345));
        currentMatch.getFirstPlayer().setGame(Game.SIX);
        currentMatch.getSecondPlayer().setGame(Game.SIX);
        currentMatch.getFirstPlayer().setGameSet(GameSet.ZERO);
        currentMatch.getSecondPlayer().setGameSet(GameSet.ZERO);
        currentMatch.setTieBreak(true);
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(12346, currentMatch.getFirstPlayer().getScore().getValue());
        assertEquals(12345, currentMatch.getSecondPlayer().getScore().getValue());
        assertTrue(currentMatch.isTieBreak());
    }

    @Test
    public void winMatchTest() {
        currentMatch.getFirstPlayer().setScore(new Score(40));
        currentMatch.getSecondPlayer().setScore(new Score(0));
        currentMatch.getFirstPlayer().setGame(Game.SIX);
        currentMatch.getSecondPlayer().setGame(Game.FIVE);
        currentMatch.getFirstPlayer().setGameSet(GameSet.ONE);
        currentMatch.getSecondPlayer().setGameSet(GameSet.ZERO);
        currentMatch.setTieBreak(false);
        calculationService.updateMatchState(currentMatch, 1);
        Assertions.assertAll(
                () -> assertEquals(0, currentMatch.getFirstPlayer().getScore().getValue()),
                () -> assertEquals(0, currentMatch.getSecondPlayer().getScore().getValue()),
                () -> assertEquals(Game.ZERO, currentMatch.getFirstPlayer().getGame()),
                () -> assertEquals(Game.ZERO, currentMatch.getSecondPlayer().getGame()),
                () -> assertEquals(GameSet.TWO, currentMatch.getFirstPlayer().getGameSet()),
                () -> assertEquals(GameSet.ZERO, currentMatch.getSecondPlayer().getGameSet()),
                () -> assertEquals(1, currentMatch.getWinner().getWinnerId()),
                () -> assertFalse(currentMatch.isTieBreak()),
                () -> assertTrue(currentMatch.hasWinner())
        );
    }

    @Test
    public void scoreEqualAndFirstPlayerWinPointTest() {
        currentMatch.getFirstPlayer().setScore(new Score(40));
        currentMatch.getSecondPlayer().setScore(new Score(40));
        calculationService.updateMatchState(currentMatch, 1);
        Assertions.assertAll(
                () -> assertEquals(50, currentMatch.getFirstPlayer().getScore().getValue()),
                () -> assertEquals(40, currentMatch.getSecondPlayer().getScore().getValue())
        );
    }

    @Test
    public void scoreEqualAndSecondPlayerWinPointTest() {
        currentMatch.getFirstPlayer().setScore(new Score(40));
        currentMatch.getSecondPlayer().setScore(new Score(40));
        calculationService.updateMatchState(currentMatch, 2);
        Assertions.assertAll(
                () -> assertEquals(40, currentMatch.getFirstPlayer().getScore().getValue()),
                () -> assertEquals(50, currentMatch.getSecondPlayer().getScore().getValue())
        );
    }

    @Test
    public void firstPlayerWinGameWithAdvantageTest() {
        currentMatch.getFirstPlayer().setScore(new Score(50));
        currentMatch.getSecondPlayer().setScore(new Score(40));
        calculationService.updateMatchState(currentMatch, 1);
        assertEquals(Game.ONE, currentMatch.getFirstPlayer().getGame());
    }

    @Test
    public void firstPlayerLosesAdvantageTest() {
        currentMatch.getFirstPlayer().setScore(new Score(50));
        currentMatch.getSecondPlayer().setScore(new Score(40));
        calculationService.updateMatchState(currentMatch, 2);
        Assertions.assertAll(
                () -> assertEquals(40, currentMatch.getFirstPlayer().getScore().getValue()),
                () -> assertEquals(40, currentMatch.getSecondPlayer().getScore().getValue())
        );
    }

    @Test
    public void matchScoreCalculationServiceThrowInvalidWinnerIdException() {
        assertThrows(InvalidWinnerIdException.class,
                () -> calculationService.updateMatchState(currentMatch, 1000));
    }

    @Test
    public void matchScoreCalculationServiceThrowSaveFinishedMatchException() {
        assertThrows(MatchNotFoundException.class,
                () -> matchScoreService.updateCurrentMatch(1, ""));
    }

    @Test
    public void ongoingMatchServiceAddAndGetNewCurrentMatch() {
        String uuid = currentMatch.getUuid();
        ongoingMatchesService.addMatch(currentMatch);
        assertTrue(ongoingMatchesService.getCurrentMatch(uuid).isPresent());
    }

    @Test
    public void ongoingMatchServiceDeleteCurrentMatch() {
        String uuid = currentMatch.getUuid();
        ongoingMatchesService.addMatch(currentMatch);
        assertTrue(ongoingMatchesService.getCurrentMatch(uuid).isPresent());
        ongoingMatchesService.deleteMatch(uuid);
        assertFalse(ongoingMatchesService.getCurrentMatch(uuid).isPresent());
    }

    @Test
    public void createNewPlayerAddToDataBaseAndGet() {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            persistenceService.createNewPlayer(session, "Zahar");
            transaction.commit();
            Optional<Player> mayBeZahar = persistenceService.getPlayerByName(session, "Zahar");
            Player zahar = mayBeZahar.get();
            assertEquals("Zahar", zahar.getName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SaveFinishedMatchException("Can't save match", e);
        }
    }
}
