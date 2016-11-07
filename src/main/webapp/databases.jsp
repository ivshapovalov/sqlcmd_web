<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd databases</title>
</head>
<body>
<table border="1">
    <c:forEach items="${databases}" var="database">
        <tr>
            <td>
                ${database}
            </td>
        </tr>
    </c:forEach>
</table>
<a href="menu">menu</a><br>
</body>
</html>