package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.dto.MatchesDto;
import ru.tennis.service.MatchesController;
import ru.tennis.util.JspHelper;
import ru.tennis.util.TennisUtil;

import java.io.IOException;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName = req.getParameter("filter_by_player_name");
        String pageNumber = req.getParameter("page");

        MatchesDto dto = MatchesController.getMatchesDto(playerName, TennisUtil.parsePage(pageNumber));

        if (dto.pageCount() < 1) {
            String request = String.format("/matches?page=%s&filter_by_player_name=%s", dto.page(), dto.playerName());
            req.getRequestDispatcher(request).forward(req, resp);
        }
        req.setAttribute("filter_by_player_name", dto.playerName());
        req.setAttribute("page", dto.page());
        req.setAttribute("pageCount", dto.pageCount());
        req.setAttribute("allMatches", dto.allMatches());
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}

