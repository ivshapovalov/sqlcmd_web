<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<B>
    ROWS OF TABLE ${tableName}
</B>
<br><br>
<form action="insertrow" method="get">
    <input type="hidden" name="table" value=${tableName}>
    <table border="1">
        <c:set var="x" value="${0}"></c:set>
        <c:forEach items="${table}" var="row">
            <tr>
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
                <c:choose>
                    <c:when test="${x == 0}">
                        <td><b>EDIT</b><br></td>
                        <td><b>DELETE</b><br></td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <button
                                    onclick="location.href='openrow?table=${tableName}&id=${row[0]}'" type="button">
                                edit
                            </button>
                        </td>
                        <td>
                            <button onclick="location.href='deleterow?table=${tableName}&id=${row[0]}'" type="button">
                                delete
                            </button>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
            <c:set var="x" value="${x+1}"></c:set>
        </c:forEach>
    </table>
    <br>
    <table>
        <tr>
            <td>
                <input type="submit" value="insert row"/>
            </td>
            <td>
                <button onclick="location.href='tables'" type="button">
                    Back to Tables
                </button>
            </td>
        </tr>
    </table>


</form>
<%@include file="footer.jsp" %>
</body>
</html>