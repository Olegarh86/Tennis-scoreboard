package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.service.MatchScoreService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchScoreService matchScoreService;
    private OngoingMatchesService ongoingMatchesService;
    private static final String UUID = "uuid";

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        ongoingMatchesService = context.getOngoingMatchesService();
        matchScoreService = context.getMatchScoreService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchUuid = req.getParameter(UUID);

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
        String winnerId = req.getParameter("winner");
        String matchUuid = req.getParameter(UUID);

        Optional<CurrentMatch> mayBeCurrentMatch = matchScoreService.updateCurrentMatch(winnerId, matchUuid);

        if (mayBeCurrentMatch.isPresent()) {
            String path = "/match-score";
            String paramValue = mayBeCurrentMatch.get().getUuid();
            RedirectHelper.redirectResponse(req, resp, UrlBuilder.buildUrl(path, UUID, paramValue));
        } else {
            String url = UrlBuilder.buildUrl("/matches", "page", "1", "filter_by_player_name", "");
            RedirectHelper.redirectResponse(req, resp, url);
        }
    }
}
