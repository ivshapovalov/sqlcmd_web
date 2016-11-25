<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="menu">
    <table class="container" border="1">
        <script template type="text/x-jquery-tmpl">
            <tr>
                <td>
                    <a href="\#{{= $data}}">{{= $data}}
                    </a>
                </td>
            </tr>
        </script>
    </table>
</div>


