<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
    <form action="insertrow" method="get" >
        <input type="hidden" name="tableName" value=${tableName} />
        <table border="1">
            <c:forEach items="${table}" var="row">
                <tr>
                    <td> <a href="row?table=${tableName}&id=${row[0]}">edit</a><br></td>
                    <c:forEach items="${row}" var="element">
                        <td>
                            ${element}
                        </td>
                    </c:forEach>
                </tr>
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