<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <title>SQLCmd</title>
    <script type="text/javascript" src="${ctx}/resources/js/jquery-2.1.4.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/table.js"></script>
    <script type="text/javascript">
        $(window).load(function () {
            initRows('${ctx}');
        });
    </script>
</head>
<body>
<B>
    ROWS OF TABLE ${tableName}
</B>
<br><br>
<form action="${tableName}/insertrow" method="get">
    <input type="hidden" name="table" value=${tableName}>
    <div id="rows">
        <div id="loading">Loading...</div>
        <table border="1" class="container">
            <script template="row" type="text/x-jquery-tmpl">
                    var x = ${0};
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
                                    <button
                                      onclick="location.href='${tableName}/row?id={{= $data[0]}}'" type="button">
                                     edit
                                     </button>
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
    </div>

    <br>
    <br>
    <table>
        <tr>
            <td>
                <input type="submit" value="insert row"/>
            </td>
            <td>
                <button onclick="location.href='../tables'" type="button">
                    Back to Tables
                </button>
            </td>
        </tr>
    </table>


</form>
<%@include file="footer.jsp" %>
</body>
</html>