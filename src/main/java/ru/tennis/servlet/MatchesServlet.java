package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.MatchPageDto;
import ru.tennis.service.MatchesService;
import ru.tennis.util.*;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {
    private static final String PAGE_PARAM = "page";
    private static final String FILTER_PARAM = "filter_by_player_name";
    private static final int PAGE_SIZE = 7;
    private static final String FIRST_PAGE = "1";
    private MatchesService matchesService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute(ApplicationContext.class.getSimpleName());
        matchesService = context.getMatchesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumber = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        String normalName = NameNormalizer.normalizePlayerName(playerName);
        Integer page = Parser.parseNumber(pageNumber);

        if (page < 1) {
            String url = UrlBuilder.buildUrl("/matches", Map.of(PAGE_PARAM, FIRST_PAGE, FILTER_PARAM, normalName));
            RedirectHelper.redirectResponse(req, resp, url);
            return;
        }
        MatchPageDto matchPageDto = matchesService.getMatchPageDto(normalName, page, PAGE_SIZE);

        if (matchPageDto.page() > matchPageDto.pageCount()) {
            String url = UrlBuilder.buildUrl("/matches", Map.of(PAGE_PARAM, String.valueOf(matchPageDto.pageCount()),
                    FILTER_PARAM, normalName));
            RedirectHelper.redirectResponse(req, resp, url);
            return;
        }
        req.setAttribute("matchesDto", matchPageDto);
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}
