package ru.tennis.service;

import ru.tennis.CurrentMatch;

import java.util.HashMap;
import java.util.Map;

public class OngoingMatchesService {
    private static final Map<String, CurrentMatch> currentMatches = new HashMap<>();

    public static void addMatch(CurrentMatch currentMatch) {
        currentMatches.put(currentMatch.getUuid(), currentMatch);
    }

    public static CurrentMatch getCurrentMatch(String uuid) {
        return currentMatches.get(uuid);
    }
}
