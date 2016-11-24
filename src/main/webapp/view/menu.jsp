<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>SQLCmd</title>
    <script type="text/javascript" src="${ctx}/resources/js/jquery-2.1.4.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/menu.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>

</head>
<body>
<div id="menu">
    <div id="loading">Loading...</div>
    <table class="container" border="1">
        <script template="row" type="text/x-jquery-tmpl">
            <tr>
                <td>
                    <button type="button" style="width:100%"
                            onclick="location.href='{{= $data}}'">{{= $data}}
                    </button>
                </td>
            </tr>

        </script>
    </table>
</div>
</body>
</html>

