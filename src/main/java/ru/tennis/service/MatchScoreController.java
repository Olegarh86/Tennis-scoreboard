package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.CurrentMatch;
import ru.tennis.dto.MatchScoreDto;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.model.Match;
import ru.tennis.util.HibernateUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchScoreController {

    public static MatchScoreDto updateMatch(String winnerId, String uuid) {
        List<Match> allFinishedMatches;
        Long totalItems;
        int pageSize = 7;
        MatchScoreDto dto = OngoingMatchesService.getCurrentMatchDto(uuid);
        CurrentMatch currentMatch = dto.currentMatch();
        MatchScoreCalculationService.updateMatchState(currentMatch, winnerId);
        MatchScoreDto dtoAfterUpdate = OngoingMatchesService.getCurrentMatchDto(uuid);
        CurrentMatch matchAfterUpdate = dtoAfterUpdate.currentMatch();

        if (uuid.equals(matchAfterUpdate.uuid)) {
            return new MatchScoreDto(currentMatch, 0, 0, Collections.emptyList());
        } else {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            try (session) {
                FinishedMatchesPersistenceService.persist(session, currentMatch);
                allFinishedMatches = FinishedMatchesPersistenceService.getAllMatches(session,
                        Optional.empty(), pageSize, 0);
                totalItems = FinishedMatchesPersistenceService.getTotalNumberOfMatches(session, Optional.empty());
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new SaveFinishedMatchException(e.getMessage());
            }
            int pageCount = (int) Math.ceil(totalItems / (double) pageSize);
            return new MatchScoreDto(matchAfterUpdate, 1, pageCount, allFinishedMatches);
        }
    }
}
