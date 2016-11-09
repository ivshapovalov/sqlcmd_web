<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="insertrow" method="get">
    <input type="hidden" name="table" value=${tableName}>
    <table border="1">
        <c:set var="x" value="${0}"></c:set>
        <c:forEach items="${table}" var="row">
            <tr>
                <c:choose>
                    <c:when test="${x == 0}">
                        <td><b>EDIT</b><br></td>
                        <td><b>DELETE</b><br></td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="row?table=${tableName}&id=${row[0]}">edit</a><br></td>
                        <td><a href="deleterow?table=${tableName}&id=${row[0]}">delete</a><br></td>
                    </c:otherwise>
                </c:choose>
                <c:forEach items="${row}" var="element">
                    <c:choose>
                        <c:when test="${x == 0}">
                            <td><b>${element}</b></td>
                        </c:when>
                        <c:otherwise>
                            <td>${element}</td>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </tr>
            <c:set var="x" value="${x+1}"></c:set>
        </c:forEach>
        <tr>
            <td></td>
            <td><input type="submit" value="insert row"/></td>
        </tr>
    </table>
</form>
<a href="tables">tables</a><br>
<%@include file="footer.jsp" %>
</body>
</html>