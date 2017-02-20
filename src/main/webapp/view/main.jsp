<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<html>
<%@include file="header.jsp" %>

<body>

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
        input.value = val;
        form1.submit();
    }
</script>

<b> Console controller </b>
<section>
    <form:form id="form1" name="name1" action="" method="post">
        <input type="hidden" name="commands" value="${commands}"/>
        <form class="form-horizontal">
            <br>
            <div class="row">
                <div class="form-group">
                    <label for="output" class="control-label col-xs-1">Output</label>
                    <div class="col-xs-8" style="height: 50%">
                            <textarea style="height: 100%" path="output" class="form-control"
                                      id="output"
                                      name="output"
                                      placeholder="output" value="${output}">${output} </textarea>
                    </div>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="form-group">
                    <label for="input" class="control-label col-xs-1">Input</label>
                    <div class="col-xs-8">
                            <textarea style="font-family:arial" path="input"
                                      class="form-control"
                                      id="input"
                                      name="input" typeof="submit"
                                      placeholder="input" value="${input}"
                                      onkeydown="if (event.keyCode == 13) { this.form.submit(); return false; }"
                            >${input}</textarea>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group">
                    <div class="col-xs-1">
                        <button type="submit" class="btn btn-success">Execute
                        </button>
                    </div>
                    <div class="col-xs-1">
                        <div class="dropdown">
                            <button class="btn btn-default dropdown-toggle" type="button"
                                    id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
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

</body>
</html>