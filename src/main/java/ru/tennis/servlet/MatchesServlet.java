package ru.tennis.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.dao.TennisDao;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.MatchesDto;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.service.MatchesController;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.TennisUtil;
import ru.tennis.validation.PlayerNamesValidator;

import java.io.IOException;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {
    private PlayerNamesValidator validator;
    private FinishedMatchesPersistenceService service;
    private MatchesController matchesController;

    @Override
    public void init(ServletConfig config) {
        validator = new PlayerNamesValidator();
        TennisDao dao = new TennisDaoImpl();
        service = new FinishedMatchesPersistenceService(dao);
        matchesController = new MatchesController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName = req.getParameter("filter_by_player_name");
        String pageNumber = req.getParameter("page");

        String normalName = validator.normalizedPlayerName(playerName);
        MatchesDto dto = matchesController.getMatchesDto(service, normalName, TennisUtil.parsePage(pageNumber));

        if (dto.pageCount() < 1) {
            String path = String.format("/matches?page=%s&filter_by_player_name=%s", dto.page(), dto.playerName());
            RedirectHelper.redirectResponse(req, resp, path);
            return;
        }
        req.setAttribute("filter_by_player_name", dto.playerName());
        req.setAttribute("page", dto.page());
        req.setAttribute("pageCount", dto.pageCount());
        req.setAttribute("allMatches", dto.allMatches());
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}

