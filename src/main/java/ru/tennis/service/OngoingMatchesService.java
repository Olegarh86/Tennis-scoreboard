package ru.tennis.service;

import ru.tennis.dto.CurrentMatch;
import ru.tennis.dto.MatchScoreDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OngoingMatchesService {
    private static final Map<String, CurrentMatch> ONGOING_MATCHES = new HashMap<>();

    public static void addMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.put(currentMatch.getUuid(), currentMatch);
    }

    public static MatchScoreDto getCurrentMatchDto(String uuid) {
        Optional<CurrentMatch> mayBeCurrentMatch = Optional.ofNullable(ONGOING_MATCHES.get(uuid));
        return new MatchScoreDto(mayBeCurrentMatch.orElse(new CurrentMatch()), 0, 0, Collections.emptyList());
    }

    public static void deleteMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.remove(currentMatch.getUuid());
    }
}
