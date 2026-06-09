package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.NameNormalizer;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;
import ru.tennis.validation.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "new-match", urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {
    private static final String PLAYER_NAME_1_PARAM = "playerName1";
    private static final String PLAYER_NAME_2_PARAM = "playerName2";
    private static final String UUID_PARAM = "uuid";
    private static final String ERRORS_PARAM = "errors";
    private static final String PATH_MATCH_SCORE = "/match-score";
    private Validator validator;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute(ApplicationContext.class.getSimpleName());
        validator = context.getValidator();
        ongoingMatchesService = context.getOngoingMatchesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToNewMatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name1 = req.getParameter(PLAYER_NAME_1_PARAM);
        String name2 = req.getParameter(PLAYER_NAME_2_PARAM);

        String normalizedName1 = NameNormalizer.normalizePlayerName(name1);
        String normalizedName2 = NameNormalizer.normalizePlayerName(name2);

        List<String> errors = validator.validatePlayerNames(normalizedName1, normalizedName2);

        if (errors.isEmpty()) {
            UUID newMatchUuid = ongoingMatchesService.createNewCurrentMatch(normalizedName1, normalizedName2);
            String url = UrlBuilder.buildUrl(PATH_MATCH_SCORE, Map.of(UUID_PARAM, newMatchUuid.toString()));
            RedirectHelper.redirectResponse(req, resp, url);
        } else {
            req.setAttribute(ERRORS_PARAM, errors);
            req.setAttribute(PLAYER_NAME_1_PARAM, normalizedName1);
            req.setAttribute(PLAYER_NAME_2_PARAM, normalizedName2);
            forwardToNewMatch(req, resp);
        }
    }

    private void forwardToNewMatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("new-match")).forward(req, resp);
    }
}
