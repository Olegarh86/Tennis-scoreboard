<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:useBean id="currentMatch" scope="request" class="ru.tennis.dto.CurrentMatch"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Current currentMatch</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon1.ico" type="image/x-icon">
</head>
<body>
<%@include file="header.jsp" %>
<main class="container">
    <h1>Current match</h1>
    <div class="current-match-image"></div>
    <div class="score">

        <table class="table">
            <tr class="result th">
                <th>Player</th>
                <th>Sets</th>
                <th>Games</th>
                <th>Points</th>
                <th></th>
            </tr>
            <tr class="player1 td table tr">
                <td class="player1"><c:out value="${currentMatch.firstPlayer.name}"/></td>
                <td><c:out value="${currentMatch.firstPlayer.set}"/></td>
                <td><c:out value="${currentMatch.firstPlayer.game}"/></td>
                <td><c:out value="${currentMatch.firstPlayer.score.toString()}"/></td>
                <td>
                    <form action="${pageContext.request.contextPath}/match-score?uuid=${param.uuid}"
                          method="post">
                        <button class="score-btn table-text"
                                type="submit"
                                name="winner"
                                value="${currentMatch.firstPlayer.id}">
                            score
                        </button>
                    </form>
                </td>
            </tr>
            <tr class="player2 td table tr">
                <td class="player2"><c:out value="${currentMatch.secondPlayer.name}"/></td>
                <td><c:out value="${currentMatch.secondPlayer.set}"/></td>
                <td><c:out value="${currentMatch.secondPlayer.game}"/></td>
                <td><c:out value="${currentMatch.secondPlayer.score.toString()}"/></td>
                <td>
                    <form action="${pageContext.request.contextPath}/match-score?uuid=${param.uuid}"
                          method="post">
                        <button class="score-btn table-text"
                                type="submit" name="winner"
                                value="${currentMatch.secondPlayer.id}">
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