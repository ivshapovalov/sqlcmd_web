<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="tables">
    <form action="createtable" method="get">
        <table border="1" class="container">
            <script template type="text/x-jquery-tmpl">
                    <tr>
                        <td>
                            <a href="#table/{{= $data}}">{{= $data}}</a>
                        </td>
                        <td>
                            <button type="button"
                                    onclick="location.href='#table/{{= $data}}'">
                                open
                            </button>
                        </td>
                        <td>
                            <button type="button"
                                    onclick="location.href='droptable?table={{= $data}}'">
                                drop
                            </button>
                        </td>
                        <td>
                            <button type="button"
                                    onclick="location.href='truncatetable?table={{= $data}}'">
                                truncate
                            </button>
                        </td>
                    </tr>


            </script>

        </table>
        <br><br>
        <input type="submit" value="create new table"/>
    </form>
</div>
