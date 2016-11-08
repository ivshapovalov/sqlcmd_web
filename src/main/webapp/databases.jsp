<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd databases</title>
</head>
<body>
<form action="createdatabase" method="get">
    <table border="1">
        <c:forEach items="${databases}" var="database">
            <tr>
                <td>
                    <a href="database?database=${database}">${database}</a><br>
                </td>
            </tr>
        </c:forEach>
        <td><input type="submit" value="create new"/></td>
    </table>
</form>
<%@include file="footer.jsp" %>
</body>
</html>