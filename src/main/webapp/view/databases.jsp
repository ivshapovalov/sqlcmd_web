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
        <div class="container">
            <row-template style="display:none;">

                <a href="opendatabase?database={{= $data}}" style="width:20%">{{= $data}}</a>

                <button type="button" style="width:20%"
                        onclick="location.href='connect?database={{= $data}}'">
                    connect
                </button>

                <button type="button" style="width:20%"
                        onclick="location.href='dropdatabase?database={{= $data}}'">
                    drop
                </button>

                <%--<c:choose>--%>
                    <%--<c:when test="${currentDatabase== {{$data}}}">--%>
                        <%--ACTIVE DB--%>
                    <%--</c:when>--%>
                    <%--<c:otherwise>--%>
                        <%--$data--%>
                    <%--</c:otherwise>--%>
                <%--</c:choose>--%>
                <br>

            </row-template>
        </div>

    </div>
    <BR>
    <input type="submit" value="create new"/>
</form>
<%@include file="footer.jsp" %>
</body>
</html>