<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd databases</title>
</head>
<body>
<b>DATABASES</b>
<br><br>
<form action="createdatabase" method="get">
    <table border="1">
        <c:forEach items="${databases}" var="database">
            <tr>
                <td>
                    <a href="database?database=${database}">${database}</a><br>
                </td>

                <td>
                    <button onclick="location.href='dropdatabase?database=${database}'" type="button">
                        delete
                    </button>
                </td>

            </tr>
        </c:forEach>

    </table>
    <BR>
    <input type="submit" value="create new"/>
</form>
<%@include file="footer.jsp" %>
</body>
</html>