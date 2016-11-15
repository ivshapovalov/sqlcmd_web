package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class DatabasesAction extends AbstractAction {

    public DatabasesAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/databases");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("manager");
        if (manager != null) {
            String currentDatabase = getCurrentDatabase(req, resp);
            if (currentDatabase != null) {
                req.setAttribute("currentDatabase", currentDatabase);
            }
            req.setAttribute("databases", new LinkedList<>(manager.getDatabasesNames()));
            goToJsp(req, resp, "databases.jsp");
        } else {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
        }
    }
}
