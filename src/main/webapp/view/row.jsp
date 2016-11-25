<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="row">

    <B>
        ROW OF TABLE ${tableName}
    </B>
    <br><br>

    <form action="updaterow" method="post">
        <input type="hidden" name="tableName" value=${tableName}>
        <input type="hidden" name="id" value=${id}>
        <table border="1" class="container">
            <script template="row" type="text/x-jquery-tmpl">
                <tr>
                    <td>
                             {{= $data[0]}}
                    </td>
                    <td>

                        <%--<c:choose>--%>
                            <%--<c:when test="${fn:startsWith('{{= $data[0]}}', 'id')}">--%>
                                <%--&lt;%&ndash;<input type="text" name= {{= $data[0]}} value= {{= $data[1]}} readonly="readonly">&ndash;%&gt;--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                                <input type="text" name= {{= $data[0]}} value= {{= $data[1]}}> </td>
                            <%--</c:otherwise>--%>
                        <%--</c:choose>--%>
                    </td>

                </tr>

            </script>
        </table>
        <br>
        <input type="submit" value="update row"/>
    </form>
</div>