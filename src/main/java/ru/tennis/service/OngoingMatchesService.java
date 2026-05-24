package ru.tennis.service;

import ru.tennis.dto.CurrentMatch;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private final Map<String, CurrentMatch> ONGOING_MATCHES = new ConcurrentHashMap<>();

    public void addMatch(CurrentMatch currentMatch) {
        if (currentMatch != null) {
            ONGOING_MATCHES.put(currentMatch.getUuid(), currentMatch);
        }
    }

    public Optional<CurrentMatch> getCurrentMatch(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(ONGOING_MATCHES.get(uuid));
    }

    public void deleteMatch(String uuid) {
        ONGOING_MATCHES.remove(uuid);
    }
}
