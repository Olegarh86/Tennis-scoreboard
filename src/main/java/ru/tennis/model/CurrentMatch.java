package ru.tennis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.tennis.exception.InvalidWinnerIdException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CurrentMatch {
    private static final String WINNER = "winner";
    private static final String LOSER = "loser";
    @Setter
    private UUID uuid;
    private TennisPlayer firstPlayer;
    private TennisPlayer secondPlayer;
    private TennisPlayer winner;
    private boolean tieBreak;

    public CurrentMatch(TennisPlayer firstPlayer, TennisPlayer secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.winner = null;
        this.tieBreak = false;
    }

    public boolean hasWinner() {
        return winner != null;
    }

    public void updateMatchState(int winnerId) {
        Map<String, TennisPlayer> winnerAndLoser = getWinnerAndLoser(winnerId);
        TennisPlayer winner = winnerAndLoser.get(WINNER);
        TennisPlayer loser = winnerAndLoser.get(LOSER);

        if (tieBreak) {
            winner.winTieBreakPoint();
            checkEndTieBreak(winner, loser);
        } else {
            if (winner.getGameScore().equals(GameScore.ADVANTAGE)) {
                winGame(winner, loser);
                return;
            }
            if (winner.getGameScore().equals(GameScore.FORTY) && loser.getGameScore().equals(GameScore.ADVANTAGE)) {
                winner.doTie();
                loser.doTie();
                return;
            }
            winner.winPoint();
            loser.losePoint();

            if (winner.getGameScore().equals(GameScore.ADVANTAGE) && !loser.getGameScore().equals(GameScore.FORTY)) {
                winGame(winner, loser);
            }
        }
    }

    private void winGame(TennisPlayer winner, TennisPlayer loser) {
        winner.winSet();
        loser.loseSet();
        checkEndSetOrStartTieBreak(winner, loser);
    }

    private void checkEndTieBreak(TennisPlayer winner, TennisPlayer loser) {
        int difference = winner.getTieBreakPoints() - loser.getTieBreakPoints();
        if (difference > 1 || difference < -1) {
            endTieBreak();
            winner.winMatch();
            loser.loseMatch();
            checkEndMatch(winner);
        }
    }

    private void checkEndSetOrStartTieBreak(TennisPlayer winner, TennisPlayer loser) {
        int difference = winner.getTennisSet().getPoints() - loser.getTennisSet().getPoints();

        if (winner.getTennisSet().getPoints() > 5 && (difference > 1 || difference < -1)) {
            winner.winMatch();
            loser.loseMatch();
            checkEndMatch(winner);
        } else if (winner.getTennisSet().getPoints() == 6 && difference == 0) {
            startTieBreak();
        }
    }

    private void checkEndMatch(TennisPlayer winner) {
        if (winner.getTennisMatch().getPoints() == 2) {
            this.winner = winner;
        }
    }

    private Map<String, TennisPlayer> getWinnerAndLoser(int winnerId) {
        Map<String, TennisPlayer> winnerAndLoser = new HashMap<>();
        if (winnerId == firstPlayer.getId()) {
            winnerAndLoser.put(WINNER, firstPlayer);
            winnerAndLoser.put(LOSER, secondPlayer);
            return winnerAndLoser;
        } else if (winnerId == secondPlayer.getId()) {
            winnerAndLoser.put(LOSER, firstPlayer);
            winnerAndLoser.put(WINNER, secondPlayer);
            return winnerAndLoser;
        }
        throw new InvalidWinnerIdException(winnerId);
    }

    private void startTieBreak() {
        this.tieBreak = true;
    }

    private void endTieBreak() {
        this.tieBreak = false;
    }

    public String showFirstPlayerScore() {
        if (tieBreak) {
            return String.valueOf(firstPlayer.getTieBreakPoints());
        }
        return firstPlayer.getGameScore().showPoints();
    }

    public String showSecondPlayerScore() {
        if (tieBreak) {
            return String.valueOf(secondPlayer.getTieBreakPoints());
        }
        return secondPlayer.getGameScore().showPoints();
    }
}
