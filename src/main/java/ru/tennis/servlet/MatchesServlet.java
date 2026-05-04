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
import ru.tennis.model.Player;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.util.JspHelper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        int pageSize = 5;
        String playerName = req.getParameter("filter_by_player_name");
        String pageNumber = req.getParameter("page");

        if (pageNumber != null) {
            currentPage = Integer.parseInt(pageNumber);
        }
        int offset = (currentPage - 1) * pageSize;

        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Match> allMatches;
            Long totalItems;

            if (playerName != null) {
                totalItems = FinishedMatchesPersistenceService.getTotalCountWithName(session, playerName);
                allMatches = FinishedMatchesPersistenceService.getMatchesByPlayerName(session, playerName, pageSize, offset);
            } else {
                totalItems = FinishedMatchesPersistenceService.getTotalCount(session);
                allMatches = FinishedMatchesPersistenceService.getAllMatches(session, pageSize,  offset);
            }


            transaction.commit();
            int pageCount = (int) Math.ceil(totalItems / (double) pageSize);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("allMatches", allMatches);
        }
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}
