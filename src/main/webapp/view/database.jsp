<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="database" style="display:none;">
    <form action="./#dropdatabase/${database}" method="post">
        <input type="hidden" name="databaseName" value=${databaseName}>
        <table border="1" class="container">
            <script template type="text/x-jquery-tmpl">
                    <tr>
                        <td>
                            database name
                        </td>
                                   <td>{{= data}}</td>
                    <c:choose>
                        <c:when test="${currentDatabase == true}">
                            <td>
                            <a href="#tables">
                            tables
                            </a>
                            </td>
                            <td>
                            <a href="truncatedatabase?database=${databaseName}">
                            truncate
                            </a>
                            </td>
                        </c:when>
                    </c:choose>
                 </tr>
            </script>
        </table>
        <br>
        <br>
        <table>
            <tr>
                <c:choose>
                    <c:when test="${currentDatabase != true}">
                        <td>
                            <input type="submit" value="drop database"/>
                        </td>
                    </c:when>
                </c:choose>
                <td>
                    <button onclick="location.href='./#databases'" type="button">
                        Back to Databases
                    </button>
                </td>
            </tr>
        </table>
    </form>
</div>