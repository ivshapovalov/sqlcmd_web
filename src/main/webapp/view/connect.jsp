<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="connect" method="post">

    <table>
        <tr>
            <td>Database name</td>

            <td><input type="text" name="database"
                    <c:choose>
                        <c:when test="${not empty database}"> value="${database}"
                        </c:when>
                    </c:choose>
                       id="database"/>
            </td>
        </tr>
        <tr>
            <td>User name</td>
            <td><input type="text" name="username" id="username"/></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" id="password"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="connect" id="connect"/></td>
        </tr>
    </table>
</form>
<%@include file="footer.jsp" %>
</body>
</html>