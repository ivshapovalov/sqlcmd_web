<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd Tables</title>
</head>
<body>
<table border="1">
    <c:forEach items="${tables}" var="table">
        <tr>

            <td>
                <a href="rows?table=${table}">${table}</a><br>

            </td>

        </tr>
    </c:forEach>
</table>
<a href="menu">menu</a><br>
</body>
</html>