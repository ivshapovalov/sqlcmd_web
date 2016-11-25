<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <head>
        <title>SQLCmd</title>
        <script type="text/javascript" src="${ctx}/resources/js/jquery-2.1.4.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/main.js"></script>
        <script type="text/javascript">
            $(window).load(function(){
                init('${ctx}');
            });
        </script>
    </head>
    <body>
        <div id="loading" style="display:none;">Loading...</div>
        <%@include file="menu.jsp" %>
        <%@include file="connect.jsp" %>
        <%@include file="databases.jsp" %>
        <%@include file="actions.jsp" %>
        <%@include file="tables.jsp" %>
        <%@include file="table.jsp" %>
        <%@include file="row.jsp" %>
        <%@include file="help.jsp" %>
        <%@include file="footer.jsp" %>
    </body>
</html>