package ru.tennis.service;

import ru.tennis.dto.CurrentMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OngoingMatchesService {
    private static final Map<String, CurrentMatch> ONGOING_MATCHES = new HashMap<>();

    public void addMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.put(currentMatch.getUuid(), currentMatch);
    }

    public Optional<CurrentMatch> getCurrentMatch(String uuid) {
        return Optional.ofNullable(ONGOING_MATCHES.get(uuid));
    }

    public void deleteMatch(CurrentMatch currentMatch) {
        ONGOING_MATCHES.remove(currentMatch.getUuid());
    }
}
