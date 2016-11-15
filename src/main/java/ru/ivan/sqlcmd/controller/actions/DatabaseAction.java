package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DatabaseAction extends AbstractAction {

    public DatabaseAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/database");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("database");
        String currentDatabase=getCurrentDatabase(req,resp);

        if (currentDatabase != null) {
            if (currentDatabase.equals(databaseName)) {
                req.setAttribute("currentDatabase", true);
            }
        }
        req.setAttribute("databaseName", databaseName);
        goToJsp(req, resp, "database.jsp");
    }
}
