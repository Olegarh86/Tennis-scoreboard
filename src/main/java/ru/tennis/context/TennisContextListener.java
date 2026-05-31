package ru.tennis.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.tennis.util.HibernateUtil;

@WebListener
public class TennisContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.init("hibernate.cfg.xml");

        // Для помещения объектов в контекст можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        sce.getServletContext().setAttribute("appContext", new ApplicationContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.destroy();
    }
}
