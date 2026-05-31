package ru.tennis.service;

import org.hibernate.Session;
import ru.tennis.dao.TennisDao;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public class FinishedMatchesPersistenceService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Сервис отвечает за персистентность двух разных сущностей (Player и Match), у него есть как минимум
        // две разные причины для изменений. Он "наследует" этот архитектурный недостаток от TennisDao
        // и нарушает Принцип единой ответственности (SRP).

    // TODO: Все публичные методы класса принимают в качестве параметра `org.hibernate.Session`.
        // Это "протечка" детали реализации в сигнатуры методов.
        // Клиенты этого сервиса вынуждены знать о существовании сессий Hibernate и управлять ими.

    // В текущей реализации класс является примером избыточного слоя.
        // Он не выполняет никакой логики — просто создаёт дополнительный уровень косвенности,
        // усложняя навигацию по коду.

    private final TennisDao tennisDao;

    // Можно использовать @RequiredArgsConstructor
    public FinishedMatchesPersistenceService(TennisDao tennisDao) {
        this.tennisDao = tennisDao;
    }

    public Player createNewPlayer(Session session, String name) {
        return tennisDao.createNewTennisPlayer(session, name);
    }

    public Optional<Player> getPlayerByName(Session session, String name) {
        return tennisDao.getTennisPlayerByName(session, name);
    }

    public void saveFinishedMatch(Session session, CurrentMatch currentMatch) {
        tennisDao.saveFinishedTennisMatch(session, currentMatch);
    }

    // Использование Optional для передачи в метод опционального значения не является идиоматичным для Optional.
        // Этот подход усложняет код для вызывающей стороны и не даёт преимуществ по сравнению с перегрузкой методов.
    public List<Match> getAllMatches(Session session, Optional<String> playerName, int pageSize, int offset) {
        return tennisDao.getAllTennisMatches(session, playerName, pageSize, offset);
    }

    // Использование Optional для передачи в метод опционального значения не является идиоматичным для Optional.
        // Этот подход усложняет код для вызывающей стороны и не даёт преимуществ по сравнению с перегрузкой методов.
    public Long getTotalNumberOfMatches(Session session, Optional<String> playerName) {
        return tennisDao.getTotalNumberAllTennisMatches(session, playerName);
    }
}
