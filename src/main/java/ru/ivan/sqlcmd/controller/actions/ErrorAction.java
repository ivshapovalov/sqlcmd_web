package ru.ivan.sqlcmd.controller.actions;

import ru.ivan.sqlcmd.controller.AbstractAction;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ErrorAction extends AbstractAction {

    public ErrorAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", "Ошибка");
        goToJsp(req, resp, "error.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return true;
    }
}
