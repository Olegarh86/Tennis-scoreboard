package ru.tennis.service;

import ru.tennis.CurrentMatch;

import java.util.HashMap;
import java.util.Map;

public class OngoingMatchesService {
    private static final Map<String, CurrentMatch> ONGOING_MATCHES = new HashMap<>();

    public static void addMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.put(currentMatch.getUuid(), currentMatch);
    }

    public static CurrentMatch getCurrentMatch(String uuid) {
        return ONGOING_MATCHES.get(uuid);
    }

    public static void deleteMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.remove(currentMatch.getUuid());
    }
}
