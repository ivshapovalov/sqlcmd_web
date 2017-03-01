<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<head>
    <title>SQL Console commander</title>
</head>
<html>

<body>

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
<script type="text/javascript">
    $(document).ready(function () {
        var $output = $('#output');
        var $input = $('#input');
        $output.scrollTop($output[0].scrollHeight);
        $input.focus()
    });

    function changeCommands(val) {
        var input = document.getElementById("input");
        var form1 = document.getElementById("form1");
        input.value = val.substr(val.indexOf("]") + 2);
//        form1.submit();
    }
</script>
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

    .text {
        color: white;
        font-size: 40px;
        background: black;
        height: 100%;
    }
</style>
<div class="container">

    <div class="row">
        <div class="col-xs-12">
            <div class="header">SQL Console commander</div>
        </div>
    </div>
    <section>
        <form:form id="form1" name="name1" action="" method="post">
            <input type="hidden" name="commands" value="${commands}"/>
            <form class="form-horizontal">
                <br>
                <div class="row" style="height: 50%">
                    <div class="form-group">
                            <%--<label for="output" class="control-label col-xs-1">Output</label>--%>
                        <div class="col-xs-12" style="height: 100%; font-family: monospace">
                            <textarea path="output" style="height: 100% ;
                        background-color: black; color: white" class="form-control"
                                      id="output"
                                      name="output" readonly
                                      placeholder="Output" value="${output}">${output} </textarea>
                        </div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                            <%--<label for="input" class="control-label col-xs-1">Input</label>--%>
                        <div class="col-xs-12">
                            <textarea style="font-family:monospace;
                        background-color: black; color: white" path="input"
                                      class="form-control"
                                      id="input"
                                      name="input" typeof="submit"
                                      placeholder="Input command and press Enter key or Execute button " value="${input}"
                                      onkeydown="if (event.keyCode == 13) { this.form.submit(); return false; }"
                            >${input}</textarea>
                        </div>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="form-group">
                        <div class="col-xs-1">
                            <button type="button" class="btn btn-success"
                                    onclick="location.href='/'"> Home
                            </button>
                        </div>
                        <div class="col-xs-1">
                            <button type="submit" class="btn btn-success">Execute
                            </button>
                        </div>
                        <div class="col-xs-1">
                            <div class="dropdown">
                                <button class="btn btn-default dropdown-toggle" type="button"
                                        id="dropdownMenu1" data-toggle="dropdown"
                                        aria-haspopup="true"
                                        aria-expanded="true">
                                    History
                                    <span class="caret"></span>
                                </button>
                                <ul id="commandList" class="dropdown-menu"
                                    aria-labelledby="dropdownMenu">
                                    <c:forEach items="${commandList}" var="command">
                                        <li class="btn-primary" value="${command}"
                                            onclick="changeCommands('${command}')">${command}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <br>
            </form>
        </form:form>
    </section>
</div>

</body>
</html>