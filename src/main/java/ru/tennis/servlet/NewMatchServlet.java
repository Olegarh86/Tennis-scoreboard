package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.CurrentMatch;
import ru.tennis.service.MatchCreator;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;

import java.io.IOException;


@WebServlet(name = "new-match", urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("new-match")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName1 = req.getParameter("Имя игрока 1");
        String playerName2 = req.getParameter("Имя игрока 2");

        CurrentMatch currentMatch = MatchCreator.createMatch(playerName1, playerName2);
        OngoingMatchesService.addMatch(currentMatch);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + currentMatch.uuid);
    }
}

