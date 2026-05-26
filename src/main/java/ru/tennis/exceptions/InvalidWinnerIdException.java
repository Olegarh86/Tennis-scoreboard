package ru.tennis.exceptions;

public class InvalidWinnerIdException extends RuntimeException {
    public InvalidWinnerIdException(Integer id) {
        super("Invalid winner id: " + id);
    }
}
