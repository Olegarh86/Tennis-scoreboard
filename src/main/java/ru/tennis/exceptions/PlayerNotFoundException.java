package ru.tennis.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    private static final String message = "Player not found in data base";

    public PlayerNotFoundException() {
        super(message);
    }
}
