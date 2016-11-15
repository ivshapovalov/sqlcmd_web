package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateDatabaseAction extends AbstractAction {

    public CreateDatabaseAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/createdatabase");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "createdatabase.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {
        String databaseName = req.getParameter("database");

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("manager");
        if (manager != null) {
            try {
                manager.createDatabase(databaseName);
                req.setAttribute("message", "New database created successfully!");
                req.setAttribute("link", "databases");
                req.setAttribute("title", "Back to databases list");
                goToJsp(req, resp, "message.jsp");
            } catch (Exception e) {
                req.setAttribute("message", "Incorrect database name. Try again!");
                this.forwardToError(req, resp, e);
            }
        }
    }

}
