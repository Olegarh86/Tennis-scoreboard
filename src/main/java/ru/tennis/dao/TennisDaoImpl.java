package ru.tennis.dao;

import org.hibernate.Session;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;

import java.util.List;
import java.util.Optional;

public class TennisDaoImpl implements TennisDao {

    // Ключевые слова в тексте HQL-запросов (`from`, `where` и др.) написаны в нижнем регистре.
        // Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL в верхнем регистре (`UPPERCASE`) является общепринятым стандартом.
        // Это значительно улучшает читаемость запросов, так как визуально отделяет синтаксические конструкции языка от имён сущностей и полей.

    // Текст HQL запроса удобнее читать, когда он логично разбит на строки, даже если он короткий.
        // Для визуального разделения запросов на строки лучше использовать текстовые блоки

    // Название каждого именованного параметра тоже лучше вынести в константу с понятным названием.

    // TODO: Проблема N+1 запросов в методе выборки матчей.
        // Метод `getAllTennisMatches` выполняет HQL-запросы вида `"select m from Match ..."`.
        // Сущность `Match` имеет связи `@ManyToOne` с `Player`, поэтому при выполнении такого запроса
        // Hibernate сначала получит список матчей (1 запрос), а затем он будет выполнять по 2 дополнительных `SELECT` запроса
        // для каждого матча, чтобы получить связанных с ним игроков. Если на странице 10 матчей,
        // это приведёт к 21 запросу (если все игроки будут разные) вместо одного.

    // TODO: Тело каждого метода стоит обернуть в try-catch и отлавливать HibernateException или PersistenceException.
        // Слой DAO должен перехватывать специфичные для технологии исключения (например, `HibernateException`)
        // и оборачивать их в свои, более общие исключения слоя доступа к данным (например, `DataAccessException`).
        // Это скрывает детали реализации от верхних слоёв и делает их независимыми от деталей реализации DAO.

    private static final String GET_PLAYER_BY_NAME = "select p from Player p where p.name = :name";
    private static final String GET_ALL_MATCHES_QUERY = "select m from Match m order by id DESC LIMIT :pageSize OFFSET " +
                                                        ":offset";
    private static final String GET_ALL_MATCHES_BY_NAME_QUERY = "select m from Match m where m.player1.name = " +
                                                                ":playerName OR m.player2.name = :playerName order by id" +
                                                                " DESC LIMIT :pageSize OFFSET :offset";
    private static final String GET_TOTAL_NUMBER_MATCHES = "select count(*) from Match";
    private static final String GET_TOTAL_NUMBER_MATCHES_BY_NAME = "select count(*) from Match where player1.name = " +
                                                                   ":playerName OR player2.name = :playerName";

    // TODO: DAO не должен знать, как создаются объекты. Он должен просто получать готовый объект и выполнять с ним нужную операцию.
    @Override
    public Player createNewTennisPlayer(Session session, String name) {
        Player player = Player.builder().name(name).build();
        session.persist(player);
        return player;
    }

    @Override
    public Optional<Player> getTennisPlayerByName(Session session, String name) {
        // Можно использовать uniqueResultOptional():
        /*
        return session.createQuery(GET_PLAYER_BY_NAME, Player.class)
                .setParameter("name", name)
                .uniqueResultOptional();
         */
        return Optional.ofNullable(session.createQuery(GET_PLAYER_BY_NAME, Player.class)
                .setParameter("name", name)
                .uniqueResult());
    }

    // TODO: DAO не должен знать, как создаются объекты. Он должен просто получать готовый объект и выполнять с ним нужную операцию.
    @Override
    public void saveFinishedTennisMatch(Session session, CurrentMatch currentMatch) {
        Player player1 =
                Player.builder().id(currentMatch.getFirstPlayer().getId()).name(currentMatch.getFirstPlayer().getName()).build();
        Player player2 =
                Player.builder().id(currentMatch.getSecondPlayer().getId()).name(currentMatch.getSecondPlayer().getName()).build();
        Player winner =
                Player.builder().id(currentMatch.getWinner().getWinnerId()).name(currentMatch.getWinner().getWinnerName()).build();
        Match match = Match.builder().player1(player1).player2(player2).winner(winner).build();
        session.persist(match);
    }

    // Лучше иметь разные методы для выборки матчей с фильтром по имени и без него,
        // чем собирать эту логику в одном методе. Если правила фильтрации поменяются,
        // то нужно будет изменить/дописать только некоторые методы, оставив логику выборки без фильтра без изменений.
    @Override
    public List<Match> getAllTennisMatches(Session session, Optional<String> playerName, int pageSize, int offset) {
        if (playerName.isEmpty()) {
            // Лучше использовать специальные методы вместо именованных параметров —
                // Так Hibernate сам генерирует правильный и оптимизированный SQL-запрос для той базы данных,
                // с которой он в данный момент работает:
            /*
            return session.createQuery(GET_ALL_MATCHES_QUERY, Match.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
             */
            return session.createQuery(GET_ALL_MATCHES_QUERY, Match.class)
                    .setParameter("pageSize", pageSize)
                    .setParameter("offset", offset)
                    .list();
        } else {
            // Лучше использовать специальные методы вместо именованных параметров —
                // Так Hibernate сам генерирует правильный и оптимизированный SQL-запрос для той базы данных,
                // с которой он в данный момент работает:
            /*
            return session.createQuery(GET_ALL_MATCHES_BY_NAME_QUERY, Match.class)
                    .setParameter("playerName", playerName.get())
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
             */
            return session.createQuery(GET_ALL_MATCHES_BY_NAME_QUERY, Match.class)
                    .setParameter("playerName", playerName.get())
                    .setParameter("pageSize", pageSize)
                    .setParameter("offset", offset)
                    .list();
        }
    }

    // Лучше иметь разные методы для подсчёта количества матчей с фильтром по имени и без него,
        // чем собирать эту логику в одном методе. Если правила фильтрации поменяются,
        // то нужно будет изменить/дописать только некоторые методы, оставив логику выборки без фильтра без изменений.
    @Override
    public Long getTotalNumberAllTennisMatches(Session session, Optional<String> playerName) {
        if (playerName.isEmpty()) {
            return session.createQuery(GET_TOTAL_NUMBER_MATCHES, Long.class).uniqueResult();
        } else {
            return session.createQuery(GET_TOTAL_NUMBER_MATCHES_BY_NAME, Long.class)
                    .setParameter("playerName", playerName.get())
                    .uniqueResult();
        }
    }
}
