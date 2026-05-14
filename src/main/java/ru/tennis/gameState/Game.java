package ru.tennis.gameState;

public enum Game {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);

    private final Integer points;

    Game(Integer points) {
        this.points = points;
    }

    public Game next() {
        return switch (this) {
            case ZERO -> ONE;
            case ONE -> TWO;
            case TWO -> THREE;
            case THREE -> FOUR;
            case FOUR -> FIVE;
            case FIVE -> SIX;
            case SIX -> SEVEN;
            default -> ZERO;
        };
    }

    public String toString(){
        return this.points.toString();
    }
}
