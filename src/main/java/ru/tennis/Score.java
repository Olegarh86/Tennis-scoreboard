package ru.tennis;

import lombok.Getter;

@Getter
public class Score {
    private Integer score;

    public Score(Integer score) {
        this.score = score;
    }

    public Score next() {
        switch (score) {
            case 0:
                score = 15;
                break;
            case 15:
                score = 30;
                break;
            case 30:
                score = 40;
                break;
            case 40:
                score = 50;
                break;
            case 50:
                score = 51;
                break;
            default:
                score = -1;
        }
        return this;
    }

    @Override
    public String toString() {
        if (score == 50) {
            return "AD";
        }
        return String.valueOf(score);
    }
}
