package ru.tennis.contextListener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.tennis.model.Match;
import ru.tennis.model.Player;
import ru.tennis.util.HibernateUtil;

@WebListener
public class TennisContextListener implements ServletContextListener {
    private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Player.class);
        cfg.addAnnotatedClass(Match.class);
        cfg.configure();
        sessionFactory = cfg.buildSessionFactory();
        sce.getServletContext().setAttribute("sessionFactory", sessionFactory);
        HibernateUtil.initSessionFactory(sessionFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sessionFactory.close();
    }
}
