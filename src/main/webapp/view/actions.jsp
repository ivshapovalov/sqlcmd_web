<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<table border="1">
    <tr>
        <td><b>USER</b></td><td><b>DATABASE</b></td><td><b>ACTION</b></td><TD><b>DATE</b></TD>
    </tr>
    <c:forEach items="${actions}" var="userAction">
        <tr>
            <td>
                    ${userAction.connection.userName}
            </td>
            <td>
                    ${userAction.connection.dbName}
            </td>
            <td>
                    ${userAction.action}
            </td>
            <td>
                    ${userAction.dateTime}
            </td>
        </tr>
    </c:forEach>
</table>
<%@include file="footer.jsp" %>
</body>
</html>