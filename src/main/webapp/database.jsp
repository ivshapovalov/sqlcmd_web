<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="dropdatabase" method="post">
    <input type="hidden" name="database" value=${databaseName} />
    <table border="1">
        <tr>
            <td>
                database name
            </td>
            <td>${databaseName}</td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="drop database"/></td>
        </tr>
    </table>
</form>
<%@include file="footer.jsp" %>
</body>
</html>