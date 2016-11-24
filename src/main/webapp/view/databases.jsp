<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="databases">
    <form action="createdatabase" method="get">
        <table class="container" border="1">
            <script template type="text/x-jquery-tmpl">
                <tr>
                    <td>
                        <a href="opendatabase?database={{= $data}}" style="width:100%">{{= $data}}</a>
                    </td>
                    <td>
                        <button type="button" style="width:100%"
                                onclick="location.href='connect?database={{= $data}}'">
                            connect
                        </button>
                    </td>
                    <td>
                        <button type="button" style="width:100%"
                                onclick="location.href='dropdatabase?database={{= $data}}'">
                            drop
                        </button>
                    </td>
                    <%--<td>--%>
                        <%--<c:choose>--%>
                            <%--<c:when test="${currentDatabase== {{= data}}}">--%>
                                <%--ACTIVE DB--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                                <%--$data--%>
                            <%--</c:otherwise>--%>
                        <%--</c:choose>--%>
                    <%--</td>--%>
                 </tr>

            </script>
        </table>
        <BR>
        <input type="submit" value="create new"/>
    </form>
</div>
