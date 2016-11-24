package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.entity.Description;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RestService {

    @Autowired
    private Service service;

    @RequestMapping(value = "/menu/content", method = RequestMethod.GET)
    public List<String> menuItems() {
        DatabaseManager manager = null;
        return service.getMainMenu(manager);
    }

    @RequestMapping(value = "/help/content", method = RequestMethod.GET)
    public List<Description> helpItems() {
        DatabaseManager manager = null;
        return service.help(manager);
    }

    @RequestMapping(value = "/tables/content", method = RequestMethod.GET)
    public List<String> tablesItems(HttpSession session) {
        DatabaseManager manager = getManager(session);
        return service.tables(manager);
    }

    @RequestMapping(value = "/databases/content", method = RequestMethod.GET)
    public List<String> databasesItems(HttpSession session) {
        DatabaseManager manager = getManager(session);
        return service.databases(manager);
    }

    @RequestMapping(value = "/table/{table}/content", method = RequestMethod.GET)
    public List<List<String>> rowsItems(@PathVariable(value = "table") String tableName,
                                  HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            return new LinkedList<>();
        }
        session.setAttribute("tableName",tableName);
        return service.rows(manager,tableName);
    }


    @RequestMapping(value = "/connected", method = RequestMethod.GET)
    public boolean isConnected(HttpServletRequest request) {
        DatabaseManager manager = getManager(request.getSession());
        return manager != null;
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("manager");
    }

}
