package ru.tennis.exception;

public class InvalidWinnerIdException extends RuntimeException {
    public InvalidWinnerIdException(Integer id) {
        super("Invalid winnerName id: " + id);
    }
}
