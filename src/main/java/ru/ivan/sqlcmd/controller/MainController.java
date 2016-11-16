package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(HttpSession session) {
        session.setAttribute("from-page", "/menu");
        return "redirect:/menu";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help(HttpSession session) {
        session.setAttribute("commands", service.help());
        return "help";
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.GET)
    public String disconnect(HttpSession session) {
        session.removeAttribute("manager");
        return "redirect:/connect";
    }

    @RequestMapping(value = "/databases", method = RequestMethod.GET)
    public String databases(Model model, HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/databases");
            return "redirect:/connect";
        }

        model.addAttribute("databases", service.databases(manager));
        return "databases";
    }

//    @RequestMapping(value = "/connect", method = RequestMethod.GET)
//    public String connect(HttpSession session, Model model) {
//        String page = (String) session.getAttribute("from-page");
//        session.removeAttribute("from-page");
//        model.addAttribute("connection", new Connection(page));
//        if (getManager(session) == null) {
//            return "connect";
//        } else {
//            return "menu";
//        }
//    }

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public String connect(Model model,  @RequestParam(value = "database", required=false)
            String database, HttpSession session) {
        String page = (String) session.getAttribute("from-page");
        session.removeAttribute("from-page");
        if (database != null) {
            model.addAttribute("connection", new Connection(database,page));
            return "connect";
        } else {
            model.addAttribute("connection", new Connection(page));
            if (getManager(session) == null) {
                return "connect";
            } else {
                return "menu";
            }
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connecting(@ModelAttribute("connection") Connection connection,
                             HttpSession session, Model model) {
        try {
            DatabaseManager manager = service.connect(connection.getDbName(),
                    connection.getUserName(), connection.getPassword());
            session.setAttribute("manager", manager);
            return "redirect:" + connection.getFromPage();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/rows", method = RequestMethod.GET)
    public String rows(Model model,
                       @RequestParam(value = "table",required = false) String table,
                       HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows/" + table);
            return "redirect:/connect";
        }
        model.addAttribute("table", service.rows(manager, table));
        return "rows";
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(Model model) {
        model.addAttribute("items", service.getMainMenu());
        return "menu";
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String tables(Model model, HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/tables");
            return "redirect:/connect";
        }

        model.addAttribute("tables", service.tables(manager));
        return "tables";
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("manager");
    }

}
