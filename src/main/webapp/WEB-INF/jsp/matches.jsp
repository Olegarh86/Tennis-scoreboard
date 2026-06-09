<%@ page contentType="text/html;charset=UTF-8" %>
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
               value="${param.page}"/>
        <input class="input-filter"
               type="text"
               name="filter_by_player_name"
               value="${param.filter_by_player_name}"
               placeholder="Find matches by player name"/>

        <button class="btn-filter"
                type="button"
                onclick="window.location='${pageContext.request.contextPath}/matches?page=1'">
            Reset
        </button>
        <button class="btn-filter"
                value="${param.filter_by_player_name}"
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
        <c:forEach var="match" items="${requestScope.matchesDto.allMatches}">
            <tr>
                <td>${match.player1Name}</td>
                <td>${match.player2Name}</td>
                <td><span class="winner-name-td">${match.winnerName}</span></td>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${requestScope.matchesDto.pageCount > 0}">
        <div class="pagination">
            <a class="prev"
               href="matches?page=${param.page - 1}&filter_by_player_name=${param.filter_by_player_name}">
                <c:if test="${param.page > 1}">
                    <
                </c:if>
            </a>

            <div class="num-page">
                <c:forEach var="count" begin="1" end="${requestScope.matchesDto.pageCount}">

                    <c:choose>
                        <c:when test="${requestScope.matchesDto.page > count - 3 and requestScope.matchesDto.page < count + 3}">

                            <c:choose>
                                <c:when test="${requestScope.matchesDto.page == count}">
                                    <a href="matches?page=${count}&filter_by_player_name=${param.filter_by_player_name}">
                                        <span class="current">${count}</span>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="matches?page=${count}&filter_by_player_name=${param.filter_by_player_name}">
                                        ${count}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </c:forEach>
            </div>

            <a class="next"
               href="matches?page=${param.page + 1}&filter_by_player_name=${param.filter_by_player_name}">
                <c:if test="${param.page != requestScope.matchesDto.pageCount}">
                    >
                </c:if>
            </a>
        </div>
    </c:if>
    <%@include file="footer.jsp" %>
</main>
</body>
</html>
