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
        CurrentMatch currentMatch = OngoingMatchesService.getCurrentMatch(req.getParameter("uuid"));
        req.setAttribute("currentMatch", currentMatch);
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String winnerId = req.getParameter("winner");
        String uuid = req.getParameter("uuid");

        CurrentMatch currentMatch = OngoingMatchesService.getCurrentMatch(uuid);
        MatchScoreCalculationService.updateMatchState(currentMatch, winnerId);

        CurrentMatch matchAfterUpdate = OngoingMatchesService.getCurrentMatch(uuid);
        if (matchAfterUpdate != null) {
            req.setAttribute("currentMatch", matchAfterUpdate);
            req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
        } else {
            SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
            try (Session session = sessionFactory.openSession()) {
                int pageSize = 10;
                Transaction transaction = session.beginTransaction();
                FinishedMatchesPersistenceService.persist(session, currentMatch);
                List<Match> allFinishedMatches = FinishedMatchesPersistenceService.getAllMatches(session, pageSize, 0);
                Long totalItems = FinishedMatchesPersistenceService.getTotalCount(session);
                transaction.commit();

                int pageCount = (int) Math.ceil(totalItems / (double) pageSize);
                req.setAttribute("currentPage", 1);
                req.setAttribute("pageCount", pageCount);
                req.setAttribute("allMatches", allFinishedMatches);
            }
            resp.sendRedirect("/matches");
//            req.getRequestDispatcher("/matches").forward(req, resp);
        }
    }
}
