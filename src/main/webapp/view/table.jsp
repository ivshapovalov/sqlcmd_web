<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="table">
    <form action="${tableName}/insertrow" method="get">
        <input type="hidden" name="tableName" value=${tableName}>
        <table border="1" class="container">
            <script template type="text/x-jquery-tmpl">
                    <tr>
                        {{each $data}}
                                    <td>{{= this}}</td>
                         {{/each}}
                         <c:choose>
                <c:when test="${x == 0}">
                    <td><b>EDIT</b><br></td>
                    <td><b>DELETE</b><br></td>
                </c:when>
                <c:otherwise>
                    <td>
                    <a href='main#row/${tableName}/{{= $data[0]}}'">
                    edit
                    </a>
                    </td>
                    <td>
                    <button onclick="location.href='../deleterow?table=${tableName}&id={{= $data[0]}}'" type="button">
                    delete
                    </button>
                    </td>
                </c:otherwise>
            </c:choose>
                        <td>${x}</td>

                    </tr>

            </script>
        </table>
        <br>
        <table>
            <tr>
                <td>
                    <input type="submit" value="insert row"/>
                </td>
                <td>
                    <button onclick="location.href='./#tables'" type="button">
                        Back to Tables
                    </button>
                </td>
            </tr>
        </table>


    </form>
</div>