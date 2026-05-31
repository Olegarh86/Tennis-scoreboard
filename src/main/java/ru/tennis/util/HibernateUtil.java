package ru.tennis.util;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Можно сделать класс утилитным, например, с помощью @UtilityClass
        // или наоборот сделать все его методы нестатическими.

    // Класс является реализацией антипаттерна Service Locator.
        // Это затрудняет тестирование и создаёт неявные зависимости в коде.
        // Лучше перейти на внедрение зависимостей (Dependency Injection),
        // где экземпляр SessionFactory создаётся один раз и передаётся в конструкторы зависимых компонентов.

    @Getter // Геттер для SessionFactory не нужен.
    private static SessionFactory sessionFactory;

    public static void init(String configFile) {

        // Стоит проверить, что SessionFactory ещё не инициализирована
        Configuration cfg = new Configuration();
        sessionFactory = cfg.configure(configFile).buildSessionFactory();
    }

    public static Session getSession() {

        // Стоит добавить проверку, что sessionFactory != null

        // Лучше перейти на sessionFactory.getCurrentSession()
        return sessionFactory.openSession();
    }

    public static void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
