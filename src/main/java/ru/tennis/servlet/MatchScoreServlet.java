package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.CurrentMatch;
import ru.tennis.service.MatchScoreCalculationService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currentMatch", OngoingMatchesService.getCurrentMatch(req.getParameter("uuid")));
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idPlayerGetPoint = req.getParameter("winner");
        String uuid = req.getParameter("uuid");

        CurrentMatch currentMatch = OngoingMatchesService.getCurrentMatch(uuid);
        MatchScoreCalculationService.updateScore(currentMatch, idPlayerGetPoint);

        req.setAttribute("currentMatch", OngoingMatchesService.getCurrentMatch(req.getParameter("uuid")));
        if (currentMatch.endMatch == false) {
            req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req,resp);
        }
    }
}
