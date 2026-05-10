package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.dto.MatchScoreDto;
import ru.tennis.service.MatchScoreController;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;

import java.io.IOException;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");

        MatchScoreDto dto = OngoingMatchesService.getCurrentMatchDto(uuid);

        req.setAttribute("currentMatch", dto.currentMatch());
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String winnerId = req.getParameter("winner");
        String uuid = req.getParameter("uuid");

        MatchScoreDto dto = MatchScoreController.updateMatch(winnerId, uuid);

        if (!uuid.equals(dto.currentMatch().uuid)) {
            req.setAttribute("currentPage", dto.currentPage());
            req.setAttribute("pageCount", dto.pageCount());
            req.setAttribute("allMatches", dto.allFinishedMatches());
            resp.sendRedirect(req.getContextPath() + "/matches?page=1&filter_by_player_name=");
        } else {
            req.setAttribute("currentMatch", dto.currentMatch());
            req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
        }
    }
}
