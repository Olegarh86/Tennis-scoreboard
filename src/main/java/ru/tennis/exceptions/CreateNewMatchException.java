package ru.tennis.exceptions;

public class CreateNewMatchException extends RuntimeException {
    public CreateNewMatchException(String message, Exception e) {
        super(message, e);
    }
}
