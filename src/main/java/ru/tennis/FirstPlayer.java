package ru.tennis;

public enum Role {
    ROLE(Score.ZERO, 0, 0);


    private final Score score;
    private final Integer game;
    private final Integer set;

    Role(Score score,  Integer game, Integer set) {
        this.score = Score.ZERO;
        this.game = 0;
        this.set = 0;
    }
}
