<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>SQLCmd</title>
    <script type="text/javascript" src="${ctx}/resources/js/jquery-2.1.4.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/databases.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>

</head>
<body>
<b>DATABASES</b>
<br><br>
<form action="createdatabase" method="get">
    <div id="databases">
        <div id="loading">Loading...</div>
        <table class="container" border="1">
            <script template="row" type="text/x-jquery-tmpl">
                <tr>
                    <td>
                        <a href="opendatabase?database={{= $data}}" style="width:100%">{{= $data}}</a>
                    </td>
                    <td>
                        <button type="button" style="width:100%"
                                onclick="location.href='connect?database={{= $data}}'">
                            connect
                        </button>
                    </td>
                    <td>
                        <button type="button" style="width:100%"
                                onclick="location.href='dropdatabase?database={{= $data}}'">
                            drop
                        </button>
                    </td>
                    <%--<td>--%>
                        <%--<c:choose>--%>
                            <%--<c:when test="${currentDatabase== {{= data}}}">--%>
                                <%--ACTIVE DB--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                                <%--$data--%>
                            <%--</c:otherwise>--%>
                        <%--</c:choose>--%>
                    <%--</td>--%>
                 </tr>
            </script>
        </table>
    </div>
    <BR>
    <input type="submit" value="create new"/>
</form>
<%@include file="footer.jsp" %>
</body>
</html>