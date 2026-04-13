package ru.tennis.service;

import ru.tennis.CurrentMatch;

public class MatchScoreCalculationService {

    public static void updateScore(CurrentMatch currentMatch, String idPlayerGetPoint) {
        currentMatch.score.put(idPlayerGetPoint, currentMatch.score.get(idPlayerGetPoint) + 15);
        OngoingMatchesService.addMatch(currentMatch);
        System.out.println();
    }
}
