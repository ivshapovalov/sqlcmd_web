package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteRowAction extends AbstractAction {

    public DeleteRowAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/deleterow");
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager manager = getManager(req, resp);
        String tableName = req.getParameter("table");
        int id = Integer.valueOf(req.getParameter("id"));
        try {
            manager.deleteRow(tableName, id);
            req.setAttribute("message", String.format("Row with id='%s' in table='%s' deleted successfully!", id,
                    tableName));
            req.setAttribute("link", "rows?table=" + tableName);
            req.setAttribute("title", String.format("Back to tables '%s' rows ", tableName));
            goToJsp(req, resp,"message.jsp");
        } catch (Exception e) {
            req.setAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                    tableName));
            goToJsp(req, resp, "error.jsp");
        }
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

    }

}
