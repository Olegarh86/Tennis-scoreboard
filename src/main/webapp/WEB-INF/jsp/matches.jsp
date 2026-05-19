<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Matches</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon1.ico" type="image/x-icon">
</head>
<body>
<%@include file="header.jsp" %>
<main class="container">
    <h1>Matches</h1>

    <form class="input-container"
          action="${pageContext.request.contextPath}/matches"
          method="get">

        <input type="hidden"
               name="page"
               value="${requestScope.page}"/>
        <c:choose>
            <c:when test="${fn:length(requestScope.filter_by_player_name) > 0}">
                <input class="input-filter"
                       type="text"
                       name="filter_by_player_name"
                       value="${requestScope.filter_by_player_name}"
                       placeholder="Find matches by player name"/>
            </c:when>
            <c:otherwise>
                <input class="input-filter"
                       type="text"
                       name="filter_by_player_name"
                       value="${requestScope.filter_by_player_name}"
                       placeholder="Find matches by player name"/>
            </c:otherwise>
        </c:choose>

        <button class="btn-filter"
                type="submit">
            Find
        </button>
    </form>

    <table class="table-matches">
        <tr>
            <th>Player One</th>
            <th>Player Two</th>
            <th>Winner</th>
        </tr>
        <c:forEach var="allMatches" items="${requestScope.allMatches}">
            <tr>
                <td>${allMatches.player1.name}</td>
                <td>${allMatches.player2.name}</td>
                <td><span class="winner-name-td">${allMatches.winner.name}</span></td>
            </tr>
        </c:forEach>
    </table>
    <div class="pagination">

        <a class="prev"
           href="matches?page=${requestScope.page - 1}&filter_by_player_name=${requestScope.filter_by_player_name}">
            <
        </a>

        <div class="num-page">
            <c:forEach var="count" begin="1" end="${requestScope.pageCount}">

                <c:choose>
                    <c:when test="${requestScope.page == count}">
                        <span class="current">${count}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="matches?page=${count}&filter_by_player_name=${requestScope.filter_by_player_name}">
                                ${count}
                        </a>
                    </c:otherwise>
                </c:choose>

            </c:forEach>
        </div>

        <a class="next"
           href="matches?page=${requestScope.page + 1}&filter_by_player_name=${requestScope.filter_by_player_name}">
            >
        </a>
    </div>
    <%@include file="footer.jsp" %>
</main>
</body>
</html>
