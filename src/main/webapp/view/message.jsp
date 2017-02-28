<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQL Web commander</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <br>
    ${message}<br><br>
    <a href="${link}">
        ${title}
    </a>
    <br>
</div>
<%@include file="footer.jsp" %>
</body>
</html>