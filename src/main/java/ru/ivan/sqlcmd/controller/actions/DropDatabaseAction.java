package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DropDatabaseAction extends AbstractAction{

    public DropDatabaseAction(Service service) {
        super(service);
    }
    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/createdatabase");
    }


    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dropDatabase(req, resp);
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        dropDatabase(req, resp);
    }

    private void dropDatabase(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        DatabaseManager manager = getManager(req, resp);
        String databaseName = req.getParameter("database");
        if (manager != null) {
            try {
                manager.dropDatabase(databaseName);
                req.setAttribute("message", String.format("Database '%s' dropped successfully!", databaseName));
                req.setAttribute("link", "databases");
                req.setAttribute("title", "Back to databases list");
                goToJsp(req, resp, "message.jsp");
            } catch (Exception e) {
                req.setAttribute("message", String.format("Database '%s' cannot be dropped!",
                        databaseName));
                goToJsp(req, resp, "error.jsp");
            }
        }
    }



}
