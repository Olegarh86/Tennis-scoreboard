package ru.tennis.service;

import ru.tennis.CurrentMatch;
import ru.tennis.dto.MatchScoreDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OngoingMatchesService {
    private static final Map<String, CurrentMatch> ONGOING_MATCHES = new HashMap<>();

    public static void addMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.put(currentMatch.getUuid(), currentMatch);
    }

    public static MatchScoreDto getCurrentMatch(String uuid) {
        CurrentMatch currentMatch = ONGOING_MATCHES.get(uuid);
        return new MatchScoreDto(currentMatch, 0, 0, Collections.emptyList());
    }

    public static void deleteMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.remove(currentMatch.getUuid());
    }
}
