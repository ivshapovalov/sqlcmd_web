<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>SQLCmd</title>
    <script type="text/javascript" src="${ctx}/resources/js/jquery-2.1.4.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/tables.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>

</head>
<body>
<B>TABLES</B>
<br><br>
<form action="createtable" method="get">
    <div id="tables">
        <div id="loading">Loading...</div>
        <table border="1" class="container">
            <script template="row" type="text/x-jquery-tmpl">
                    <tr>
                        <td>
                            <a href="table/{{= $data}}">{{= $data}}</a>
                        </td>
                        <td>
                            <button type="button"
                                    onclick="location.href='table/{{= $data}}'">
                                open
                            </button>
                        </td>
                        <td>
                            <button type="button"
                                    onclick="location.href='droptable?table={{= $data}}'">
                                drop
                            </button>
                        </td>
                        <td>
                            <button type="button"
                                    onclick="location.href='truncatetable?table={{= $data}}'">
                                truncate
                            </button>
                        </td>
                    </tr>

            </script>

        </table>

    </div>
    <br><br>
    <input type="submit" value="create new table"/>
    <br>
    <%@include file="footer.jsp" %>

</body>
</html>