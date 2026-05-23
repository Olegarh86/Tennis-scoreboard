package ru.tennis.exceptions;

public class SaveFinishedMatchException extends RuntimeException {
    public SaveFinishedMatchException(Exception e) {
        super("Can't save match", e);
    }
}
