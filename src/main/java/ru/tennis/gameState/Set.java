package ru.tennis.gameState;

public enum Set {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer points;

    Set(Integer points) {
        this.points = points;
    }

    public Set next() {
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
        return this.points.toString();
    }
}
