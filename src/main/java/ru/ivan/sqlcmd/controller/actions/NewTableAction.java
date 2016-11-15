package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewTableAction extends AbstractAction {

    public NewTableAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/newtable");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));
        req.setAttribute("tableName", tableName);
        req.setAttribute("columnCount", columnCount);
        goToJsp(req, resp, "newtable.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));
        String keyName = req.getParameter("keyName");

        Map<String, Object> columnParameters = new HashMap<>();
        for (int index = 1; index < columnCount; index++) {
            columnParameters.put(req.getParameter("columnName" + index),
                    req.getParameter("columnType" + index));
        }
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("manager");
        if (manager != null) {

            try {
                String query = tableName + "(" + keyName + " INT  PRIMARY KEY NOT NULL"
                        + getParameters(columnParameters) + ")";
                manager.createTable(query);
                req.setAttribute("message", String.format("Table '%s' created successfully!",
                        tableName));
                req.setAttribute("link", "tables");
                req.setAttribute("title", "Back to tables list");
                goToJsp(req, resp, "message.jsp");
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' not created. Try again!", tableName));
                this.forwardToError(req, resp, e);
            }
        }
    }

    private String getParameters(final Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
    }
}
