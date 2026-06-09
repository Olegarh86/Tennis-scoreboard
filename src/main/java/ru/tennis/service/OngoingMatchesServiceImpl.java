package ru.tennis.service;

import lombok.RequiredArgsConstructor;
import ru.tennis.dto.CurrentMatchDto;
import ru.tennis.exception.MatchNotFoundException;
import ru.tennis.model.CurrentMatch;
import ru.tennis.model.TennisPlayer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class OngoingMatchesServiceImpl implements OngoingMatchesService {
    private final FinishedMatchesPersistenceService persistenceService;
    private final Map<UUID, CurrentMatch> ONGOING_MATCHES = new ConcurrentHashMap<>();

    @Override
    public UUID addMatch(CurrentMatch currentMatch) {
        UUID uuid = UUID.randomUUID();
        if (currentMatch != null) {
            ONGOING_MATCHES.put(uuid, currentMatch);
            currentMatch.setUuid(uuid);
        }
        return uuid;
    }

    @Override
    public Optional<CurrentMatch> getCurrentMatch(UUID uuid) {
        return Optional.ofNullable(ONGOING_MATCHES.get(uuid));
    }

    @Override
    public CurrentMatchDto getCurrentMatchDto(UUID uuid) {
        Optional<CurrentMatch> currentMatchOptional = getCurrentMatch(uuid);

        if (currentMatchOptional.isEmpty()) {
            throw new MatchNotFoundException(uuid.toString());
        }
        CurrentMatch currentMatch = currentMatchOptional.get();
        String firstPlayerName = currentMatch.getFirstPlayer().getName();
        String secondPlayerName = currentMatch.getSecondPlayer().getName();
        String firstPlayerMatch = currentMatch.getFirstPlayer().getTennisMatch().showPoints();
        String secondPlayerMatch = currentMatch.getSecondPlayer().getTennisMatch().showPoints();
        String firstPlayerSet = currentMatch.getFirstPlayer().getTennisSet().showPoints();
        String secondPlayerSet = currentMatch.getSecondPlayer().getTennisSet().showPoints();
        String firstPlayerPoints = currentMatch.showFirstPlayerScore();
        String secondPlayerPoints = currentMatch.showSecondPlayerScore();
        return new CurrentMatchDto(firstPlayerName, secondPlayerName, firstPlayerMatch, secondPlayerMatch,
                firstPlayerSet, secondPlayerSet, firstPlayerPoints, secondPlayerPoints);
    }

    @Override
    public void deleteMatch(UUID uuid) {
        ONGOING_MATCHES.remove(uuid);
    }

    @Override
    public UUID createNewCurrentMatch(String playerName1, String playerName2) {
        persistenceService.createNewPlayer(playerName1);
        persistenceService.createNewPlayer(playerName2);
        CurrentMatch currentMatch = new CurrentMatch(new TennisPlayer(1, playerName1), new TennisPlayer(2,
                playerName2));
        return addMatch(currentMatch);
    }
}
