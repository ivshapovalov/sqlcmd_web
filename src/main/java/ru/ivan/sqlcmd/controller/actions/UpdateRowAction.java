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

public class UpdateRowAction extends AbstractAction{

    public UpdateRowAction(Service service) {
        super(service);
    }
    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/updaterow");
    }


    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

        String tableName = req.getParameter("tableName");
        int id = Integer.valueOf(req.getParameter("id"));

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("manager");
        if (manager != null) {
            List<String> columnNames = new LinkedList<>(manager.getTableColumns(tableName));
            Map<String, Object> row = new HashMap<>();
            for (String columnName : columnNames
                    ) {
                String parameter = req.getParameter(columnName);
                row.put(columnName, parameter);
            }
            row.remove("id");
            try {
                manager.updateRow(tableName, "id", String.valueOf(id), row);
                req.setAttribute("message", String.format("Row with id='%s' updated successfully!", id));
                req.setAttribute("link", "rows?table=" + tableName);
                req.setAttribute("title", String.format("Back to table '%s' rows ", tableName));
                goToJsp(req, resp, "message.jsp");
            } catch (Exception e) {
                req.setAttribute("message", "Incorrect data. Try again!");
                this.forwardToError(req, resp, e);
            }
        }

    }
}
