<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQL Web commander</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <br>
    <table border="1" class="table">
        <tr>
            <td>
                <b>COMMAND</b>
            </td>
            <td w>
                <b>DESCRIPTION</b>
            </td>
        </tr>
        <c:forEach items="${commands}" var="row">
            <tr>
                <td>
                        ${row[0]}
                </td>
                <td>
                        ${row[1]}
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<body>
<%@include file="footer.jsp" %>
</body>
</html>