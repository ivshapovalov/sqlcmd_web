package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DropTableAction extends AbstractAction {

    public DropTableAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/droptable");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("manager");
        if (manager != null) {
            String tableName = req.getParameter("table");
            try {
                manager.dropTable(tableName);
                req.setAttribute("message", String.format("Table '%s' dropped successfully!",
                        tableName));
                req.setAttribute("link", "tables");
                req.setAttribute("title", "Back to tables list");
                goToJsp(req, resp, "message.jsp");
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be dropped!", tableName));
                goToJsp(req, resp, "error.jsp");
            }
        } else {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
        }
    }
}
