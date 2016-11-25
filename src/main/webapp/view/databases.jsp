<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="databases">
    <form action="createdatabase" method="get">
        <table class="container" border="1">
            <script template type="text/x-jquery-tmpl">
                <tr>
                    <td>
                        <a href="#database/{{= $data}}" >{{= $data}}</a>
                    </td>
                    <td>
                        <a href="#connect/{{= $data}}">connect</a>
                    </td>
                    <td>
                        <a href="#dropdatabase/{{= $data}}">
                            drop
                        </a>
                    </td>
                 </tr>
            </script>
        </table>
        <BR>
        <input type="submit" value="create new"/>
    </form>
</div>
