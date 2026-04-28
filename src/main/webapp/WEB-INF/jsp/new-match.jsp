<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>New match</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<%@include file="header.jsp" %>
<main class="container">
    <h1>Start new match</h1>

    <div class="new-match-image"></div>

    <form class="form-container center"
          action="${pageContext.request.contextPath}/new-match"
          method="post">

        <p class="label-player">Имя игрока 1 </p>

        <label>
            <input class="input-player" type="text" name="Имя игрока 1" placeholder="Введите имя">
        </label>

        <p class="label-player">Имя игрока 2 </p>

        <label>
            <input class="input-player" type="text" name="Имя игрока 2" placeholder="Введите имя">
        </label>

        <button class="form-button">начать</button>
    </form>
</main>

<%@include file="footer.jsp" %>
</body>
</html>
