package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.MatchNotFoundException;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.service.MatchScoreService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchScoreService matchScoreService;
    private OngoingMatchesService ongoingMatchesService;
    private static final String UUID = "uuid";
    private static final String WINNER = "winner";

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        ongoingMatchesService = context.getOngoingMatchesService();
        matchScoreService = context.getMatchScoreService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchUuid = req.getParameter(UUID);

        if (isMissing(resp, matchUuid, UUID)) {
            return;
        }
        Optional<CurrentMatch> mayBeMatch = ongoingMatchesService.getCurrentMatch(matchUuid);

        if (mayBeMatch.isEmpty()) {
            resp.sendError(SC_NOT_FOUND, "Match not found");
            return;
        }
        req.setAttribute("currentMatch", mayBeMatch.get());
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String winnerId = req.getParameter(WINNER);
        String matchUuid = req.getParameter(UUID);

        if (isMissing(resp, matchUuid, UUID)) {
            return;
        }
        if (isMissing(resp, winnerId, WINNER)) {
            return;
        }
        int id;
        try {
            id = Integer.parseInt(winnerId);
            if (id < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect parameter: " + WINNER);
            return;
        }

        CurrentMatch currentMatch;
        try {
            currentMatch = matchScoreService.updateCurrentMatch(id, matchUuid);
        } catch (MatchNotFoundException e) {
            resp.sendError(SC_BAD_REQUEST, "Match not found with id: " + matchUuid);
            return;
        } catch (SaveFinishedMatchException e) {
            resp.sendError(SC_BAD_REQUEST, "Error in time saved new finished match");
            return;
        }

        if (currentMatch.hasWinner()) {
            String url = UrlBuilder.buildUrl("/matches", "page", "1", "filter_by_player_name", "");
            RedirectHelper.redirectResponse(req, resp, url);
        } else {
            String paramValue = currentMatch.getUuid();
            RedirectHelper.redirectResponse(req, resp, UrlBuilder.buildUrl("/match-score", UUID, paramValue));
        }
    }

    private static boolean isMissing(HttpServletResponse resp, String value, String paramName) throws IOException {
        if (value == null || value.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Missing parameter " + paramName);
            return true;
        }
        return false;
    }
}
