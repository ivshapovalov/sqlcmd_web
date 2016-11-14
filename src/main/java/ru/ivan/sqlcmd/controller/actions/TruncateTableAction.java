package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TruncateTableAction extends AbstractAction {

    public TruncateTableAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/truncatetable");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String tableName = req.getParameter("table");
        try {
            manager.truncateTable(tableName);
            req.setAttribute("message", String.format("Table '%s' truncated successfully!",
                    tableName));
            req.setAttribute("link", "tables");
            req.setAttribute("title", "Back to tables list");
            goToJsp(req, resp, "message.jsp");
        } catch (Exception e) {
            req.setAttribute("message", String.format("Table '%s' cannot be truncated!", tableName));
            goToJsp(req, resp, "error.jsp");
        }
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

    }

}
