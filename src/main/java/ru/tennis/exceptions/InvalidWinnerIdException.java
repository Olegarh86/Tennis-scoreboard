package ru.tennis.exceptions;

public class InvalidWinnerIdException extends RuntimeException {
    public InvalidWinnerIdException(Integer id) {
        super("Player not found with id: " + id);
    }
}
