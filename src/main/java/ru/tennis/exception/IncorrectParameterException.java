package ru.tennis.exception;

public class IncorrectParameterException extends RuntimeException {
    public IncorrectParameterException(String parameter) {
        super("Invalid parameter: " + parameter);
    }
}
