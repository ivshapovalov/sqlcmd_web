package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.entity.Description;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpSession;
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
        DatabaseManager manager = (DatabaseManager) session.getAttribute("manager");
        return service.tables(manager);
    }


}
