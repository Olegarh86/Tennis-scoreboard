<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/favicon1.ico" type="image/x-icon">
</head>
<body>
<%@include file="header.jsp" %>
<main class="container">
    <h1>Error page</h1>
    <div class="error-image"></div>
        <div style="color: darkred">
            <li>Status code: ${requestScope.status_code}</li>
            <li>Error message: ${requestScope.message}</li>
        </div>

</main>
<%@include file="footer.jsp" %>
</body>
</html>
