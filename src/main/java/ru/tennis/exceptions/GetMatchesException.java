package ru.tennis.exceptions;

public class GetMatchesException extends RuntimeException {
    public GetMatchesException(Exception e) {
        super("Can't get matches", e);
    }
}