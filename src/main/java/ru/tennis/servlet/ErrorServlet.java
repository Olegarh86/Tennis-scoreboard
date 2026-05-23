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
    private String code = "empty";
    private String message = "empty";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String attributeErrorCode = "jakarta.servlet.error.status_code";

        if (req.getAttribute(attributeErrorCode) != null) {
            code = req.getAttribute(attributeErrorCode).toString();
        }
        String attributeErrorMessage = "jakarta.servlet.error.message";

        if (req.getAttribute(attributeErrorMessage) != null) {
            message = req.getAttribute(attributeErrorMessage).toString();
        }
        req.setAttribute("status_code", code);
        req.setAttribute("message", message);
        forwardToErrorPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToErrorPage(req, resp);
    }

    private void forwardToErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("errorPage")).forward(req, resp);
    }
}
