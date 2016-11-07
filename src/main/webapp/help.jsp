<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd help</title>
</head>
<body>
<table border="1">
    <c:forEach items="${commands}" var="row">
        <tr>
            <c:forEach items="${row}" var="column">
            <td>
                ${column}
            </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>
<a href="menu">menu</a><br>
</body>
</html>