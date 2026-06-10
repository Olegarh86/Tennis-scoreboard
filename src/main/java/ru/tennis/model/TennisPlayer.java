package ru.tennis.model;

import lombok.Getter;

@Getter
public class TennisPlayer {
    private final int id;
    private final String name;
    private final TennisSet tennisSet;
    private final TennisMatch tennisMatch;
    private GameScore gameScore;
    private int tieBreakPoints;

    public TennisPlayer(int id, String name) {
        this.id = id;
        this.name = name;
        this.gameScore = GameScore.LOVE;
        this.tennisSet = new TennisSet();
        this.tennisMatch = new TennisMatch();
        this.tieBreakPoints = 0;
    }

    protected void winPoint() {
        this.gameScore = gameScore.nextPoint();
    }

    protected void losePoint() {
        this.gameScore = gameScore.prevPoint();
    }

    protected void winSet() {
        this.gameScore = GameScore.LOVE;
        tennisSet.nextPoint();
    }

    protected void loseSet() {
        this.gameScore = GameScore.LOVE;
    }

    protected void winMatch() {
        this.gameScore = GameScore.LOVE;
        tennisSet.resetPoints();
        tennisMatch.nextPoint();
    }

    protected void loseMatch() {
        this.gameScore = GameScore.LOVE;
        tennisSet.resetPoints();
    }

    protected void winTieBreakPoint() {
        this.tieBreakPoints++;
    }

    protected void doTie() {
        this.gameScore = GameScore.FORTY;
    }
}
