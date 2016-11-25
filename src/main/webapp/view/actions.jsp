<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="actions">
    <table class="container" border="1">
        <tr>
            <td><b>USER</b></td>
            <td><b>DATABASE</b></td>
            <td><b>ACTION</b></td>
            <TD><b>DATE</b></TD>
        </tr>
        <script template type="text/x-jquery-tmpl">
                <tr>
                    <td>
                            ${'${'}connection.userName}
                    </td>
                    <td>
                            ${'${'}connection.databaseName}
                    </td>
                    <td>
                         ${'${'}action}
                    </td>
                    <td>
                            {{= dateTime}}
                    </td>
                </tr>
        </script>
    </table>
</div>
