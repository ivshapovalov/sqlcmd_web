<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQL Web commander</title>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>

    <br>
    <form action="newtable" method="get">
        <table class="table">
            <tr>
                <td>Table name</td>
                <td><label>
                    <input type="text" name="tableName"/>
                </label></td>
            </tr>

            <tr>
                <td>Column count</td>
                <td><label>
                    <input type="number" name="columnCount"/>
                </label></td>
            </tr>

            <tr>
                <td></td>
                <td><input class="btn btn-primary" type="submit" value="OK"/></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
