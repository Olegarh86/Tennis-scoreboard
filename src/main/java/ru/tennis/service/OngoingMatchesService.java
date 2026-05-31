package ru.tennis.service;

import ru.tennis.dto.CurrentMatch;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Ключом должен быть UUID, а не его строковое представление.
    private final Map<String, CurrentMatch> ONGOING_MATCHES = new ConcurrentHashMap<>();

    // Этот метод не должен получать ID из CurrentMatch.
        // Хранилище должно само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
    public void addMatch(CurrentMatch currentMatch) {
        if (currentMatch != null) {
            ONGOING_MATCHES.put(currentMatch.getUuid(), currentMatch);
        }
    }

    // Лучше принимать объект UUID. Валидация и парсинг должны будут происходить на входе этих данных в приложение.
    public Optional<CurrentMatch> getCurrentMatch(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(ONGOING_MATCHES.get(uuid));
    }

    // Лучше принимать объект UUID. Валидация и парсинг должны будут происходить на входе этих данных в приложение.
    public void deleteMatch(String uuid) {
        ONGOING_MATCHES.remove(uuid);
    }
}
