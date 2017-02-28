<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<%@include file="header.jsp" %>
<body>

<h2>Press 'Connect' button</h2>

<form:form action="connect" method="post" modelAttribute="connection">
    <form:input type="hidden" path="fromPage" id="from-page"/>
    <table class="table" width="50%" >
        <tr>
            <td width="10%">Database name</td>
            <td ><form:input path="dbName" id="database"/>
            </td>
        </tr>
        <tr>
            <td width="10%">User name</td>
            <td ><form:input path="userName" id="username" value="${userName}"/></td>
        </tr>
        <tr>
            <td width="10%">Password</td>
            <td><form:input path="password" id="password" value="${password}"/></td>
        </tr>
        <tr>
            <td width="10%"></td>
            <td><input class="btn btn-primary" type="submit" value="connect" id="connect"/></td>
        </tr>
    </table>
</form:form>
<%@include file="footer.jsp" %>
</body>
</html>