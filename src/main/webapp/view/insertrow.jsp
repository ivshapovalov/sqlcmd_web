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
    <form action="new" method="post">
        <table class="table">
            <c:forEach items="${columns}" var="column">
                <tr>
                    <td>
                            ${column}
                    </td>
                    <td><input type="text" name=${column}></td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td><input class="btn btn-primary" type="submit" value="insert row"/></td>
            </tr>

        </table>
    </form>
</div>
<%@include file="footer.jsp" %>
</body>
</html>