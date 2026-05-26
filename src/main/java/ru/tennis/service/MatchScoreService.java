package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.MatchNotFoundException;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public record MatchScoreService(OngoingMatchesService ongoingMatchesService,
                                FinishedMatchesPersistenceService persistenceService,
                                MatchScoreCalculationService calculationService) {

    public CurrentMatch updateCurrentMatch(Integer winnerId, String uuid) {
        Optional<CurrentMatch> mayBeCurrentMatch = ongoingMatchesService.getCurrentMatch(uuid);

        if (mayBeCurrentMatch.isEmpty()) {
            throw new MatchNotFoundException("Current match not found");
        }
        CurrentMatch currentMatch = mayBeCurrentMatch.get();

        calculationService.updateMatchState(currentMatch, winnerId);

        if (currentMatch.hasWinner()) {
            saveFinishedMatch(currentMatch);
            ongoingMatchesService.deleteMatch(currentMatch.getUuid());
        }
        return currentMatch;
    }

    private void saveFinishedMatch(CurrentMatch currentMatch) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();
            persistenceService.saveFinishedMatch(session, currentMatch);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new SaveFinishedMatchException("Can't save match", e);
        }
    }
}
