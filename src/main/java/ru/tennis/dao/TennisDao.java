package ru.tennis.dao;

import org.hibernate.Session;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public interface TennisDao {

    // TODO: Интерфейс нарушает Принцип единой ответственности (SRP): описывает два контракта — для работы с таблицей игроков и с таблицей матчей.

    // Лучше иметь разные методы для выборки матчей с фильтром по имени и без него, а также для подсчёта количества,
        // чем собирать эту логику в одном методе. Если правила фильтрации поменяются,
        // то нужно будет изменить/дописать только некоторые методы, оставив логику выборки без фильтра без изменений.

    // TODO: Текущая реализация методов заставляет слой бизнес-логики (сервисы) напрямую зависеть от низкоуровневой детали реализации —
        // `org.hibernate.Session` и делает его жёстко привязанным к Hibernate.
        // Лучше внедрять в DAO объект SessionFactory как зависимость и в методах получать из неё объект текущей сессии,
        // а не принимать в качестве аргумента в методы.
        // Стоит подумать, как оставить в сервисном слое управление транзакциями, но при этом избавить его от зависимости от Hibernate (`Session`).

    // Можно дать более распространённое и точное название: `save`
    // Метод должен принимать объект Player, а не просто имя игрока
    Player createNewTennisPlayer(Session session, String name);

    // Можно дать более распространённое и точное название: `findByName`
    Optional<Player> getTennisPlayerByName(Session session, String name);

    // Можно дать более распространённое и точное название: `save`
    // Если метод сохранения матчей будет возвращать сохранённую сущность,
        // то работа с ней (или сгенерированными БД данными) в клиентском коде будет более явной
    // TODO: Передача доменной модели CurrentMatch в DAO нарушает Принцип разделения ответственности (Separation of Concerns)
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
    void saveFinishedTennisMatch(Session session, CurrentMatch currentMatch);

    // Можно дать более распространённое и точное название: `findAll`
    // Использование Optional для передачи в метод опционального значения не является идиоматичным для Optional.
        // Этот подход усложняет код для вызывающей стороны и не даёт преимуществ по сравнению с перегрузкой методов.
    List<Match> getAllTennisMatches(Session session, Optional<String> playerName, int pageSize, int offset);

    // Можно дать более распространённое и точное название: `countAll`
    // Использование Optional для передачи в метод опционального значения не является идиоматичным для Optional.
        // Этот подход усложняет код для вызывающей стороны и не даёт преимуществ по сравнению с перегрузкой методов.
    Long getTotalNumberAllTennisMatches(Session session, Optional<String> playerName);
}
