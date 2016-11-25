package ru.ivan.sqlcmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.entity.Description;
import ru.ivan.sqlcmd.model.entity.UserAction;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/actions/content", method = RequestMethod.GET)
    public List<UserAction> actionsItems(HttpSession session) {
        DatabaseManager manager = getManager(session);
        if (manager != null) {
            List<UserAction> actions = service.getAllActionsOfUserAndDatabase(manager.getUserName(), manager
                    .getDatabaseName());
            return actions;
        }
        return new ArrayList<UserAction>();
    }

    @RequestMapping(value = "row/{table}/{id}/content", method = {RequestMethod.GET})
    public List<List<String>> openRow(Model model,
                                      @PathVariable("table") String tableName,
                                      @PathVariable("id") int id,
                                      HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            return new LinkedList<>();
        } else {
            session.setAttribute("tableName", tableName);
            session.setAttribute("id", id);

            List<List<String>> row = getRow(manager, tableName, id);
            return row;
        }
    }

    @RequestMapping(value = "/table/{table}/content", method = RequestMethod.GET)
    public List<List<String>> rowsItems(@PathVariable(value = "table") String tableName,
                                        HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            return new LinkedList<>();
        }
        session.setAttribute("tableName", tableName);
        return service.rows(manager, tableName);
    }

    @RequestMapping(value = "/database/{database}/content", method = RequestMethod.GET)
    public String database(@PathVariable(value = "database") String databaseName,
                         HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager != null) {
            String currentDatabase = (String) session.getAttribute("db_name");
            if (currentDatabase != null) {
                if (currentDatabase.equals(databaseName)) {
                    session.setAttribute("currentDatabase", true);
                }
            }
        }
        session.setAttribute("databaseName", databaseName);
        return databaseName;
    }


    public List<List<String>> getRow(final DatabaseManager manager, final String tableName, final int id) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<String> columnsWithType = new LinkedList<>(manager.getTableColumnsWithType(tableName));
        Map<String, Object> tableData = manager.getRow(tableName, id);

        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            String columnWithType = columnsWithType.get(i);
            List<String> row = new ArrayList<>(2);
            row.add(columnWithType);
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

    @RequestMapping(value = "/connected", method = RequestMethod.GET)
    public boolean isConnected(HttpServletRequest request) {
        DatabaseManager manager = getManager(request.getSession());
        return manager != null;
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.GET)
    public void disconnect(HttpSession session) {
        session.removeAttribute("manager");
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("manager");
    }

    @RequestMapping(value = "/connect", method = RequestMethod.PUT)
    public String connecting(Model model, @ModelAttribute("connection") Connection
            connection, HttpSession session) {
        try {
            DatabaseManager manager = service.connect(connection.getDatabaseName(),
                    connection.getUserName(), connection.getPassword());
            session.setAttribute("manager", manager);
            session.setAttribute("db_name", connection.getDatabaseName());

            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/dropdatabase/{database}", method = {RequestMethod.POST, RequestMethod
            .GET})
    public void dropDatabase(Model model,
                             @PathVariable("database") String database,
                             HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager != null) {
            service.dropDatabase(manager, database);
        }
    }
}
