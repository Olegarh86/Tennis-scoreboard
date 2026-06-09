package ru.tennis.model;

import lombok.Getter;

public class TennisMatch {
    @Getter
    private int points;

    protected TennisMatch() {
        this.points = 0;
    }

    protected void nextPoint() {
        this.points++;
    }

    public String showPoints() {
        return String.valueOf(points);
    }
}
