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

    // TODO: Класс сервиса не должен быть record. Record в Java были введены как неизменяемые носители данных.
        // Использование их для компонентов с поведением (сервисов) не идиоматично и сбивает с толку любого, кто будет читать этот код.

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс вручную управляет сессиями и транзакциями
        // (см. файл "service.md" в этом же пакете)

    // TODO: В блоке `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.
        // (см. файл "service.md" в этом же пакете)


    // Лучше переименовать параметр и изменить его тип String uuid —> UUID matchId
    // TODO: Race condition при обработке выигранного очка.
        // Если пользователь очень быстро нажмёт кнопку выигрыша очка, браузер отправит два POST-запроса почти одновременно.
        // Tomcat обработает эти два запроса в двух разных потоках, но так как оба потока будут работать с одним и тем же общим объектом `CurrentMatch`,
        // будет возникать ситуация, когда счёт изменится только один раз.
        // Чтобы это исправить, нужно гарантировать, что только один поток может изменять состояние конкретного матча в один момент времени.
    public CurrentMatch updateCurrentMatch(Integer winnerId, String uuid) {

        // Более понятным было бы название currentMatchOptional
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

    // Этот метод более уместно разместить в FinishedMatchesPersistenceService
    // TODO: Этот метод работает с `org.hibernate.Session`.
        // Это "протечка" детали реализации в нижележащего уровня в сервисный слой.
        // Это класс не должен знать о существовании сессий Hibernate и работать с ними.
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
