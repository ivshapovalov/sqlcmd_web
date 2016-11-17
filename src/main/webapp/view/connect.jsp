<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form:form action="connect" method="post" modelAttribute="connection">
    <form:input type="hidden" path="fromPage" id="from-page"/>
    <table>
        <tr>
            <td>Database name</td>
            <td><form:input path="dbName" id="database"/>
                    <%--<c:choose>--%>
                        <%--<c:when test="${not empty database}"> value="${database}"--%>
                        <%--</c:when>--%>
                        <%--<c:otherwise>--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${not empty dbName}"> value="${dbName}"--%>
                                <%--</c:when>--%>
                            <%--</c:choose>--%>
                        <%--</c:otherwise>--%>
                    <%--</c:choose>--%>
            </td>
        </tr>
        <tr>
            <td>User name</td>
            <td><form:input path="userName" id="username" value="${userName}"/></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><form:input path="password" id="password" value="${password}"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="connect" id="connect"/></td>
        </tr>
    </table>
</form:form>
<%@include file="footer.jsp" %>
</body>
</html>