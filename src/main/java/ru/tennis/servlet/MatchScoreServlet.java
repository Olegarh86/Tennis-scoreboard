package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.dao.TennisDao;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.service.MatchScoreController;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchScoreController matchScoreController;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        TennisDao tennisDao = new TennisDaoImpl();
        FinishedMatchesPersistenceService persistenceService = new FinishedMatchesPersistenceService(tennisDao);
        ongoingMatchesService = new OngoingMatchesService();
        matchScoreController = new MatchScoreController(ongoingMatchesService, persistenceService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");

        Optional<CurrentMatch> mayBeMatch = ongoingMatchesService.getCurrentMatch(uuid);

        if (mayBeMatch.isEmpty()) {
            req.setAttribute("jakarta.servlet.error.status_code", "404");
            req.setAttribute("jakarta.servlet.error.message", "This match not found. Enter correct uuid of match");
            req.getRequestDispatcher("/errorPage").forward(req, resp);
            return;
        }
        req.setAttribute("currentMatch", mayBeMatch.get());
        forwardToMatchScore(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String winnerId = req.getParameter("winner");
        String uuid = req.getParameter("uuid");

        Optional<CurrentMatch> mayBeCurrentMatch = matchScoreController.updateCurrentMatch(winnerId, uuid);

        if (mayBeCurrentMatch.isPresent()) {
            req.setAttribute("currentMatch", mayBeCurrentMatch.get());
            forwardToMatchScore(req, resp);
        } else {
            String url = UrlBuilder.buildUrl("/matches", "page", "1", "filter_by_player_name", "");
            RedirectHelper.redirectResponse(req, resp, url);
        }
    }

    private static void forwardToMatchScore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }
}
