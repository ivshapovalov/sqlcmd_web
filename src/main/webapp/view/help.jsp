<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="help" style="display:none;">
    Существующие команды:<br>
    <table border="1" id="commands" class="container">
        <script template type="text/x-jquery-tmpl">
                <tr>
                    <td>{{= command}}</td>
                    <td>{{= description}}</td>
                </tr>

        </script>
    </table>
</div>
