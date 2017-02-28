<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQL Web commander</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <h2> ROW OF TABLE ${tableName} </h2>
    <form action="update" method="post">
        <input type="hidden" name="tableName" value=${tableName}>
        <input type="hidden" name="id" value=${id}>
        <table border="1" class="table">
            <c:forEach items="${table}" var="column">
                <tr>
                    <td>
                            ${column[0]}
                    </td>
                    <td>
                        <c:choose>
                        <c:when test="${column[0] == 'id'}">
                        <input type="text" name=${column[0]} value=${column[1]} readonly="readonly">
                    </td>
                    </c:when>
                    <c:otherwise>
                        <input type="text" name=${column[0]} value=${column[1]}> </td>
                    </c:otherwise>
                    </c:choose>

                </tr>
            </c:forEach>
        </table>
        <br>
        <input class="btn btn-primary" type="submit" value="update row"/>

    </form>
</div>
<%@include file="footer.jsp" %>
</body>
</html>