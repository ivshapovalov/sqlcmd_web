<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<html>
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
    .content {
        padding: 15px;
        font-size: 18px;
        min-height: 300px;
        background: #dbdfe5;
        margin-bottom: 10px;
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

    .content.bg-alt {
        background: #abb1b8;
    }
</style>
<head>
    <title>SQL commander</title>
</head>
<body>

<div class="container">
    <!--Row with two equal columns-->
    <div class="row">
        <div class="col-xs-12">
            <div class="header">SQL Commander</div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-6">
            <div class="content"><a href="/console/">Console commander</a>
                <div class="bs-example">
                    <div class="panel-group" id="accordionLeft">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordionLeft"
                                       href="#collapseOneLeft">1. What is it?</a>
                                </h4>
                            </div>
                            <div id="collapseOneLeft" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p>It is a small project to study JDBC operations on SQL DB</p>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordionLeft"
                                       href="#collapseTwoLeft">2. What capabilities?</a>
                                </h4>
                            </div>
                            <div id="collapseTwoLeft" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p>Connect to DB</p>
                                    <p>Tables operations - add, delete ,truncate</p>
                                    <p>Rows operations - CRUD</p>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordionLeft"
                                       href="#collapseThreeLeft">3. What technologies?</a>
                                </h4>
                            </div>
                            <div id="collapseThreeLeft" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p>DB: PostgresQL, JDBC </p>
                                    <p>Lang: Java </p>
                                    <p>UI: Javascript, Bootstrap, CSS</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-6">
            <div class="content bg-alt"><a href="/web/">Web commander</a>
                <div class="bs-example">
                    <div class="panel-group" id="accordionRight">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordionRight"
                                       href="#collapseOneRight">1. What is it?</a>
                                </h4>
                            </div>
                            <div id="collapseOneRight" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p>It is a small project to study WEB tech on SQL DB</p>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordionRight"
                                       href="#collapseTwoRight">2. What capabilities?</a>
                                </h4>
                            </div>
                            <div id="collapseTwoRight" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p>Connect to DB</p>
                                    <p>Tables operations - add, delete ,truncate</p>
                                    <p>Rows operations - CRUD</p>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordionRight"
                                       href="#collapseThreeRight">3. What technologies?</a>
                                </h4>
                            </div>
                            <div id="collapseThreeRight" class="panel-collapse collapse">
                                <div class="panel-body">
                                    <p>DB: PostgresQL</p>
                                    <p>Lang: Java </p>
                                    <p>Spring MVC, Hibernate </p>
                                    <p>UI: Javascript, Bootstrap, CSS</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row" align="center">
        <div class="col-xs-12">
            <img src="/construction.gif" alt="under construction"  height="50"
                 width="200">
        </div>
    </div>
</div>


</body>
</html>