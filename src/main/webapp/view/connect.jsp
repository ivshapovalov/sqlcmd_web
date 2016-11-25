<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="connecting-form">
    <table>
        <tr>
            <td>Database name</td>
            <td><input type="text" name="database" id="database"/></td>
        </tr>
        <tr>
            <td>User name</td>
            <td><input type="text" name="userName" id="username"/></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" id="password"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="connect" id="connect"/></td>
            <td><span id="error"></span></td>
        </tr>
    </table>
</div>