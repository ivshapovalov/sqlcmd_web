<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd Tables</title>
</head>
<body>
<B>TABLES</B>
<br><br>
<form action="createtable" method="get">
    <table border="1">
        <c:forEach items="${tables}" var="table">
            <tr>
                <td>
                    <a href="rows?table=${table[0]}">${table[0]}</a>
                </td>
                <td>
                        ${table[1]} rows
                </td>
                <td>
                    <button onclick="location.href='rows?table=${table[0]}'" type="button">
                        open
                    </button>
                </td>
                <td>
                    <button onclick="location.href='droptable?table=${table[0]}'" type="button">
                        drop
                    </button>
                </td>
                <td>
                    <button onclick="location.href='truncatetable?table=${table[0]}'" type="button">
                        truncate
                    </button>
                </td>
                    <%--<td> <a href="droptable?table=${table[0]}">delete table</a><br></td>--%>
                    <%--<td> <a href="truncatetable?table=${table[0]}">clear table</a><br></td>--%>

            </tr>
        </c:forEach>
    </table>
    <br><br>
    <input type="submit" value="create new table"/>
    <br>


    <%@include file="footer.jsp" %>

</body>
</html>