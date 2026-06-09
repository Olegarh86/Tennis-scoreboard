package ru.tennis.service;

import ru.tennis.dto.CurrentMatchDto;
import ru.tennis.model.CurrentMatch;

import java.util.Optional;
import java.util.UUID;

public interface OngoingMatchesService {
    UUID addMatch(CurrentMatch currentMatch);

    Optional<CurrentMatch> getCurrentMatch(UUID uuid);

    CurrentMatchDto getCurrentMatchDto(UUID uuid);

    void deleteMatch(UUID uuid);

    UUID createNewCurrentMatch(String playerName1, String playerName2);
}
