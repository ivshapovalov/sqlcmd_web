<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<style type="text/css">
    .header {
        padding: 15px;
        text-align: center;
        color: white;
        font-size: 40px;
        min-height: 100px;
        background: #dbdfe5;
        margin-bottom: 10px;

        background: red;
        background: -webkit-linear-gradient(steelblue, gray);
        background: -ms-linear-gradient(darkgray, yellow);
        background: linear-gradient(steelblue, gray);
    }
</style>

<html>
<%@include file="header.jsp" %>

<body>
<div class="container">

    <div class="row">
        <div class="col-xs-12">
            <div class="header">SQL WEB commander</div>
        </div>
    </div>
    <section>
        <form class="form-vertical">
            <c:forEach items="${items}" var="item">
                <div class="row" align="left">
                    <div class="col-lg-6">
                        <%--<label class="control-label col-xs-1"></label>--%>
                        <div class="col-xs-6" style="padding-bottom: 1%">
                            <button class="btn btn-success" style="width: 50%"
                                    onclick="location.href='${item}'" type="button"
                                    class="btn btn-primary">
                                    ${item}
                            </button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </form>
    </section>
</div>
</body>
</html>