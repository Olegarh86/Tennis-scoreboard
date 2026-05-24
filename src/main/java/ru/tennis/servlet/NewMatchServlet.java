package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.service.CurrentMatchCreator;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;
import ru.tennis.validation.PlayerNamesValidator;
import ru.tennis.validation.ValidationResult;

import java.io.IOException;

@WebServlet(name = "new-match", urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {
    private PlayerNamesValidator validator;
    private CurrentMatchCreator currentMatchCreator;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        validator = context.getValidator();
        currentMatchCreator = context.getCurrentMatchCreator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToNewMatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName1 = "playerName1";
        String playerName2 = "playerName2";
        String paramNameUuid = "uuid";
        String name1 = req.getParameter(playerName1);
        String name2 = req.getParameter(playerName2);

        ValidationResult validationResult = validator.validate(name1, name2);

        if (validationResult.hasErrors()) {
            req.setAttribute("errors", validationResult.errors());
            req.setAttribute(playerName1, validationResult.normalName1());
            req.setAttribute(playerName2, validationResult.normalName2());
            forwardToNewMatch(req, resp);
        } else {
            String newMatchUuid = currentMatchCreator.createNewCurrentMatch(validationResult.normalName1(),
                    validationResult.normalName2());
            String path = "/match-score";
            String url = UrlBuilder.buildUrl(path, paramNameUuid, newMatchUuid);
            RedirectHelper.redirectResponse(req, resp, url);
        }
    }

    private void forwardToNewMatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("new-match")).forward(req, resp);
    }
}

