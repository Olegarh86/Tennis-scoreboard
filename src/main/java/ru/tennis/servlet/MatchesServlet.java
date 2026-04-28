package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.tennis.model.Match;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.util.JspHelper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName = req.getParameter("filter_by_player_name");
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Match> allFinishedMatches = FinishedMatchesPersistenceService.getMatchesByPlayerName(session, playerName);
            transaction.commit();
            req.setAttribute("allMatches", allFinishedMatches);
        }
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}
