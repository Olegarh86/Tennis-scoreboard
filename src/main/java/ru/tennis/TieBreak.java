package ru.tennis;

public class TieBreak extends Score{
    public TieBreak(Integer score) {
        super(score);
    }

    public Score next() {
        return new TieBreak(this.getScore() + 1);
    }
}
