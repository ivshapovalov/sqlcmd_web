package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DisconnectAction extends AbstractAction {

    public DisconnectAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/disconnect");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("manager", null);
        req.getSession().setAttribute("db_name", null);
        resp.sendRedirect(resp.encodeRedirectURL("connect"));
    }

}
