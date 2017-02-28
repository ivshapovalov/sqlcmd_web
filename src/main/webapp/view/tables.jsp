<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQL Web commander</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>
    <h2> TABLES </h2>
    <form action="createtable" method="get">
        <table border="1" class="table">
            <c:forEach items="${tables}" var="table">
                <tr>
                    <td>
                        <a href="table/${table[0]}/rows">${table[0]}</a>
                    </td>
                    <td>
                            ${table[1]} rows
                    </td>
                    <td>
                        <button class="btn btn-primary"
                                onclick="location.href='table/${table[0]}/rows'" type="button">
                            open
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-primary"
                                onclick="location.href='droptable?table=${table[0]}'" type="button">
                            drop
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-primary"
                                onclick="location.href='truncatetable?table=${table[0]}'"
                                type="button">
                            truncate
                        </button>
                    </td>
                        <%--<td> <a href="droptable?table=${table[0]}">delete table</a><br></td>--%>
                        <%--<td> <a href="truncatetable?table=${table[0]}">clear table</a><br></td>--%>

                </tr>
            </c:forEach>
        </table>
        <br><br>
        <input class="btn btn-primary" type="submit" value="New table"/>
        <br>
    </form>
    <%@include file="footer.jsp" %>
</body>
</html>