package ru.tennis.gameState;

import lombok.Getter;

@Getter
public class Score {
    private final int score;

    public Score(int score) {
        this.score = score;
    }

    public Score next() {
        return switch (score) {
            case 0 -> new Score(15);
            case 15 -> new Score(30);
            case 30 -> new Score(40);
            case 40 -> new Score(50);
            case 50 -> new Score(51);
            default -> new Score(-1);
        };
    }

    public int getValue() {
        return score;
    }

    @Override
    public String toString() {
        if (score == 50) {
            return "AD";
        }
        return String.valueOf(score);
    }
}
