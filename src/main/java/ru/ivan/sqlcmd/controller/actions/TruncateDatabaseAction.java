package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TruncateDatabaseAction extends AbstractAction {

    public TruncateDatabaseAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/truncatedatabase");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String databaseName = req.getParameter("database");
        try {
            manager.truncateAllTables();
            req.setAttribute("message", String.format("All tables in database '%s' truncated successfully!",
                    databaseName));
            req.setAttribute("link", "databases");
            req.setAttribute("title", "Back to databases list");
            goToJsp(req, resp, "message.jsp");
        } catch (Exception e) {
            req.setAttribute("message", String.format("Database '%s' cannot be truncated!", databaseName));
            goToJsp(req, resp, "error.jsp");
        }
    }
}
