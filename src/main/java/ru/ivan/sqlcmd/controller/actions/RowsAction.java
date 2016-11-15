package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RowsAction extends AbstractAction {

    public RowsAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/rows");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String tableName = req.getParameter("table");
        req.setAttribute("tableName", tableName);
        req.setAttribute("table", service.rows(manager, tableName));
        goToJsp(req, resp,"rows.jsp");    }

}
