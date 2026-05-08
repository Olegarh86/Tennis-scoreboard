package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.MatchesDto;
import ru.tennis.model.Match;
import ru.tennis.util.HibernateUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchesController {
    static int page = 1;
    static int pageSize = 7;

    public static MatchesDto getMatches(String playerName, String pageNumber) {

        if (!pageNumber.isEmpty()) {
            page = Integer.parseInt(pageNumber);
        }

        if (page <= 0) {
            return new MatchesDto(playerName, page, 0, Collections.emptyList());
        }

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
            throw e;
        }

        if (totalItems < 1 && page > 1) {
            return new MatchesDto(playerName, page - 1, 0, Collections.emptyList());
        }
        int pageCount = (int) Math.ceil(totalItems / (double) pageSize);

        if (pageCount < 1) {
            return new MatchesDto(playerName, page, 1, Collections.emptyList());
        }

        if (page > pageCount) {
            page = pageCount;
            return new MatchesDto(playerName, page, 0, Collections.emptyList());
        }
        return new MatchesDto(playerName, page, pageCount, allMatches);
    }
}
