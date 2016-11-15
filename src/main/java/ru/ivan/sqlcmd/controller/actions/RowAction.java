package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RowAction extends AbstractAction {

    public RowAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/row");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String tableName = req.getParameter("table");
        int id = Integer.valueOf(req.getParameter("id"));
        req.setAttribute("tableName", tableName);
        req.setAttribute("id", id);
        req.setAttribute("table", getRow(manager, tableName, id));
        goToJsp(req, resp,"row.jsp");
    }

    public List<List<String>> getRow(final DatabaseManager manager, final  String tableName, final int id) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        Map<String, Object> tableData = manager.getRow(tableName, id);

        for (String column : columns) {
            List<String> row = new ArrayList<>(2);
            row.add(column);
            row.add(tableData.get(column).toString());
            result.add(row);
        }
        return result;
    }

}
