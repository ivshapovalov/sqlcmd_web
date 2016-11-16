package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            session.setAttribute("db_name",connection.getDbName());
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
        model.addAttribute("tableName", table);
        return "rows";
    }

    @RequestMapping(value = "/opendatabase", method = RequestMethod.GET)
    public String openDatabase(Model model,
                       @RequestParam(value = "database",required = false) String database,
                       HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/databases");
            return "redirect:/connect";
        }
        model.addAttribute("databaseName", database);
        String currentDatabase=(String) session.getAttribute("db_name");;
        if (currentDatabase != null) {
            if (currentDatabase.equals(database)) {
                model.addAttribute("currentDatabase", true);
            }
        }
        return "opendatabase";
    }



    @RequestMapping(value = "/createdatabase", method = {RequestMethod.GET})
    public String createDatabase() {
        return "createdatabase";

    }

    @RequestMapping(value = "/createdatabase", method = {RequestMethod.POST})
    public String createDatabase(Model model,
                               @RequestParam(value = "database",required = true) String database,
                               HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/databases");
            return "redirect:/connect";
        } else {
            try {
                manager.createDatabase(database);
                model.addAttribute("message", "New database created successfully!");
                model.addAttribute("link", "databases");
                model.addAttribute("title", "Back to databases list");
                return "message";
            } catch (Exception e) {
                model.addAttribute("message","Incorrect database name. Try again!");
                return "error";
            }
        }
    }

    @RequestMapping(value = "/dropdatabase", method = {RequestMethod.POST,RequestMethod.GET})
    public String dropDatabase(Model model,
                               @RequestParam(value = "database",required = true) String database,
                               HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/databases");
            return "redirect:/connect";
        } else {
            try {
                manager.dropDatabase(database);
                model.addAttribute("message", String.format("Database '%s' dropped successfully!",
                        database));
                model.addAttribute("link", "databases");
                model.addAttribute("title", "Back to databases list");
                return "message";
            } catch (Exception e) {
                model.addAttribute("message", String.format("Database '%s' cannot be dropped!",
                        database));
                return "error";
            }
        }
    }

    @RequestMapping(value = "/droptable", method = {RequestMethod.GET})
    public String dropTable(Model model,
                               @RequestParam(value = "table",required = true) String table,
                               HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/tables");
            return "redirect:/connect";
        } else {
            try {
                manager.dropTable(table);
                model.addAttribute("message", String.format("Table '%s' dropped successfully!",
                        table));
                model.addAttribute("link", "tables");
                model.addAttribute("title", "Back to tables list");
                return "message";
            } catch (Exception e) {
                model.addAttribute("message", String.format("Table '%s' cannot be dropped!",
                        table));
                return "error";
            }
        }
    }

    @RequestMapping(value = "/truncatetable", method = {RequestMethod.GET})
    public String truncateTable(Model model,
                            @RequestParam(value = "table",required = true) String table,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/tables");
            return "redirect:/connect";
        } else {
            try {
                manager.truncateTable(table);
                model.addAttribute("message", String.format("Table '%s' truncated successfully!",
                        table));
                model.addAttribute("link", "tables");
                model.addAttribute("title", "Back to tables list");
                return "message";
            } catch (Exception e) {
                model.addAttribute("message", String.format("Table '%s' cannot be truncated!",
                        table));
                return "error";
            }
        }
    }

    @RequestMapping(value = "/openrow", method = {RequestMethod.GET})
    public String openRow(Model model,
                            @RequestParam(value = "table",required = true) String table,
                            @RequestParam(value = "id",required = true) int id,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table="+table);
            return "redirect:/connect";
        } else {
            model.addAttribute("tableName", table);
            model.addAttribute("id", id);
            model.addAttribute("table", getRow(manager, table, id));
            return "openrow";
        }
    }

    public List<List<String>> getRow(final DatabaseManager manager, final String tableName, final int id) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        Map<String, Object> tableData = manager.getRow(tableName, id);

        for (String column : columns) {
            List<String> row = new ArrayList<>(2);
            row.add(column);
            row.add(tableData.get(column).toString());
            result.add(row);
        }
        return result;
    }

    @RequestMapping(value = "/deleterow", method = {RequestMethod.GET})
    public String deleteRow(Model model,
                            @RequestParam(value = "table",required = true) String table,
                            @RequestParam(value = "id",required = true) int id,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table="+table);
            return "redirect:/connect";
        } else {
            try {
                manager.deleteRow(table, id);
                model.addAttribute("message", String.format("Row with id='%s' in table='%s' " +
                        "deleted successfully!",id,
                        table));
                model.addAttribute("link", "rows?table=" + table);
                model.addAttribute("title",  String.format("Back to tables '%s' rows ", table));
                return "message";
            } catch (Exception e) {
                model.addAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                        table));
                return "error";
            }
        }
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
