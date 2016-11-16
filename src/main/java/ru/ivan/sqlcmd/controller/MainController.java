package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

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
    public String connect(Model model, @RequestParam(value = "database", required = false)
            String database, HttpSession session) {
        String page = (String) session.getAttribute("from-page");
        session.removeAttribute("from-page");
        if (database != null) {
            model.addAttribute("connection", new Connection(database, page));
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
            session.setAttribute("db_name", connection.getDbName());
            return "redirect:" + connection.getFromPage();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/rows", method = RequestMethod.GET)
    public String rows(Model model,
                       @RequestParam(value = "table", required = false) String table,
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
                               @RequestParam(value = "database", required = false) String database,
                               HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/databases");
            return "redirect:/connect";
        }
        model.addAttribute("databaseName", database);
        String currentDatabase = (String) session.getAttribute("db_name");
        ;
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
                                 @RequestParam(value = "database", required = true) String database,
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
                model.addAttribute("message", "Incorrect database name. Try again!");
                return "error";
            }
        }
    }

    @RequestMapping(value = "/dropdatabase", method = {RequestMethod.POST, RequestMethod.GET})
    public String dropDatabase(Model model,
                               @RequestParam(value = "database", required = true) String database,
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

    @RequestMapping(value = "/truncatedatabase", method = {RequestMethod.GET})
    public String truncateDatabase(Model model,
                               @RequestParam(value = "database", required = true) String database,
                               HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/opendatabase?database="+database);
            return "redirect:/connect";
        } else {
            try {
                manager.truncateAllTables();;
                model.addAttribute("message", String.format("Database '%s' truncated successfully!",
                        database));
                model.addAttribute("link", "databases");
                model.addAttribute("title", "Back to databases list");
                return "message";
            } catch (Exception e) {
                model.addAttribute("message", String.format("Database '%s' cannot be truncated!",
                        database));
                return "error";
            }
        }
    }

    @RequestMapping(value = "/droptable", method = {RequestMethod.GET})
    public String dropTable(Model model,
                            @RequestParam(value = "table", required = true) String table,
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
                                @RequestParam(value = "table", required = true) String table,
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
                          @RequestParam(value = "table", required = true) String table,
                          @RequestParam(value = "id", required = true) int id,
                          HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table=" + table);
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
            Object ob = tableData.get(column);
            if (ob != null) {
                row.add(ob.toString());
            } else {
                row.add("");

            }
            result.add(row);
        }
        return result;
    }

    @RequestMapping(value = "/deleterow", method = {RequestMethod.GET})
    public String deleteRow(Model model,
                            @RequestParam(value = "table", required = true) String table,
                            @RequestParam(value = "id", required = true) int id,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table=" + table);
            return "redirect:/connect";
        } else {
            try {
                manager.deleteRow(table, id);
                model.addAttribute("message", String.format("Row with id='%s' in table='%s' " +
                                "deleted successfully!", id,
                        table));
                model.addAttribute("link", "rows?table=" + table);
                model.addAttribute("title", String.format("Back to tables '%s' rows ", table));
                return "message";
            } catch (Exception e) {
                model.addAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                        table));
                return "error";
            }
        }
    }

    @RequestMapping(value = "/updaterow", method = {RequestMethod.POST})
    public String updateRow(Model model,
                            @RequestParam(value = "tableName", required = false) String tableName,
                            @RequestParam(value = "id", required = false) int id,
                            @RequestParam Map<String, Object> allRequestParams,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/openrow?table=" + tableName + "&id=" + id);
            return "redirect:/connect";
        } else {
            try {
                List<String> columnNames = new LinkedList<>(manager.getTableColumns(tableName));
                Map<String, Object> row = new LinkedHashMap<>();
                for (String columnName : columnNames
                        ) {
                    Object parameter = allRequestParams.get(columnName);
                    row.put(columnName, parameter);
                }
                row.remove("id");

                manager.updateRow(tableName, "id", String.valueOf(id), row);
                model.addAttribute("message", String.format("Row with id='%s' updated " +
                        "successfully!", id));
                model.addAttribute("link", "rows?table=" + tableName);
                model.addAttribute("title", String.format("Back to table '%s' rows ",
                        tableName));
                return "message";

            } catch (Exception e) {
                model.addAttribute("message", "Incorrect data. Try again!");
                return "error";
            }
        }
    }

    @RequestMapping(value = "/insertrow", method = {RequestMethod.GET})
    public String insertRow(Model model,
                            @RequestParam(value = "table", required = false) String tableName,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table=" + tableName);
            return "redirect:/connect";
        } else {
            model.addAttribute("tableName", tableName);
            model.addAttribute("columns", new LinkedList<>(manager.getTableColumnsWithType
                    (tableName)));
            return "insertrow";
        }
    }

    @RequestMapping(value = "/insertrow", method = {RequestMethod.POST})
    public String insertRow(Model model,
                            @RequestParam(value = "table", required = false) String tableName,
                            @RequestParam Map<String, Object> allRequestParams,
                            HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table=" + tableName);
            return "redirect:/connect";
        } else {
            try {
                List<String> columnNames = new LinkedList<>(manager.getTableColumns(tableName));
                Map<String, Object> row = new LinkedHashMap<>();
                for (String columnName : columnNames
                        ) {
                    Object parameter = allRequestParams.get(columnName);
                    row.put(columnName, parameter);
                }
                manager.insertRow(tableName, row);
                model.addAttribute("message", "New row inserted successfully!");
                model.addAttribute("link", "rows?table=" + tableName);
                model.addAttribute("title", String.format("Back to table '%s' rows ",
                        tableName));
                return "message";

            } catch (Exception e) {
                model.addAttribute("message", "Incorrect data. Try again!");
                return "error";
            }
        }
    }

    @RequestMapping(value = "/createtable", method = {RequestMethod.GET})
    public String createTable() {
        return "createtable";
    }

    @RequestMapping(value = "/newtable", method = {RequestMethod.GET})
    public String newTable(Model model,
                           @RequestParam(value = "tableName", required = false) String tableName,
                           @RequestParam(value = "columnCount", required = true, defaultValue = "1")
                                   int columnCount,
                           HttpSession session) {

        if (columnCount < 1) {
            model.addAttribute("message", String.format("Column count must be greater than 1, but" +
                            " actual %s",
                    columnCount));
            return "error";
        }

        model.addAttribute("tableName", tableName);
        model.addAttribute("columnCount", columnCount);
        return "newtable";
    }

    @RequestMapping(value = "/newtable", method = {RequestMethod.POST})
    public String newTable(Model model,
                           @RequestParam(value = "tableName", required = false) String tableName,
                           @RequestParam(value = "columnCount", required = true, defaultValue = "1")
                                   int columnCount,
                           @RequestParam(value = "keyName", required = false) String keyName,
                           @RequestParam Map<String, Object> allRequestParams,
                           HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/rows?table=" + tableName);
            return "redirect:/connect";
        } else {
            try {
                Map<String, Object> columnParameters = new LinkedHashMap<>();
                for (int index = 1; index < columnCount; index++) {
                    columnParameters.put((String) allRequestParams.get("columnName" + index),
                            allRequestParams.get("columnType" + index));
                }
                String query = tableName + "(" + keyName + " INT PRIMARY KEY NOT NULL"
                        + getParameters(columnParameters) + ")";
                manager.createTable(query);
                model.addAttribute("message", String.format("Table '%s' created successfully!",
                        tableName));
                model.addAttribute("link", "tables");
                model.addAttribute("title", "Back to tables list");
                return "message";

            } catch (Exception e) {
                model.addAttribute("message", String.format("Table '%s' not created. Try again!",
                        tableName));
                return "error";
            }
        }
    }

    private String getParameters(final Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
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
