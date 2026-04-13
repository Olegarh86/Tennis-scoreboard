<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    <title>Tennis-scoreboard</title>
    <style>
        .new-match-button {
            font-size: 21px;
            color: #fff;
            text-decoration: none;
            text-align: center;
        }
        .matches-button {
            font-size: 21px;
            color: #000;
            text-decoration: none;
            text-align: center;
        }
    </style>
</head>
<body>
<%@include file="WEB-INF/jsp/header.jsp" %>
<div class="container">
        <h1><%= "Welcome to Tennis Scoreboard" %>
        </h1>
        <p> Manage your tennis matches, record results, and track rankings </p>
        <div class="welcome-image"></div>
        <div class="form-container center">
            <a class="start-match homepage-action-button new-match-button" href="new-match">
                <p>Новая игра</p>
            </a>
            <br/>
            <a class="view-results homepage-action-button matches-button" href="matches">
                <p>Результаты</p>
            </a>
        </div>
</div>
<%@include file="WEB-INF/jsp/footer.jsp" %>
</body>
</html>