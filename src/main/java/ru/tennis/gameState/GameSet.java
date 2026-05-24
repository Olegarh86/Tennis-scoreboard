package ru.tennis.gameState;

public enum GameSet {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int points;

    GameSet(int points) {
        this.points = points;
    }

    public GameSet next() {
        return switch (this) {
            case ZERO -> ONE;
            case ONE -> TWO;
            case TWO -> THREE;
            case THREE -> FOUR;
            case FOUR -> FIVE;
            default -> ZERO;
        };
    }

    public String toString() {
        return String.valueOf(points);
    }

    public int getValue() {
        return this.points;
    }
}
