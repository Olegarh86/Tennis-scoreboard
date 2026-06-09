package ru.tennis.context;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.tennis.dao.MatchDao;
import ru.tennis.dao.MatchDaoImpl;
import ru.tennis.dao.PlayerDao;
import ru.tennis.dao.PlayerDaoImpl;
import ru.tennis.service.*;
import ru.tennis.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationContext {

    private final SessionFactory sessionFactory;
    @Getter
    private final Validator validator;
    @Getter
    private final FinishedMatchesPersistenceService persistenceService;
    @Getter
    private final OngoingMatchesService ongoingMatchesService;
    @Getter
    private final MatchScoreService matchScoreService;
    @Getter
    private final MatchesService matchesService;

    public ApplicationContext() {
        PlayerDao playerDao = new PlayerDaoImpl();
        MatchDao matchDao = new MatchDaoImpl();
        sessionFactory = buildSessionFactory();
        this.validator = new Validator();
        this.persistenceService = new FinishedMatchesPersistenceServiceImpl(playerDao, matchDao, sessionFactory);
        this.ongoingMatchesService = new OngoingMatchesServiceImpl(persistenceService);
        this.matchScoreService = new MatchScoreServiceImpl(ongoingMatchesService, persistenceService);
        this.matchesService = new MatchesServiceImpl(persistenceService);
    }

    private SessionFactory buildSessionFactory() {
        Properties prop = new Properties();
        try (InputStream in = ApplicationContext.class.getClassLoader().getResourceAsStream("db.properties")) {
            prop.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String configFile = "hibernate.cfg.xml";
        Configuration cfg = new Configuration().configure(configFile);
        cfg.setProperty("hibernate.connection.username", prop.getProperty("db.username"));
        cfg.setProperty("hibernate.connection.password", prop.getProperty("db.password"));
        return cfg.buildSessionFactory();
    }

    public void close() {
        sessionFactory.close();
    }
}
