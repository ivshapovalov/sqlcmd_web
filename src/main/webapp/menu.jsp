<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
    <b> MENU </b>
    <br><br>
    <table border="1">
        <c:forEach items="${items}" var="item">
            <tr><td>
            <a href="${item}">${item}</a><br>
            </td></tr>
        </c:forEach>
    </table>
    </body>
</html>