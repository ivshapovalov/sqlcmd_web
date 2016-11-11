<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<B>
    DATABASE ${databaseName}
</B>
<br><br>
<form action="dropdatabase" method="post">
    <input type="hidden" name="database" value=${databaseName}>
    <table border="1">
        <tr>
            <td>
                database name
            </td>
            <td>${databaseName}</td>
            <c:choose>
                <c:when test="${currentDatabase == true}">
                    <td>
                        <button onclick="location.href='tables'" type="button">
                            tables
                        </button>
                    </td>
                    <td>
                        <button onclick="location.href='truncatedatabase?database=${databaseName}'" type="button">
                            truncate
                        </button>
                    </td>
                </c:when>
            </c:choose>
        </tr>
    </table>
    <br>
    <br>
    <table>
        <tr>
            <c:choose>
                <c:when test="${currentDatabase != true}">
                    <td>
                        <input type="submit" value="drop database"/>
                    </td>
                </c:when>
            </c:choose>
            <td>
                <button onclick="location.href='databases'" type="button">
                    Back to Databases
                </button>
            </td>
        </tr>
    </table>
</form>
<%@include file="footer.jsp" %>
</body>
</html>