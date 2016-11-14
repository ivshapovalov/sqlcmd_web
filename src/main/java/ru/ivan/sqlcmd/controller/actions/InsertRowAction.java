package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InsertRowAction extends AbstractAction{

    public InsertRowAction(Service service) {
        super(service);
    }
    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/insertrow");
    }


    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String tableName = req.getParameter("table");
        req.setAttribute("tableName", tableName);
        req.setAttribute("columns", new LinkedList<>(manager.getTableColumns(tableName)));
        goToJsp(req, resp, "insertrow.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

        String tableName = req.getParameter("table");
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        if (manager != null) {
            List<String> columnNames = new LinkedList<>(manager.getTableColumns(tableName));
            Map<String, Object> row = new HashMap<>();
            for (String columnName : columnNames
                    ) {
                String parameter = req.getParameter(columnName);
                row.put(columnName, parameter);
            }
            try {
                manager.insertRow(tableName, row);
                req.setAttribute("message", "New row inserted successfully!");
                req.setAttribute("link", "rows?table=" + tableName);
                req.setAttribute("title", String.format("Back to table '%s' rows ", tableName));
                goToJsp(req, resp, "message.jsp");
            } catch (ServletException | IOException e) {
                req.setAttribute("message", "Incorrect data. Try again!");
                this.forwardToError(req, resp, e);
            }

        }
    }
}
