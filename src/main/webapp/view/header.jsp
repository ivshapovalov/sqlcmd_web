<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--<link href="/resources/favicon.ico" rel="shortcut icon" type="image/x-icon">--%>


<head>
    <title>SQLCmd</title>
    <br>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
          crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <style type="text/css">
        .bs-example {
            margin: 20px;
        }

        .li.my_active {
            color: #ffffff;
            background-color: #5CB85C;
            background-image: linear-gradient(to bottom, #087508, #419641);
            border-color: #3E8F3E #3E8F3E #3E8F3E;
        }

        .li.my_active:hover {
            color: #ffffff;
            background-color: #419641;
            background-image: linear-gradient(to bottom, #419641, #419641);
            border-color: #3E8F3E #3E8F3E #3E8F3E;
        }

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

        .main_menu {
            margin: 0 auto; /* центрирование обертки! меню */
            overflow: hidden; /* прячем пункты, выходящие за область обертки */
            position: relative; /* относительное позиционирование */
        }

        .main_menu ul {
            float: left; /* прижимаем меню к левому краю обертки */
            position: relative; /* относительное позиционирование */
            left: 50%; /* сдвигаем меню вправо */
        }

        .main_menu ul li {
            float: left; /* выстраиваем пункты меню по горизонтали */
            position: relative; /* относительное позиционирование */
            right: 50%; /* сдвигаем каждый пункт влево */
        }
    </style>

</head>

<div class="row">
    <div class="col-xs-12">
        <div class="header">SQL WEB commander</div>
    </div>
</div>
<div class="main_menu">

    <ul class="nav nav-pills">
        <li class="active"><a href="/">Home</a></li>
        <li class="active"><a href="/web/help">Help</a></li>
        <c:choose>
            <c:when test="${manager==null}">
                <li class="active"><a href="/web/connect">Connect</a>
                </li>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${manager!=null}">
                <li class="active"><a href="/web/tables">Tables</a>
                </li>
            </c:when>
        </c:choose>
        <c:choose>
            <c:when test="${manager!=null}">
                <li class="active"><a href="/web/actions">Actions</a>
                </li>
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${manager!=null}">
                <li class="active"><a href="/web/disconnect">Disconnect<span
                        class="badge">${user}</span></a>
                </li>
            </c:when>
        </c:choose>

    </ul>
</div>



