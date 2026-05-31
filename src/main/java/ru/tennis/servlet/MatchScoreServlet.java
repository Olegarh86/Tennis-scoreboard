package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.CurrentMatch;
import ru.tennis.exceptions.MatchNotFoundException;
import ru.tennis.exceptions.SaveFinishedMatchException;
import ru.tennis.service.MatchScoreService;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.JspHelper;
import ru.tennis.util.RedirectHelper;
import ru.tennis.util.UrlBuilder;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(name = "match-score", urlPatterns = "/match-score")
public class MatchScoreServlet extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет передаёт в слой представления доменные модели (`CurrentMatch`).
        // Передача доменных моделей в JSP не является хорошей практикой. Это нарушает принцип разделения ответственности между слоями
        // и связывает слой представления с моделью данных (что чревато ошибками, например, в случае переименования полей).
        // Лучше использовать DTO (Data Transfer Object) для передачи данных в представление.
        // DTO позволяют контролировать, какие именно данные передаются.

    // Логику обработки исключений можно реализовать в фильтре.
        // Так она будет централизована для всего приложения и её части не будут повторяться в разных местах.

    private MatchScoreService matchScoreService;
    private OngoingMatchesService ongoingMatchesService;

    // Константы нужно объявлять первыми (самыми верхними) в классе
    private static final String UUID = "uuid"; // Точнее назвать UUID_PARAM
    private static final String WINNER = "winner"; // Точнее назвать WINNER_PARAM

    @Override
    public void init() {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        ongoingMatchesService = context.getOngoingMatchesService();
        matchScoreService = context.getMatchScoreService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchUuid = req.getParameter(UUID);

        if (isMissing(resp, matchUuid, UUID)) {
            return;
        }

        // Парсить UUID нужно в сервлете и только после этого передавать объект UUID (а не String) в сервис
        // Понятнее было бы название CurrentMatchOptional
        Optional<CurrentMatch> mayBeMatch = ongoingMatchesService.getCurrentMatch(matchUuid);

        if (mayBeMatch.isEmpty()) {
            resp.sendError(SC_NOT_FOUND, "Match not found");
            return;
        }

        // TODO: Сервлет не должен передавать доменные модели во View
        req.setAttribute("currentMatch", mayBeMatch.get());
        req.getRequestDispatcher(JspHelper.getPath("match-score")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String winnerId = req.getParameter(WINNER);
        String matchUuid = req.getParameter(UUID);

        if (isMissing(resp, matchUuid, UUID)) {
            return;
        }
        if (isMissing(resp, winnerId, WINNER)) {
            return;
        }

        // Логику парсинга ID лучше вынести во вспомогательный метод
        int id;
        try {
            id = Integer.parseInt(winnerId);
            if (id < 0) {

                // Управление потоком выполнения через исключения является антипаттерном
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect parameter: " + WINNER);
            return;
        }

        // TODO: Сервлет не должен работать с доменными моделями — достаточно получать из сервиса ID матча
        CurrentMatch currentMatch;
        try {
            currentMatch = matchScoreService.updateCurrentMatch(id, matchUuid);

        // Логику обработки исключений можно реализовать в фильтре.
        } catch (MatchNotFoundException e) {
            resp.sendError(SC_BAD_REQUEST, "Match not found with id: " + matchUuid);
            return;
        } catch (SaveFinishedMatchException e) {
            resp.sendError(SC_BAD_REQUEST, "Error in time saved new finished match");
            return;
        }

        if (currentMatch.hasWinner()) {

            // Здесь не нужно указывать пустое значение фильтра
            String url = UrlBuilder.buildUrl("/matches", "page", "1", "filter_by_player_name", "");
            RedirectHelper.redirectResponse(req, resp, url);
        } else {

            // Точнее назвать matchIdValue
            String paramValue = currentMatch.getUuid();
            RedirectHelper.redirectResponse(req, resp, UrlBuilder.buildUrl("/match-score", UUID, paramValue));
        }
    }

    // Этот метод не должен отправлять ответ с ошибкой — эта логика должна быть в вызывающем методе.
    // Этому методу не нужно быть static
    private static boolean isMissing(HttpServletResponse resp, String value, String paramName) throws IOException {
        if (value == null || value.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Missing parameter " + paramName);
            return true;
        }
        return false;
    }
}
