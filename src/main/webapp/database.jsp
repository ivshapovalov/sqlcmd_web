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
    </table>
    <br>
    <input type="submit" value="drop database"/>
</form>
<%@include file="footer.jsp" %>
</body>
</html>