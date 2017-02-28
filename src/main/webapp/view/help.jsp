<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd help</title>
</head>
<body>
<%@include file="header.jsp" %>

<table border="1" class="table">
    <tr>
        <td >
            <b>COMMAND</b>
        </td>
        <td w>
            <b>DESCRIPTION</b>
        </td>
    </tr>
    <c:forEach items="${commands}" var="row">
        <tr>
            <td >
                    ${row[0]}
            </td>
            <td >
                    ${row[1]}
            </td>
        </tr>
    </c:forEach>
</table>
<body>
<%@include file="footer.jsp" %>
</body>
</html>