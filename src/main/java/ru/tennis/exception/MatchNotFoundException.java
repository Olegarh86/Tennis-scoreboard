package ru.tennis.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String message) {
        super("Match not found with id: " + message);
    }
}
