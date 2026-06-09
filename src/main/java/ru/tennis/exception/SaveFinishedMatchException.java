package ru.tennis.exception;

public class SaveFinishedMatchException extends RuntimeException {
    public SaveFinishedMatchException(String e) {
        super("Can't save match" + e);
    }
}
