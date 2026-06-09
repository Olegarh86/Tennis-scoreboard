package ru.tennis.exception;

public class CreateNewMatchException extends RuntimeException {
    public CreateNewMatchException(Exception e) {
        super("Can't create new match", e);
    }
}
