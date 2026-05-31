package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.tennis.util.JspHelper;

import java.io.IOException;

@Slf4j
@WebServlet(name = "errorPage", urlPatterns = "/errorPage")
public class ErrorServlet extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // Вместо вручную объявленных констант лучше использовать стандартные константы из RequestDispatcher:
    private static final String ATTRIBUTE_ERROR_CODE = "jakarta.servlet.error.status_code"; // RequestDispatcher.ERROR_STATUS_CODE
    private static final String ATTRIBUTE_ERROR_MESSAGE = "jakarta.servlet.error.message"; // RequestDispatcher.ERROR_MESSAGE
    private static final String ATTRIBUTE_EXCEPTION = "jakarta.servlet.error.exception"; // RequestDispatcher.ERROR_EXCEPTION

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        prepareErrorAttributes(req);
        forwardToErrorPage(req, resp);
    }

    private void prepareErrorAttributes(HttpServletRequest req) {
        String code = "500";
        String message = "Unknown Error";
        Throwable exception = null;

        if (req.getAttribute(ATTRIBUTE_ERROR_CODE) != null) {
            code = req.getAttribute(ATTRIBUTE_ERROR_CODE).toString();
        }

        if (req.getAttribute(ATTRIBUTE_ERROR_MESSAGE) != null) {
            message = req.getAttribute(ATTRIBUTE_ERROR_MESSAGE).toString();
        }
        Object exceptionObj = req.getAttribute(ATTRIBUTE_EXCEPTION);
        if (exceptionObj instanceof Throwable) {
            exception = (Throwable) exceptionObj;
        }
        req.setAttribute("status_code", code);
        req.setAttribute("message", message);
        if (exception != null) {
            log.error("Unhandled exception with code: {}, and message: {}", code, message, exception);
        } else {
            log.warn("Error response with code: {}, and message: {}", code, message);
        }
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("errorPage")).forward(req, resp);
    }
}
