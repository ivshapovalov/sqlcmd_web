package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class DatabasesAction extends AbstractAction{

    public DatabasesAction(Service service) {
        super(service);
    }
    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/databases");
    }


    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String currentDatabase=getCurrentDatabase(req,resp);
        if (currentDatabase != null) {
            req.setAttribute("currentDatabase", currentDatabase);
        }
        req.setAttribute("databases", new LinkedList<>(manager.getDatabasesNames()));
        goToJsp(req, resp, "databases.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

    }

}
