package ru.tennis.validation;

import ru.tennis.exception.IncorrectParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Validator {
    private static final String ACCEPTABLE_SYMBOLS = "[A-Za-z'. -]+";
    private static final String LETTERS = ".*[A-Za-z].*";
    private static final String ERROR_IMPOSSIBLE_SYMBOL = "The player's name must contain only Latin letters, spaces, hyphens or apostrophes";
    private static final String ERROR_NAMES_NOT_DIFFERENT = "Player can't play with yourself!";
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 30;
    private static final String ERROR_LENGTH_NAME = String.format("Player name length must be between %d and %d " +
                                                                  "characters", MIN_LENGTH, MAX_LENGTH);
    private static final String ERROR_EMPTY_NAME = "Player name is empty";
    private static final String ERROR_NAME_WITHOUT_LETTERS = "Player name must contain Latin letters";
    private static final String EMPTY_NAME = "";

    public List<String> validatePlayerNames(String name1, String name2) {
        List<String> errors = new ArrayList<>();

        Optional<String> optionalEmptyName = checkEmptyName(name1, name2);
        optionalEmptyName.ifPresent(errors::add);

        Optional<String> optionalDifferentStrings = checkDifferentStrings(name1, name2);
        optionalDifferentStrings.ifPresent(errors::add);

        Optional<String> optionalLengthName1 = checkLengthString(name1);
        optionalLengthName1.ifPresent(errors::add);
        Optional<String> optionalLengthName2 = checkLengthString(name2);
        optionalLengthName2.ifPresent(errors::add);

        Optional<String> optionalAllowedCharacters1 = checkAllowedCharacters(name1);
        optionalAllowedCharacters1.ifPresent(errors::add);
        Optional<String> optionalAllowedCharacters2 = checkAllowedCharacters(name2);
        Optional<String> haveLetters1 = checkNameHaveLetters(name1);
        haveLetters1.ifPresent(errors::add);
        Optional<String> haveLetters2 = checkNameHaveLetters(name2);
        haveLetters2.ifPresent(errors::add);
        optionalAllowedCharacters2.ifPresent(errors::add);
        return errors;
    }

    public void validateParameter(String value) {
        if (value == null || value.isBlank()) {
            throw new IncorrectParameterException("Missing parameter " + value);
        }
    }

    private Optional<String> checkEmptyName(String name1, String name2) {
        if (EMPTY_NAME.equals(name1) || EMPTY_NAME.equals(name2)) {
            return Optional.of(ERROR_EMPTY_NAME);
        }
        return Optional.empty();
    }

    private Optional<String> checkAllowedCharacters(String playerName) {
        if (!playerName.matches(ACCEPTABLE_SYMBOLS)) {
            return Optional.of(ERROR_IMPOSSIBLE_SYMBOL);
        }
        return Optional.empty();
    }

    private Optional<String> checkDifferentStrings(String playerName1, String playerName2) {
        if (playerName1.equalsIgnoreCase(playerName2)) {
            return Optional.of(ERROR_NAMES_NOT_DIFFERENT);
        }
        return Optional.empty();
    }

    private Optional<String> checkLengthString(String playerName) {
        if (playerName.length() < MIN_LENGTH || playerName.length() > MAX_LENGTH) {
            return Optional.of(ERROR_LENGTH_NAME);
        }
        return Optional.empty();
    }

    private Optional<String> checkNameHaveLetters(String playerName) {
        if (!playerName.matches(LETTERS)) {
            return Optional.of(ERROR_NAME_WITHOUT_LETTERS);
        }
        return Optional.empty();
    }
}
