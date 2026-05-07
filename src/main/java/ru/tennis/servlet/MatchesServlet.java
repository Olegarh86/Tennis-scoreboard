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
import java.util.Optional;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 7;
        String playerName = req.getParameter("filter_by_player_name");
        String pageNumber = req.getParameter("page");

        if (!pageNumber.isEmpty()) {
            page = Integer.parseInt(pageNumber);
        }

        if (page < 1) {
            page = 1;
            req.getRequestDispatcher(String.format("/matches?page=%s&filter_by_player_name=%s", page, playerName))
                    .forward(req, resp);
        }

        int offset = (page - 1) * pageSize;

        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Match> allMatches;
            Long totalItems;

            if (playerName.isEmpty()) {
                totalItems = FinishedMatchesPersistenceService.getTotalNumberOfMatches(session, Optional.empty());
                allMatches = FinishedMatchesPersistenceService.getAllMatches(session, Optional.empty(),
                        pageSize, offset);
            } else {
                totalItems = FinishedMatchesPersistenceService.getTotalNumberOfMatches(session,Optional.of(playerName));
                allMatches = FinishedMatchesPersistenceService.getAllMatches(session, Optional.of(playerName), pageSize,
                        offset);
            }
            transaction.commit();

            int pageCount = (int) Math.ceil(totalItems / (double) pageSize);

            if (pageCount < 1) {
                page = 1;
                req.setAttribute("filter_by_player_name", playerName);
                req.setAttribute("page", page);
                req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
            }

            if (page > pageCount) {
                page = pageCount;
                req.getRequestDispatcher(String.format("/matches?page=%s&filter_by_player_name=%s", page, playerName))
                        .forward(req, resp);
            }
            req.setAttribute("filter_by_player_name", playerName);
            req.setAttribute("page", page);
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("allMatches", allMatches);
        }
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}
