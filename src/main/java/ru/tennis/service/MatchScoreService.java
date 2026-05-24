package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public record MatchScoreService(OngoingMatchesService ongoingMatchesService,
                                FinishedMatchesPersistenceService persistenceService,
                                MatchScoreCalculationService calculationService) {

    public CurrentMatch updateCurrentMatch(Integer winnerId, String uuid) {
        Optional<CurrentMatch> mayBeCurrentMatch = ongoingMatchesService.getCurrentMatch(uuid);

        if (mayBeCurrentMatch.isEmpty()) {
            return new CurrentMatch();
        }
        CurrentMatch currentMatch = mayBeCurrentMatch.get();

        calculationService.updateMatchState(currentMatch, winnerId);

        if (currentMatch.endMatch) {
            Session session = HibernateUtil.getSession();
            Transaction transaction = null;
            try (session) {
                transaction = session.beginTransaction();
                persistenceService.saveFinishedMatch(session, currentMatch);
                ongoingMatchesService.deleteMatch(currentMatch.getUuid());
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new SaveFinishedMatchException(e);
            }
        }
        return currentMatch;
    }
}
