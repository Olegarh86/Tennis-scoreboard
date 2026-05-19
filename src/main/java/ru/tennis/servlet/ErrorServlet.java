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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = "0";
        String message = "Unknown error";
        if (req.getAttribute("jakarta.servlet.error.status_code") != null) {
            code = req.getAttribute("jakarta.servlet.error.status_code").toString();
        }
        if (req.getAttribute("jakarta.servlet.error.message") != null) {
            message = req.getAttribute("jakarta.servlet.error.message").toString();
        }
        req.setAttribute("status_code", code);
        req.setAttribute("message", message);
        req.getRequestDispatcher(JspHelper.getPath("errorPage")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("errorPage.jsp")).forward(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
