package ru.tennis.service;

import ru.tennis.dto.CurrentMatch;
import ru.tennis.dto.Winner;
import ru.tennis.exceptions.InvalidWinnerIdException;
import ru.tennis.gameState.Game;
import ru.tennis.gameState.Score;
import ru.tennis.gameState.TieBreak;


public class MatchScoreCalculationService {

    // Составные условия из if лучше выносить во вспомогательный метод с понятным названием.
        // Это улучшит читаемость кода и позволит переиспользовать повторяющиеся условия без дублирования кода.

    // TODO: Класс содержит в себе всю бизнес-логику по подсчёту очков, геймов и сетов.
        // Объект, которым он оперирует (`CurrentMatch`), является "анемичной" моделью —
        // простым контейнером данных практически без собственного поведения. Сервис напрямую читает и записывает его поля и изменяет внутренние объекты.
        // Это главная архитектурная проблема этой части логики. По этим причинам:
        //
        //  - Нарушение инкапсуляции: Данные (в `CurrentMatch`) и поведение (в `MatchScoreCalculationService`) полностью разделены.
            //  Любой другой сервис может так же напрямую изменить счёт матча, и объект `CurrentMatch` не сможет себя защитить.
        //  - Процедурный стиль: Вместо объектно-ориентированного подхода, где объекты сами управляют своим состоянием
            //  (и начисление очков происходит в духе `matchScore.pointWonBy(player)`), получается процедурный код,
            //  который манипулирует внешними структурами данных.
        //  - Жёсткая связанность (Tight Coupling) и низкая связность (Low Cohesion):
            //  Сервис тесно связан с внутренним устройством `CurrentMatch`. При этом логика,
            //  относящаяся к одному понятию (счёт), размазана по разным классам (модели и сервису).
        //  - Сложность тестирования: Чтобы протестировать один конкретный сценарий (например, переход от "ровно" к "преимуществу"),
            //  нужно разбираться во множестве `if` и переходов по методам. Это сложно и хрупко.
        //
        // Как исправить: Провести рефакторинг классов моделей с переходом к "богатой" доменной модели.

    // TODO: Класс имеет высокую когнитивную сложность: чтобы понять, как меняется счёт, нужно проследить всю цепочку вызовов
        // приватных методов внутри одного большого класса, держа в уме множество условий.
        // Перенос логики в доменные модели значительно упростит её понимание, тестирование и поддержку.

    private static final int ZERO = 0; // ZERO не является информативным названием
    private static final int DIFFERENCE_FOR_WIN = 2;
    private static final int SETS_TO_WIN_MATCH = 2;
    private static final int GAMES_TO_WIN_SET = 6;
    private static final int POINTS_TO_WIN_TIE_BREAK = 7;
    private static final int ADVANTAGE_DIFFERENCE = 10; // Это значение не соответствует предметной области
    private static final int DEUCE_SCORE = 40;
    private static final int ADVANTAGE_SCORE = 50; // Это значение не соответствует предметной области

    public void updateMatchState(CurrentMatch currentMatch, int winnerPlayerId) {
        addPointToWinner(currentMatch, winnerPlayerId);

        if (currentMatch.isTieBreak()) {
            checkEndTieBreak(currentMatch);
            return;
        }
        checkEndGame(currentMatch);
    }

    private void addPointToWinner(CurrentMatch currentMatch, int winnerPlayerId) {
        if (currentMatch.getFirstPlayer().getId().equals(winnerPlayerId)) {
            Score nextPoint = currentMatch.getFirstPlayer().getScore().next();
            currentMatch.getFirstPlayer().setScore(nextPoint);
        } else if (currentMatch.getSecondPlayer().getId().equals(winnerPlayerId)) {
            Score nextPoint = currentMatch.getSecondPlayer().getScore().next();
            currentMatch.getSecondPlayer().setScore(nextPoint);
        } else {
            throw new InvalidWinnerIdException(winnerPlayerId);
        }
    }

    private void checkEndGame(CurrentMatch currentMatch) {
        int firstPlayerScores = currentMatch.getFirstPlayer().getScore().getScore();
        int secondPlayerScores = currentMatch.getSecondPlayer().getScore().getScore();

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
        if (firstPlayerScores > DEUCE_SCORE || secondPlayerScores > DEUCE_SCORE) {
            int difference = firstPlayerScores - secondPlayerScores;

            if (difference > ADVANTAGE_DIFFERENCE || difference < -ADVANTAGE_DIFFERENCE) {
                winGame(currentMatch, difference);
                return;
            }

            if (difference == ADVANTAGE_DIFFERENCE) {
                currentMatch.getFirstPlayer().setScore(new Score(ADVANTAGE_SCORE));
                currentMatch.getSecondPlayer().setScore(new Score(DEUCE_SCORE));
            }

            if (difference == ZERO) {
                currentMatch.getFirstPlayer().setScore(new Score(DEUCE_SCORE));
                currentMatch.getSecondPlayer().setScore(new Score(DEUCE_SCORE));
            }

            if (difference == -ADVANTAGE_DIFFERENCE) {
                currentMatch.getFirstPlayer().setScore(new Score(DEUCE_SCORE));
                currentMatch.getSecondPlayer().setScore(new Score(ADVANTAGE_SCORE));
            }
        }
    }

