package ru.tennis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.tennis.CurrentMatch;
import ru.tennis.model.Match;
import ru.tennis.model.Player;
import ru.tennis.service.OngoingMatchesService;
import ru.tennis.util.DataBaseUtil;
import ru.tennis.util.JspHelper;

import java.io.IOException;

@WebServlet(name = "new-match", urlPatterns = "/new-match")
public class NewMatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("new-match")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName1 = req.getParameter("Имя игрока 1");
        String playerName2 = req.getParameter("Имя игрока 2");

        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataBaseUtil.addNMatches(session, 21);
            Player player1 = getPlayer(session, playerName1);
            Player player2 = getPlayer(session, playerName2);

            CurrentMatch currentMatch = new CurrentMatch(player1, player2);
            OngoingMatchesService.addMatch(currentMatch);
            transaction.commit();
            resp.sendRedirect("/match-score?uuid=" + currentMatch.uuid);
        }
    }

    private Player getPlayer(Session session, String name) {

        Player player = session.createQuery("select p from Player p where p.name = :name", Player.class)
                .setParameter("name", name)
                .uniqueResult();

        if (player == null) {
            player = Player.builder().name(name).build();
            session.persist(player);
        }

        return player;
    }
}
