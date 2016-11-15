package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConnectAction extends AbstractAction {

    public ConnectAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/connect");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String databaseName = req.getParameter("database");
        if (databaseName != null) {
            req.setAttribute("database", databaseName);

        }
        goToJsp(req, resp, "connect.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {
        String databaseName = req.getParameter("database");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            DatabaseManager manager = service.connect(databaseName, userName, password);
            req.getSession().setAttribute("manager", manager);
            req.getSession().setAttribute("db_name", databaseName);
            resp.sendRedirect(resp.encodeRedirectURL("menu"));
        } catch (Exception e) {
            forwardToError(req, resp, e);
        }
    }
}
