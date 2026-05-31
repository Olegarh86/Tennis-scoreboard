package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.MatchesDto;
import ru.tennis.exceptions.GetMatchesException;
import ru.tennis.model.Match;
import ru.tennis.util.HibernateUtil;
import ru.tennis.util.TennisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MatchesService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс вручную управляет сессиями и транзакциями
        // (см. файл "service.md" в этом же пакете)

    // TODO: В блоке `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.
        // (см. файл "service.md" в этом же пакете)

    private final FinishedMatchesPersistenceService persistenceService;

    // Можно использовать @RequiredArgsConstructor
    public MatchesService(FinishedMatchesPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    // TODO: Этот метод работает с `org.hibernate.Session`.
        // Это "протечка" детали реализации в нижележащего уровня в сервисный слой.
        // Это класс не должен знать о существовании сессий Hibernate и работать с ними.
    // Редирект на ближайшую корректную страницу — это хороший и дружелюбный для пользователя подход.
        // Но стоит придумать как реализовать логику редиректа на корректную страницу без создания пустого DTO
        // и флага needsRedirect. Описал один из вариантов (см. файл "service.md" в этом же пакете)
    public MatchesDto getMatchesDto(String playerName, int page) {
        int pageSize = TennisUtil.getPageSize();

        if (page < 1) {
            return new MatchesDto(playerName, 1, 0, Collections.emptyList(), true);
        }
        int offset = TennisUtil.offsetCalculate(page);
        List<Match> allMatches;
        Long totalItems;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();

            if (playerName.isEmpty()) {
                totalItems = persistenceService.getTotalNumberOfMatches(session, Optional.empty());
                allMatches = persistenceService.getAllMatches(session, Optional.empty(), pageSize, offset);
            } else {
                totalItems = persistenceService.getTotalNumberOfMatches(session, Optional.of(playerName));
                allMatches = persistenceService.getAllMatches(session, Optional.of(playerName), pageSize, offset);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new GetMatchesException(e);
        }

        if (totalItems == 0 && allMatches.isEmpty()) {
            return new MatchesDto(playerName, page, 0, Collections.emptyList(), false);
        }

        int pageCount = TennisUtil.pageCountCalculate(totalItems, pageSize);

        if (page > pageCount) {
            page = pageCount;
            return new MatchesDto(playerName, page, 0, Collections.emptyList(), true);
        }
        return new MatchesDto(playerName, page, pageCount, allMatches, false);
    }
}
