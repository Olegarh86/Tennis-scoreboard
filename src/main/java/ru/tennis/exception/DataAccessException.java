package ru.tennis.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String parameter, Exception ex) {
        super("Invalid parameter: " + parameter, ex);
    }
}
