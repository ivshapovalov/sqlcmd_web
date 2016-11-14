package ru.ivan.sqlcmd.controller.actions;


import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class CreateTableAction extends AbstractAction{

    public CreateTableAction(Service service) {
        super(service);
    }
    @Override
    public boolean canProcess(String url) {
        return url.equalsIgnoreCase("/createtable");
    }


    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "createtable.jsp");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {

    }

}
