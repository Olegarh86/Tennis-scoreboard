package ru.tennis.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tennis.exception.*;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (IncorrectParameterException | InvalidWinnerIdException e) {
            res.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (MatchNotFoundException e) {
            res.sendError(SC_NOT_FOUND, e.getMessage());
        } catch (SaveFinishedMatchException | CreateNewMatchException | GetMatchesException | DataAccessException e) {
            res.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

