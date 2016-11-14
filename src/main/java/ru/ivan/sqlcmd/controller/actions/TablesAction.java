package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TablesAction extends AbstractAction{

    public TablesAction(Service service) {
        super(service);
    }
    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/tables");
    }


    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        req.setAttribute("tables", service.tables(manager));
        goToJsp(req, resp, "tables.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

    }

}
