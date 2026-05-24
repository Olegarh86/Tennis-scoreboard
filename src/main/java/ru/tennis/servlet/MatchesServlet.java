package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.MatchesDto;
import ru.tennis.service.MatchesService;
import ru.tennis.util.*;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {
    private static final String PAGE = "page";
    private static final String FILTER_BY_PLAYER_NAME = "filter_by_player_name";
    private NameNormalizer nameNormalizer;
    private MatchesService matchesService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        nameNormalizer = context.getNormalizer();
        matchesService = context.getMatchesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumber = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        String normalName = nameNormalizer.normalizePlayerName(playerName);
        int page = 0;
        try {
            page = Integer.parseInt(pageNumber);
        } catch (Exception e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect parameter: " + PAGE);
        }
        MatchesDto matchesDto = matchesService.getMatchesDto(normalName, page);

        if (matchesDto.needsRedirect()) {
            String url = UrlBuilder.buildUrl("/matches", PAGE, String.valueOf(matchesDto.page()), FILTER_BY_PLAYER_NAME, matchesDto.playerName());
            RedirectHelper.redirectResponse(req, resp, url);
            return;
        }
        req.setAttribute("matchesDto", matchesDto);
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}
