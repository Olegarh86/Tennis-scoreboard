package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.MatchesDto;
import ru.tennis.exceptions.GetMatchesException;
import ru.tennis.model.Match;
import ru.tennis.util.HibernateUtil;
import ru.tennis.util.TennisCalculator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchesController {

    public static MatchesDto getMatchesDto(String playerName, int page) {
        int pageSize = 7;
        int offset = (page - 1) * pageSize;
        List<Match> allMatches;
        Long totalItems;
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            if (playerName.isEmpty()) {
                totalItems = FinishedMatchesPersistenceService.getTotalNumberOfMatches(session, Optional.empty());
                allMatches = FinishedMatchesPersistenceService.getAllMatches(session, Optional.empty(),
                        pageSize, offset);
            } else {
                totalItems = FinishedMatchesPersistenceService.getTotalNumberOfMatches(session, Optional.of(playerName));
                allMatches = FinishedMatchesPersistenceService.getAllMatches(session, Optional.of(playerName), pageSize,
                        offset);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new GetMatchesException(e.getMessage());
        }

        if (totalItems == 0 && allMatches.isEmpty()) {
            return new MatchesDto(playerName, page, 1, Collections.emptyList());
        }

        if (offset >= totalItems) {
            page = page - 1;
            return new MatchesDto(playerName, page, 0, Collections.emptyList());
        }
        int pageCount = TennisCalculator.pageCountCalculate(totalItems, pageSize);

        if (page > pageCount) {
            page = pageCount;
            return new MatchesDto(playerName, page, 0, Collections.emptyList());
        }
        return new MatchesDto(playerName, page, pageCount, allMatches);
    }
}
