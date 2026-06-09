package ru.tennis.model;

import lombok.Getter;

@Getter
public enum GameScore {
    LOVE,
    FIFTEEN,
    THIRTY,
    FORTY,
    ADVANTAGE;

    GameScore nextPoint() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> ADVANTAGE;
            case ADVANTAGE -> null;
        };
    }

    GameScore prevPoint() {
        return switch (this) {
            case LOVE -> LOVE;
            case FIFTEEN -> FIFTEEN;
            case THIRTY -> THIRTY;
            case FORTY, ADVANTAGE -> FORTY;
        };
    }

    String showPoints() {
        return switch (this) {
            case LOVE -> "0";
            case FIFTEEN -> "15";
            case THIRTY -> "30";
            case FORTY -> "40";
            case ADVANTAGE -> "AD";
        };
    }
}
