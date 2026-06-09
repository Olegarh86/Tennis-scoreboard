package ru.tennis.service;

import java.util.Optional;
import java.util.UUID;

public interface MatchScoreService {

    Optional<UUID> updateCurrentMatch(Integer winnerId, UUID uuid);
}
