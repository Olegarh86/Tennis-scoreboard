<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>New match</title>
    <link rel="stylesheet"
          type="text/css"
          href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon1.ico" type="image/x-icon">
</head>
<body>
<%@include file="header.jsp" %>
<main class="container">
    <h1>Start new match</h1>

    <div class="new-match-image"></div>

    <form class="form-container center"
          action="${pageContext.request.contextPath}/new-match"
          method="post">

        <div style="color: darkred">
            <c:if test="${not empty requestScope.errors}">
                <c:forEach var="errors" items="${requestScope.errors}">
                    <li>${errors}</li>
                </c:forEach>
            </c:if>
        </div>

        <p class="label-player">Имя игрока 1 </p>

        <label>
            <input class="input-player"
                   type="text"
                   name="Имя игрока 1"
                   placeholder="Введите имя"
                   value="${requestScope.playerName1}">
        </label>

        <p class="label-player">Имя игрока 2 </p>

        <label>
            <input class="input-player"
                   type="text"
                   name="Имя игрока 2"
                   placeholder="Введите имя"
                   value="${requestScope.playerName2}">
        </label>

        <button class="form-button">
            начать
        </button>
    </form>
</main>

<%@include file="footer.jsp" %>
</body>
</html>
