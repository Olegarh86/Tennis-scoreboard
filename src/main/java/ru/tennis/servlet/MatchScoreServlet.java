package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.tennis.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.service.MatchScoreCalculationService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currentMatch", OngoingMatchesService.getCurrentMatch(req.getParameter("uuid")));
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerGetPoint = req.getParameter("winner");
        String uuid = req.getParameter("uuid");

        CurrentMatch currentMatch = OngoingMatchesService.getCurrentMatch(uuid);
        MatchScoreCalculationService.updateScore(currentMatch, playerGetPoint);

        CurrentMatch newCurrentMatch = OngoingMatchesService.getCurrentMatch(uuid);
        if (newCurrentMatch != null) {
            req.setAttribute("currentMatch", newCurrentMatch);
            req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
        } else {
            SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                FinishedMatchesPersistenceService.persist(session, currentMatch);
                List<Match> allFinishedMatches = FinishedMatchesPersistenceService.getAllMatches(session);
                transaction.commit();
                req.setAttribute("allMatches", allFinishedMatches);
            }
            req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
        }
    }
}
