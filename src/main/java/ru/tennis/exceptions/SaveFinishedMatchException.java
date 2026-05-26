package ru.tennis.exceptions;

public class SaveFinishedMatchException extends RuntimeException {
    public SaveFinishedMatchException(String message, Exception e) {
        super(message, e);
    }
}
