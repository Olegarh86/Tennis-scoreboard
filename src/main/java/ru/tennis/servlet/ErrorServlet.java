package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.util.JspHelper;

import java.io.IOException;

@WebServlet(name = "errorPage", urlPatterns = "/errorPage")
public class ErrorServlet extends HttpServlet {
    private static final String ATTRIBUTE_ERROR_CODE = "jakarta.servlet.error.status_code";
    private static final String ATTRIBUTE_ERROR_MESSAGE = "jakarta.servlet.error.message";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        prepareErrorAttributes(req);
        forwardToErrorPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        prepareErrorAttributes(req);
        forwardToErrorPage(req, resp);
    }

    private void prepareErrorAttributes(HttpServletRequest req) {
        String code = "500";
        String message = "Unknown Error";

        if (req.getAttribute(ATTRIBUTE_ERROR_CODE) != null) {
            code = req.getAttribute(ATTRIBUTE_ERROR_CODE).toString();
        }

        if (req.getAttribute(ATTRIBUTE_ERROR_MESSAGE) != null) {
            message = req.getAttribute(ATTRIBUTE_ERROR_MESSAGE).toString();
        }
        req.setAttribute("status_code", code);
        req.setAttribute("message", message);
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("errorPage")).forward(req, resp);
    }
}
