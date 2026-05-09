package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.CurrentMatch;
import ru.tennis.dto.MatchScoreDto;
import ru.tennis.model.Match;
import ru.tennis.util.HibernateUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchScoreController {

    public static MatchScoreDto updateMatch(String winnerId, String uuid) {
        List<Match> allFinishedMatches;
        Long totalItems;
        int pageSize = 10;
        MatchScoreDto dto = OngoingMatchesService.getCurrentMatch(uuid);
        CurrentMatch currentMatch = dto.currentMatch();
        MatchScoreCalculationService.updateMatchState(currentMatch, winnerId);
        MatchScoreDto dtoAfterUpdate = OngoingMatchesService.getCurrentMatch(uuid);
        CurrentMatch matchAfterUpdate = dtoAfterUpdate.currentMatch();
        if (matchAfterUpdate == null) {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            try {
                FinishedMatchesPersistenceService.persist(session, currentMatch);
                allFinishedMatches = FinishedMatchesPersistenceService.getAllMatches(session,
                        Optional.empty(), pageSize, 0);
                totalItems = FinishedMatchesPersistenceService.getTotalNumberOfMatches(session, Optional.empty());
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            } finally {
                transaction.commit();
            }
            int pageCount = (int) Math.ceil(totalItems / (double) pageSize);
            return new MatchScoreDto(null, 1, pageCount, allFinishedMatches);
        } else {
            return new MatchScoreDto(currentMatch, 0, 0, Collections.emptyList());
        }
    }
}
