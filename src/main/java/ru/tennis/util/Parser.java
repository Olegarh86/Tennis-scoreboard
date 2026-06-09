package ru.tennis.util;

import lombok.experimental.UtilityClass;
import ru.tennis.exception.IncorrectParameterException;

import java.util.UUID;

@UtilityClass
public class Parser {
    public static Integer parseNumber(String winnerId) {
        int id;
        try {
            id = Integer.parseInt(winnerId);
        } catch (NumberFormatException e) {
            throw new IncorrectParameterException(winnerId);
        }
        return id;
    }

    public UUID parseUuid(String matchUuid) {
        if (matchUuid == null || matchUuid.isBlank()) {
            throw new IncorrectParameterException("Missing parameter uuid " + matchUuid);
        }
        try {
            return UUID.fromString(matchUuid);
        } catch (IllegalArgumentException e) {
            throw new IncorrectParameterException("Invalid parameter uuid " + matchUuid);
        }
    }
}
