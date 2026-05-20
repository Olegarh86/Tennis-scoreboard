package ru.tennis.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.dao.TennisDao;
import ru.tennis.dao.TennisDaoImpl;
import ru.tennis.dto.MatchCreateDto;
import ru.tennis.service.FinishedMatchesPersistenceService;
import ru.tennis.service.MatchCreator;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.validation.PlayerNamesValidator;
import ru.tennis.validation.ValidationResult;

import java.io.IOException;


@WebServlet(name = "new-match", urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {
    private PlayerNamesValidator validator;
    private FinishedMatchesPersistenceService service;
    private OngoingMatchesService ongoingMatchesService;
    private MatchCreator  matchCreator;

    @Override
    public void init(ServletConfig config) {
        validator = new PlayerNamesValidator();
        TennisDao dao = new TennisDaoImpl();
        service = new FinishedMatchesPersistenceService(dao);
        ongoingMatchesService = new OngoingMatchesService();
        matchCreator = new MatchCreator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("new-match")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName1 = req.getParameter("Имя игрока 1");
        String playerName2 = req.getParameter("Имя игрока 2");

        ValidationResult validationResult = validator.validate(playerName1, playerName2);

        if (validationResult.hasErrors()) {
            req.setAttribute("errors", validationResult.errors());
            req.setAttribute("playerName1", validationResult.normalizedName1());
            req.setAttribute("playerName2", validationResult.normalizedName2());
            req.getRequestDispatcher(JspHelper.getPath("new-match")).forward(req, resp);
        } else {
            MatchCreateDto dto = matchCreator.createNewCurrentMatch(ongoingMatchesService, service,
                    validationResult.normalizedName1(),
                    validationResult.normalizedName2());
            String path = "/match-score?uuid=" + dto.uuid();
            RedirectHelper.redirectResponse(req, resp, path);
        }
    }
}

