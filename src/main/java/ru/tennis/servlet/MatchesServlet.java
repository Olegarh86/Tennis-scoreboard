package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.context.ApplicationContext;
import ru.tennis.dto.MatchesDto;
import ru.tennis.service.MatchesService;
import ru.tennis.util.*;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "matches", urlPatterns = "/matches")
public class MatchesServlet extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет передаёт в слой представления JPA сущности (`List<Match> allMatches` в MatchesDto).
        // Передача Entity объектов в JSP не является хорошей практикой.
        // Это может привести к проблемам производительности (например, ленивая загрузка)
        // и безопасности (например, случайная передача чувствительных данных).
        // Кроме того, это связывает слой представления с моделью данных.
        // Лучше использовать DTO (Data Transfer Object) для передачи данных в представление.
        // DTO позволяют контролировать, какие именно данные передаются.

    // Логику обработки исключений можно реализовать в фильтре.
        // Так она будет централизована для всего приложения и её части не будут повторяться в разных местах.

    private static final String PAGE = "page"; // Точнее назвать PAGE_PARAM
    private static final String FILTER_BY_PLAYER_NAME = "filter_by_player_name"; // Точнее назвать FILTER_PARAM
    private NameNormalizer nameNormalizer;
    private MatchesService matchesService;

    @Override
    public void init() {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");
        nameNormalizer = context.getNormalizer();
        matchesService = context.getMatchesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumber = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        String normalName = nameNormalizer.normalizePlayerName(playerName);

        // Логику парсинга номера страницы лучше вынести во вспомогательный метод
        int page = 0;
        try {
            page = Integer.parseInt(pageNumber);
        } catch (Exception e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect parameter: " + PAGE);
            // TODO: Здесь выполнение продолжается. После отправки ответа клиенту поток выполнения должен прерваться.
        }
        MatchesDto matchesDto = matchesService.getMatchesDto(normalName, page);

        if (matchesDto.needsRedirect()) {
            String url = UrlBuilder.buildUrl("/matches", PAGE, String.valueOf(matchesDto.page()), FILTER_BY_PLAYER_NAME, matchesDto.playerName());
            RedirectHelper.redirectResponse(req, resp, url);
            return;
        }

        // TODO: Сервлет не должен передавать JPA Entity во View.
        req.setAttribute("matchesDto", matchesDto);
        req.getRequestDispatcher(JspHelper.getPath("matches")).forward(req, resp);
    }
}
