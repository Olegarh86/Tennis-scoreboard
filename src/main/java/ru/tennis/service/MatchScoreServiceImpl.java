package ru.tennis.service;

import lombok.RequiredArgsConstructor;
import ru.tennis.exception.MatchNotFoundException;
import ru.tennis.model.CurrentMatch;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class MatchScoreServiceImpl implements MatchScoreService {
    private final OngoingMatchesService ongoingMatchesService;
    private final FinishedMatchesPersistenceService persistenceService;

    @Override
    public Optional<UUID> updateCurrentMatch(Integer winnerId, UUID uuid) {

        Optional<CurrentMatch> currentMatchOptional = ongoingMatchesService.getCurrentMatch(uuid);

        if (currentMatchOptional.isEmpty()) {
            throw new MatchNotFoundException(uuid.toString());
        }
        CurrentMatch currentMatch = currentMatchOptional.get();
        synchronized (currentMatch) {
            currentMatch.updateMatchState(winnerId);

            if (currentMatch.hasWinner()) {
                persistenceService.saveFinishedMatch(currentMatch);
                ongoingMatchesService.deleteMatch(currentMatch.getUuid());
                return Optional.empty();
            }
            return Optional.of(currentMatch.getUuid());
        }
    }
}
