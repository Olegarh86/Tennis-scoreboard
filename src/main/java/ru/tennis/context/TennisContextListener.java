package ru.tennis.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TennisContextListener implements ServletContextListener {
    private ApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        applicationContext = new ApplicationContext();
        sce.getServletContext().setAttribute(ApplicationContext.class.getSimpleName(), applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        applicationContext.close();
    }
}
