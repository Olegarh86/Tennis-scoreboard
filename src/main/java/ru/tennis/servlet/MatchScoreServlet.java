package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.CurrentMatchDto;
import ru.tennis.exception.IncorrectParameterException;
import ru.tennis.service.MatchScoreService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.Parser;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;
import ru.tennis.validation.Validator;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {
    private static final String UUID_PARAM = "uuid";
    private static final String WINNER_PARAM = "winner";
    private MatchScoreService matchScoreService;
    private OngoingMatchesService ongoingMatchesService;
    private Validator validator;


    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute(ApplicationContext.class.getSimpleName());
        ongoingMatchesService = context.getOngoingMatchesService();
        matchScoreService = context.getMatchScoreService();
        validator = context.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchUuid = req.getParameter(UUID_PARAM);

        validator.validateParameter(matchUuid);
        UUID uuid = Parser.parseUuid(matchUuid);

        CurrentMatchDto currentMatchDto = ongoingMatchesService.getCurrentMatchDto(uuid);
        req.setAttribute("currentMatchDto", currentMatchDto);
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String winnerId = req.getParameter(WINNER_PARAM);
        String matchUuid = req.getParameter(UUID_PARAM);

        validator.validateParameter(matchUuid);
        validator.validateParameter(winnerId);
        Integer id = Parser.parseNumber(winnerId);

        if (id < 0) {
            throw new IncorrectParameterException("Winner id can't less than zero. Winner id: " + winnerId);
        }
        UUID uuid = Parser.parseUuid(matchUuid);

        Optional<UUID> uuidOptional = matchScoreService.updateCurrentMatch(id, uuid);

        if (uuidOptional.isEmpty()) {
            String url = UrlBuilder.buildUrl("/matches", Map.of("page", "1"));
            RedirectHelper.redirectResponse(req, resp, url);
        } else {
            RedirectHelper.redirectResponse(req, resp, UrlBuilder.buildUrl("/match-score", Map.of(UUID_PARAM,
                    uuidOptional.get().toString())));
        }
    }
}
