package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public class MatchScoreController {

    public Optional<CurrentMatch> updateCurrentMatch(OngoingMatchesService ongoingMatchesService, FinishedMatchesPersistenceService service, String winnerId,
                                                     String uuid) {
        Optional<CurrentMatch> mayBeCurrentMatch = ongoingMatchesService.getCurrentMatch(uuid);

        if (mayBeCurrentMatch.isEmpty()) {
            return Optional.empty();
        }
        CurrentMatch matchBeforeUpdate = mayBeCurrentMatch.get();

        Optional<CurrentMatch> currentMatchAfterUpdate =
                MatchScoreCalculationService.updateMatchState(ongoingMatchesService, matchBeforeUpdate, winnerId);

        if (currentMatchAfterUpdate.isPresent()) {
            return currentMatchAfterUpdate;
        } else {
            Session session = HibernateUtil.getSession();
            Transaction transaction = null;
            try (session) {
                transaction = session.beginTransaction();
                service.saveFinishedMatch(session, matchBeforeUpdate);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new SaveFinishedMatchException("Can't save match", e);//
            }
            return Optional.empty();
        }
    }
}
