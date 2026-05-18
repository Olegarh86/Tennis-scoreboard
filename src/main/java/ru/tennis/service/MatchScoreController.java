package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.dto.MatchScoreDto;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.model.Match;
import ru.tennis.util.HibernateUtil;
import ru.tennis.util.TennisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchScoreController {

    public static MatchScoreDto updateCurrentMatch(String winnerId, String uuid) {
        List<Match> allFinishedMatches;
        Long totalItems;
        int pageSize = TennisUtil.getPageSize();

        MatchScoreDto matchScoreDtoBeforeUpdate = OngoingMatchesService.getCurrentMatchDto(uuid);
        CurrentMatch matchBeforeUpdate = matchScoreDtoBeforeUpdate.currentMatch();

        MatchScoreDto dtoAfterUpdate = MatchScoreCalculationService.updateMatchState(matchBeforeUpdate, winnerId);
        CurrentMatch matchAfterUpdate = dtoAfterUpdate.currentMatch();

        if (uuid.equals(matchAfterUpdate.uuid)) {
            return new MatchScoreDto(matchBeforeUpdate, 0, 0, Collections.emptyList());
        } else {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            try (session) {
                FinishedMatchesPersistenceService service = new FinishedMatchesPersistenceService(new TennisDaoImpl());
                service.saveFinishedMatch(session, matchBeforeUpdate);
                allFinishedMatches = service.getAllMatches(session, Optional.empty(), pageSize, 0);
                totalItems = service.getTotalNumberOfMatches(session, Optional.empty());
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new SaveFinishedMatchException(e.getMessage());
            }
            int pageCount = TennisUtil.pageCountCalculate(totalItems, pageSize);
            return new MatchScoreDto(matchAfterUpdate, 1, pageCount, allFinishedMatches);
        }
    }
}
