package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.MatchesDto;
import ru.tennis.service.MatchesController;
import ru.tennis.util.*;

import java.io.IOException;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {
    private NameNormalizer nameNormalizer;
    private MatchesController matchesController;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        nameNormalizer = context.getNormalizer();
        matchesController = context.getMatchesController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = "page";
        String filterPlayerName = "filter_by_player_name";
        String pageNumber = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        String normalName = nameNormalizer.normalizePlayerName(playerName);
        MatchesDto dto = matchesController.getMatchesDto(normalName, TennisUtil.parsePage(pageNumber));

        if (dto.mustBeChanged()) {
            String url = UrlBuilder.buildUrl("/matches", page, String.valueOf(dto.page()), filterPlayerName, dto.playerName());
            RedirectHelper.redirectResponse(req, resp, url);
            return;
        }
        req.setAttribute(filterPlayerName, dto.playerName());
        req.setAttribute(page, dto.page());
        req.setAttribute("pageCount", dto.pageCount());
        req.setAttribute("allMatches", dto.allMatches());
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}

