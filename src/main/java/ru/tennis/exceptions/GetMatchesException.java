package ru.tennis.exceptions;

public class GetMatchesException extends RuntimeException {
    public GetMatchesException(String message, Exception e) {
        super(message, e);
    }
}