    private void winGame(CurrentMatch currentMatch, int difference) {
        currentMatch.getFirstPlayer().setScore(new Score(ZERO));
        currentMatch.getSecondPlayer().setScore(new Score(ZERO));

        if (difference > ZERO) {
            currentMatch.getFirstPlayer().setGame(currentMatch.getFirstPlayer().getGame().next());
        } else {
            currentMatch.getSecondPlayer().setGame(currentMatch.getSecondPlayer().getGame().next());
        }
        checkEndSet(currentMatch);
    }

    private void checkEndSet(CurrentMatch currentMatch) {
        int firstPlayerGames = currentMatch.getFirstPlayer().getGame().getValue();
        int secondPlayerGames = currentMatch.getSecondPlayer().getGame().getValue();

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
        if (firstPlayerGames >= GAMES_TO_WIN_SET || secondPlayerGames >= GAMES_TO_WIN_SET) {
            int difference = firstPlayerGames - secondPlayerGames;

            // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
            if (difference >= DIFFERENCE_FOR_WIN || difference <= -DIFFERENCE_FOR_WIN) {
                winSet(currentMatch, difference);
                return;
            }

            if (firstPlayerGames == GAMES_TO_WIN_SET && secondPlayerGames == GAMES_TO_WIN_SET) {
                startTieBreak(currentMatch);
            }
        }
    }

    private void startTieBreak(CurrentMatch currentMatch) {
        currentMatch.setTieBreak(true);
        currentMatch.getFirstPlayer().setScore(new TieBreak(ZERO));
        currentMatch.getSecondPlayer().setScore(new TieBreak(ZERO));
    }

    private void checkEndTieBreak(CurrentMatch currentMatch) {
        int firstPlayerScore = currentMatch.getFirstPlayer().getScore().getScore();
        int secondPlayerScore = currentMatch.getSecondPlayer().getScore().getScore();
        int difference = firstPlayerScore - secondPlayerScore;
        if (firstPlayerScore >= POINTS_TO_WIN_TIE_BREAK || secondPlayerScore >= POINTS_TO_WIN_TIE_BREAK) {
            if (difference >= DIFFERENCE_FOR_WIN || difference <= -DIFFERENCE_FOR_WIN) {
                currentMatch.setTieBreak(false);
                winSet(currentMatch, difference);
            }
        }
    }

    // Уже на входе в этот метод нужно держать в уме комбинацию из 4-х условий в разных блоках if.
        // Внутри этого метода используются длинные цепочки вызовов, а также вызывается
        // метод `checkEndMatch()`, который тоже содержит условную логику и вызов ещё одного метода
        // (`determineWinner()` тоже с условной логикой и длинными цепочками вызовов).
        // Это создаёт избыточно большую когнитивную нагрузку для понимания этой логики.
    private void winSet(CurrentMatch currentMatch, int difference) {
        currentMatch.getFirstPlayer().setGame(Game.ZERO);
        currentMatch.getFirstPlayer().setScore(new Score(ZERO));
        currentMatch.getSecondPlayer().setGame(Game.ZERO);
        currentMatch.getSecondPlayer().setScore(new Score(ZERO));

        if (difference > ZERO) {
            currentMatch.getFirstPlayer().setGameSet(currentMatch.getFirstPlayer().getGameSet().next());
        } else {
            currentMatch.getSecondPlayer().setGameSet(currentMatch.getSecondPlayer().getGameSet().next());
        }
        checkEndMatch(currentMatch);
    }

    private void checkEndMatch(CurrentMatch currentMatch) {
        int firstPlayerSetsWin = currentMatch.getFirstPlayer().getGameSet().getValue();
        int secondPlayerSetsWin = currentMatch.getSecondPlayer().getGameSet().getValue();

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
        if (firstPlayerSetsWin == SETS_TO_WIN_MATCH || secondPlayerSetsWin == SETS_TO_WIN_MATCH) {
            determineWinner(currentMatch);
        }
    }

    private void determineWinner(CurrentMatch currentMatch) {
        int firstPlayerSetsWin = currentMatch.getFirstPlayer().getGameSet().getValue();
        int secondPlayerSetsWin = currentMatch.getSecondPlayer().getGameSet().getValue();

        if (firstPlayerSetsWin == SETS_TO_WIN_MATCH) {
            currentMatch.setWinner(new Winner(currentMatch.getFirstPlayer().getId(),
                    currentMatch.getFirstPlayer().getName()));
        } else if (secondPlayerSetsWin == SETS_TO_WIN_MATCH) {
            currentMatch.setWinner(new Winner(currentMatch.getSecondPlayer().getId(),
                    currentMatch.getSecondPlayer().getName()));

        }
    }
}
