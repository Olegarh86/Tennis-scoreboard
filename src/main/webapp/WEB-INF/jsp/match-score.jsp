<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="player1" scope="application" class="ru.tennis.model.Player"/>
<jsp:useBean id="player2" scope="application" class="ru.tennis.model.Player"/>
<jsp:useBean id="currentMatch" scope="request" class="ru.tennis.CurrentMatch"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Current currentMatch</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}css/style.css"/>
</head>
<body>
<%@include file="header.jsp" %>
<main class="container">
    <h1>
        <%= "Current match" %>
    </h1>
    <div class="current-match-image"></div>
    <div class="score">
        <table class="table">
            <tr class="result th">
                <th>
                    Player
                </th>
                <th>
                    Sets
                </th>
                <th>
                    Games
                </th>
                <th>
                    Points
                </th>
                <th>

                </th>
            </tr>
            <tr class="player1 td table tr">
                <td class="player1">
                    <c:out value="${player1.name}" />
                </td>
                <td>
                    <c:out value="${requestScope.currentMatch.set['1']}" />
                </td>
                <td>
                    <c:out value="${requestScope.currentMatch.game['1']}" />
                </td>
                <td>
                    <c:out value="${requestScope.currentMatch.score['1']}" />
                </td>
                <td>
                    <form action="match-score?uuid=${param.uuid}"
                          method="post">
                        <button type="submit"
                                name="winner"
                                value="${applicationScope.player1.id}"
                                class="score-btn table-text">
                            score
                        </button>
                    </form>
                </td>
            </tr>
            <tr class="player2 td table tr">
                <td class="player2">
                    <c:out value="${player2.name}" />
                </td>
                <td>
                    <c:out value="${requestScope.currentMatch.set['2']}" />
                </td>
                <td>
                    <c:out value="${requestScope.currentMatch.game['2']}" />
                </td>
                <td>
                    <c:out value="${requestScope.currentMatch.score['2']}" />
                </td>
                <td>
                    <form action="match-score?uuid=${param.uuid}"
                          method="post">
                        <button type="submit"
                                name="winner"
                                value="${applicationScope.player2.id}"
                                class="score-btn table-text">
                            score
                        </button>
                    </form>
                </td>
            </tr>
        </table>
    </div>
</main>
<%@include file="footer.jsp" %>
</body>
</html>