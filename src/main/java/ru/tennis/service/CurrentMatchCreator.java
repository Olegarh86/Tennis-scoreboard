package ru.tennis.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.CreateNewMatchException;
import ru.tennis.model.Player;
import ru.tennis.util.HibernateUtil;

import java.util.Optional;

public class CurrentMatchCreator {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс отвечает за создание объекта текущего матча (доменной модели).
        // При этом он способствует смешению слоёв — работает с JPA Entity и передаёт их в доменную модель.
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
        // Этому классу должна быть не нужна зависимость FinishedMatchesPersistenceService.

    // TODO: Класс вручную управляет сессиями и транзакциями
        // (см. файл "service.md" в этом же пакете)

    // TODO: В блоке `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.
        // (см. файл "service.md" в этом же пакете)

    private final FinishedMatchesPersistenceService persistenceService;
    private final OngoingMatchesService ongoingMatchesService;

    // Можно использовать @RequiredArgsConstructor
    public CurrentMatchCreator(FinishedMatchesPersistenceService persistenceService, OngoingMatchesService ongoingMatchesService) {
        this.persistenceService = persistenceService;
        this.ongoingMatchesService = ongoingMatchesService;
    }

    // Лучше возвращать из этого метода UUID, а клиентский код сам преобразует его в строковое представление, если понадобится.
    public String createNewCurrentMatch(String playerName1, String playerName2) {
        CurrentMatch currentMatch;
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try (session) {
            transaction = session.beginTransaction();

            // Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
//            DataBaseUtil.addNFinishedMatchesWithRandomPlayers(persistenceService, session, 21);
            Player player1 = getOrCreatePlayer(playerName1, session);
            Player player2 = getOrCreatePlayer(playerName2, session);
            currentMatch = new CurrentMatch(player1, player2);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new CreateNewMatchException(e);
        }
        ongoingMatchesService.addMatch(currentMatch);
        return currentMatch.getUuid();
    }

    private Player getOrCreatePlayer(String playerName, Session session) {
        Optional<Player> mayBePlayer = persistenceService.getPlayerByName(session, playerName);
        return mayBePlayer.orElseGet(() -> persistenceService.createNewPlayer(session, playerName));
    }
}
