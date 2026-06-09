package ru.tennis.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.WordUtils;

@UtilityClass
public class NameNormalizer {
    private static final String MANY_SPACES = "\\s+";
    private static final String ONE_SPACE = " ";

    public String normalizePlayerName(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return "";
        }
        String trimmedName = playerName.trim();
        trimmedName = trimmedName.replaceAll(MANY_SPACES, ONE_SPACE);
        return WordUtils.capitalizeFully(trimmedName);
    }
}
