package ru.tennis.util;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    public static void init(String configFile) {
        Configuration cfg = new Configuration();
        sessionFactory = cfg.configure(configFile).buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
