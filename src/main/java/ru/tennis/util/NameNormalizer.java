package ru.tennis.util;

import org.apache.commons.text.WordUtils;

public class NameNormalizer {

    // Можно сделать класс утилитным, например, с помощью @UtilityClass

    private static final String MANY_SPACES = "\\s+";
    private static final String ONE_SPACE = " ";

    public String normalizePlayerName(String playerName) {
        if (playerName == null) {
            return "";
        }

        // Точнее назвать trimmedName
        String trimName = playerName.trim();
        trimName = trimName.replaceAll(MANY_SPACES, ONE_SPACE);
        return WordUtils.capitalizeFully(trimName);
    }
}
