package ru.tennis.validation;

import org.apache.commons.text.WordUtils;
import java.util.ArrayList;
import java.util.List;

public class PlayerNamesValidator {
    private static final String ACCEPTABLE_SYMBOLS = "[A-Za-z' -]+";
    private static final String MANY_SPACES = "\\s+";
    private static final String ONE_SPACE = " ";
    private static final String ERROR_IMPOSSIBLE_SYMBOL = "The player's name must contain only Latin letters, spaces, hyphens or apostrophes";
    private static final String ERROR_NAMES_NOT_DIFFERENT = "Player can't play with yourself!";
    private static final String ERROR_LENGTH_NAME = "Player name length must be between 2 and 30 characters";
    private static final String ERROR_EMPTY_NAME = "Player name is empty";
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 30;

    public ValidationResult validate(String playerName1, String playerName2) {
        List<String> errors = new ArrayList<>();

        if (playerName1 == null || playerName1.isBlank() || playerName2 == null || playerName2.isBlank()) {
            errors.add(ERROR_EMPTY_NAME);
            return new ValidationResult(errors, playerName1, playerName2);
        }

        String normalizedName1 = normalizedPlayerName(playerName1);
        String normalizedName2 = normalizedPlayerName(playerName2);

        checkLengthString(normalizedName1, errors);
        checkLengthString(normalizedName2, errors);

        checkDifferentStrings(normalizedName1, normalizedName2, errors);

        checkAllowedCharacters(normalizedName1, errors);
        checkAllowedCharacters(normalizedName2, errors);
        return new ValidationResult(errors, normalizedName1, normalizedName2);
    }

    public String normalizedPlayerName(String playerName) {
        String trimName = playerName.trim();
        trimName = trimName.replaceAll(MANY_SPACES, ONE_SPACE);
        return WordUtils.capitalizeFully(trimName);
    }

    private void checkAllowedCharacters(String playerName, List<String> errors) {
        if (!playerName.matches(ACCEPTABLE_SYMBOLS)) {
            errors.add(ERROR_IMPOSSIBLE_SYMBOL);
        }
    }

    private void checkDifferentStrings(String playerName1, String playerName2, List<String> errors) {
        if (playerName1.equalsIgnoreCase(playerName2)) {
            errors.add(ERROR_NAMES_NOT_DIFFERENT);
        }
    }

    private void checkLengthString(String playerName, List<String> errors) {
        if (playerName.length() < MIN_LENGTH || playerName.length() > MAX_LENGTH) {
            errors.add(ERROR_LENGTH_NAME);
        }
    }
}
