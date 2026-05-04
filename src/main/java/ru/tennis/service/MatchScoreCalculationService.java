package ru.tennis.service;

import ru.tennis.*;


public class MatchScoreCalculationService {

    public static void updateMatchState(CurrentMatch currentMatch, String playerGetPoint) {
        Integer idPlayerGetPoint = Integer.parseInt(playerGetPoint);

        if (currentMatch.firstPlayer.id.equals(idPlayerGetPoint)) {
            Score nextPoint = currentMatch.firstPlayer.score.next();
            currentMatch.firstPlayer.setScore(nextPoint);
        } else {
            Score nextPoint = currentMatch.secondPlayer.score.next();
            currentMatch.secondPlayer.setScore(nextPoint);
        }

        if (currentMatch.tieBreak) {
            playTieBreak(currentMatch);
        } else {
            checkEndGame(currentMatch);

            if (currentMatch.endMatch) {
                OngoingMatchesService.deleteMatch(currentMatch);
            } else {
                OngoingMatchesService.addMatch(currentMatch);
            }
        }
    }

    private static void checkEndGame(CurrentMatch currentMatch) {
        int firstPlayerScores = currentMatch.firstPlayer.score.getScore();
        int secondPlayerScores = currentMatch.secondPlayer.score.getScore();

        if (firstPlayerScores > 40 || secondPlayerScores > 40) {
            int difference = firstPlayerScores - secondPlayerScores;

            if (difference > 10 || difference < -10) {
                winGame(currentMatch, difference);
            }

            if (difference == 10) {
                currentMatch.firstPlayer.setScore(new Score(50));
                currentMatch.secondPlayer.setScore(new Score(40));
            }

            if (difference == 0) {
                currentMatch.firstPlayer.setScore(new Score(40));
                currentMatch.secondPlayer.setScore(new Score(40));
            }

            if (difference == -10) {
                currentMatch.firstPlayer.setScore(new Score(40));
                currentMatch.secondPlayer.setScore(new Score(50));
            }
        }
    }

    private static void winGame(CurrentMatch currentMatch, int difference) {
        currentMatch.firstPlayer.setScore(new Score(0));
        currentMatch.secondPlayer.setScore(new Score(0));

        if (difference > 0) {
            currentMatch.firstPlayer.setGame(currentMatch.firstPlayer.game.next());
        } else {
            currentMatch.secondPlayer.setGame(currentMatch.secondPlayer.game.next());
        }
        checkEndSet(currentMatch);
    }

    private static void checkEndSet(CurrentMatch currentMatch) {
        int firstPlayerGames = currentMatch.firstPlayer.game.ordinal();
        int secondPlayerGames = currentMatch.secondPlayer.game.ordinal();

        if (firstPlayerGames >= 6 || secondPlayerGames >= 6) {
            int difference = firstPlayerGames - secondPlayerGames;

            if (difference >= 2 || difference <= -2) {
                winSet(currentMatch, difference);
            }

            if (difference == 0) {
                currentMatch.tieBreak = true;
                currentMatch.firstPlayer.setScore(new TieBreak(0));
                currentMatch.secondPlayer.setScore(new TieBreak(0));
                playTieBreak(currentMatch);
            }
        }
    }

    private static void playTieBreak(CurrentMatch currentMatch) {
        Integer firstPlayerScore = currentMatch.firstPlayer.score.getScore();
        Integer secondPlayerScore = currentMatch.secondPlayer.score.getScore();
        int difference = firstPlayerScore - secondPlayerScore;
        if (firstPlayerScore >= 7 || secondPlayerScore >= 7) {
            if (difference >= 2 || difference <= -2) {
                currentMatch.tieBreak = false;
                winSet(currentMatch, difference);
            }
        }
    }

    private static void winSet(CurrentMatch currentMatch, int difference) {
        currentMatch.firstPlayer.setGame(Game.ZERO);
        currentMatch.firstPlayer.setScore(new Score(0));
        currentMatch.secondPlayer.setGame(Game.ZERO);
        currentMatch.secondPlayer.setScore(new Score(0));

        if (difference > 0) {
            currentMatch.firstPlayer.setSet(currentMatch.firstPlayer.set.next());
        } else {
            currentMatch.secondPlayer.setSet(currentMatch.secondPlayer.set.next());
        }
        checkEndMatch(currentMatch);
    }

    private static void checkEndMatch(CurrentMatch currentMatch) {
        int firstPlayerSetsWin = currentMatch.firstPlayer.set.ordinal();
        int secondPlayerSetsWin = currentMatch.secondPlayer.set.ordinal();
        if (firstPlayerSetsWin == 5) {
            currentMatch.setWinnerId(currentMatch.firstPlayer.id);
            currentMatch.endMatch = true;
        }

        if (secondPlayerSetsWin == 5) {
            currentMatch.setWinnerId(currentMatch.secondPlayer.id);
            currentMatch.endMatch = true;
        }
    }
}
