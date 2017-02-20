<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<html>
<%@include file="header.jsp" %>

    <head>
        <title>SQLCmd</title>
    </head>
    <body>
    <b> MENU </b>
    <section>
        <form class="form-vertical">
            <c:forEach items="${items}" var="item">
                    <div class="row" align="left">
                        <div class="col-lg-6" >
                            <label class="control-label col-xs-1"></label>
                            <div class="col-xs-6" style="padding-bottom: 1%">
                                <button class="btn btn-success" style="width: 50%"
                                        onclick="location.href='${item}'" type="button" class="btn btn-primary">
                                        ${item}
                                </button>
                            </div>
                        </div>
                    </div>
            </c:forEach>
        </form>
    </section>

    </body>
</html>