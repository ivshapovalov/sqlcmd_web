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
                    <button onclick="location.href='${item}'" type="button" style="width:100%" >
                            ${item}
                    </button>
            </td></tr>
        </c:forEach>
    </table>
    </body>
</html>