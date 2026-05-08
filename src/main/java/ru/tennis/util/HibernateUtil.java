package ru.tennis.util;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    public static void initSessionFactory(SessionFactory factory) {
        sessionFactory = factory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
