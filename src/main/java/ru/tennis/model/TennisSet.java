package ru.tennis.model;

import lombok.Getter;

public class TennisSet {
    @Getter
    private int points;

    protected TennisSet() {
        this.points = 0;
    }

    protected void nextPoint() {
        this.points++;
    }

    protected void resetPoints() {
        this.points = 0;
    }

    public String showPoints() {
        return String.valueOf(this.points);
    }
}

