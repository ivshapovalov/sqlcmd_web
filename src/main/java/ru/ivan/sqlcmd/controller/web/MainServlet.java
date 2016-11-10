package ru.ivan.sqlcmd.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.ConnectionService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class MainServlet extends HttpServlet {

    @Autowired
    private ConnectionService connectionService;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (action.startsWith("/connect")) {
            String databaseName = req.getParameter("database");
            if (databaseName != null) {
                req.setAttribute("dbname", databaseName);
                jsp("connect", req, resp);
                return;
            }

            if (manager == null) {
                jsp("connect", req, resp);
            } else {
                redirect("menu", resp);
            }
            return;
        }

        if (manager == null) {
            redirect("connect", resp);
            return;
        }
        if (action.startsWith("/disconnect")) {
            req.getSession().setAttribute("db_manager", null);
            redirect("connect", resp);
        } else if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", getMainMenu());
            jsp("menu", req, resp);

        } else if (action.startsWith("/help")) {
            req.setAttribute("commands", help());
            jsp("help", req, resp);

        } else if (action.startsWith("/rows")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("table", rows(manager, tableName));
            jsp("rows", req, resp);
        } else if (action.startsWith("/row")) {
            String tableName = req.getParameter("table");
            int id = Integer.valueOf(req.getParameter("id"));
            req.setAttribute("tableName", tableName);
            req.setAttribute("id", id);
            req.setAttribute("table", row(manager, tableName, id));
            jsp("row", req, resp);
        } else if (action.startsWith("/deleterow")) {
            String tableName = req.getParameter("table");
            int id = Integer.valueOf(req.getParameter("id"));

            try {
                manager.deleteRow(tableName, id);
                req.setAttribute("message", String.format("Row with id='%s' in table='%s' deleted successfully!", id,
                        tableName));
                req.setAttribute("link", "rows?table=" + tableName);
                req.setAttribute("title", String.format("Back to of table '%s' rows ", tableName));
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                        tableName));
                jsp("error", req, resp);
            }
        } else if (action.startsWith("/droptable")) {
            String tableName = req.getParameter("table");

            try {
                manager.dropTable(tableName);
                req.setAttribute("message", String.format("Table '%s' dropped successfully!",
                        tableName));
                req.setAttribute("link", "tables");
                req.setAttribute("title", "Back to of table list");
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be dropped!", tableName));
                jsp("error", req, resp);
            }
        } else if (action.startsWith("/truncatetable")) {
            String tableName = req.getParameter("table");
            try {
                manager.truncateTable(tableName);
                req.setAttribute("message", String.format("Table '%s' truncated successfully!",
                        tableName));
                req.setAttribute("link", "tables");
                req.setAttribute("title", "Back to of tables list ");
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be truncated!", tableName));
                jsp("error", req, resp);
            }

        } else if (action.startsWith("/tables")) {
            req.setAttribute("tables", tables(manager));
            jsp("tables", req, resp);

        } else if (action.startsWith("/databases")) {
            req.setAttribute("databases", new LinkedList<>(manager.getDatabasesNames()));
            jsp("databases", req, resp);
        } else if (action.startsWith("/createtable")) {
            jsp("createtable", req, resp);
        } else if (action.startsWith("/newtable")) {
            String tableName = req.getParameter("tableName");
            int columnCount = Integer.parseInt(req.getParameter("columnCount"));
            req.setAttribute("tableName", tableName);
            req.setAttribute("columnCount", columnCount);
            jsp("newtable", req, resp);
        } else if (action.startsWith("/insertrow")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("columns", new LinkedList<>(manager.getTableColumns(tableName)));
            jsp("insertrow", req, resp);
        } else if (action.startsWith("/createdatabase")) {
            jsp("createdatabase", req, resp);
        } else if (action.startsWith("/database")) {
            String databaseName = req.getParameter("database");
            req.setAttribute("databaseName", databaseName);
            jsp("database", req, resp);
        } else if (action.startsWith("/dropdatabase")) {
            String databaseName = req.getParameter("database");
            manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    manager.dropDatabase(databaseName);
                    req.setAttribute("message", String.format("Database '%s' dropped successfully!", databaseName));
                    req.setAttribute("link", "databases");
                    req.setAttribute("title", "Back to ofdatabases list ");
                    jsp("message", req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", String.format("Database '%s' cannot be dropped!",
                            databaseName));
                    jsp("error", req, resp);
                }
            }

        } else {
            jsp("error", req, resp);
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.startsWith("/connect")) {
            String databaseName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");

            try {
                DatabaseManager manager = connectionService.connect(databaseName, userName, password);
                req.getSession().setAttribute("db_manager", manager);
                redirect("menu", resp);
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                jsp("error", req, resp);
            }
        } else if (action.startsWith("/insertrow")) {
            String tableName = req.getParameter("table");
            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                List<String> columnNames = new LinkedList<>(manager.getTableColumns(tableName));
                Map<String, Object> row = new HashMap<>();
                for (String columnName : columnNames
                        ) {
                    String parameter = req.getParameter(columnName);
                    row.put(columnName, parameter);
                }
                try {
                    manager.insertRow(tableName, row);
                    req.setAttribute("message", "New row inserted successfully!");
                    req.setAttribute("link", "rows?table=" + tableName);
                    req.setAttribute("title", String.format("Back to of table '%s' rows ", tableName));
                    jsp("message", req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    jsp("error", req, resp);
                }
            }
        } else if (action.startsWith("/newtable")) {
            String tableName = req.getParameter("tableName");
            int columnCount = Integer.parseInt(req.getParameter("columnCount"));
            String keyName = req.getParameter("keyName");

            Map<String, Object> columnParameters = new HashMap<>();
            for (int index = 1; index < columnCount; index++) {
                //int jindex = index + 1;
                columnParameters.put(req.getParameter("columnName" + index),
                        req.getParameter("columnType" + index));
            }
            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {

                try {
                    String query = tableName + "(" + keyName + " INT  PRIMARY KEY NOT NULL"
                            + getParameters(columnParameters) + ")";
                    manager.createTable(query);
                    req.setAttribute("message", String.format("Table '%s' created successfully!",
                            tableName));
                    req.setAttribute("link", "tables");
                    req.setAttribute("title", "Back to of tables list");
                    jsp("message", req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", String.format("Table '%s' not created. Try again!", tableName));
                    jsp("error", req, resp);
                }
            }
        } else if (action.startsWith("/updaterow")) {
            String tableName = req.getParameter("tableName");
            int id = Integer.valueOf(req.getParameter("id"));

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                List<String> columnNames = new LinkedList<>(manager.getTableColumns(tableName));
                Map<String, Object> row = new HashMap<>();
                for (String columnName : columnNames
                        ) {
                    String parameter = req.getParameter(columnName);
                    row.put(columnName, parameter);
                }
                row.remove("id");
                try {
                    manager.updateRow(tableName, "id", String.valueOf(id), row);
                    req.setAttribute("message", String.format("Row with id='%s' updated successfully!", id));
                    req.setAttribute("link", "rows?table=" + tableName);
                    req.setAttribute("title", String.format("Back to of table '%s' rows ", tableName));
                    jsp("message", req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    jsp("error", req, resp);
                }
            }

        } else if (action.startsWith("/createdatabase")) {
            String databaseName = req.getParameter("database");

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    manager.createDatabase(databaseName);
                    req.setAttribute("message", "New database created successfully!");
                    req.setAttribute("link", "databases");
                    req.setAttribute("title", "Back to of databases list ");
                    jsp("message", req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect database name. Try again!");
                    jsp("error", req, resp);
                }
            }
        } else if (action.startsWith("/dropdatabase")) {
            String databaseName = req.getParameter("database");

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    manager.dropDatabase(databaseName);
                    req.setAttribute("message", String.format("Database '%s' dropped successfully!", databaseName));
                    req.setAttribute("link", "databases");
                    req.setAttribute("title", "Back to of databases list");
                    jsp("message", req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", String.format("Database '%s' cannot be dropped!",
                            databaseName));
                    jsp("error", req, resp);
                }
            }
        }
    }

    private String getParameters(Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
    }

    public List<String> getMainMenu() {
        return Arrays.asList("help", "connect", "databases", "tables", "disconnect");
    }

    public List<List<String>> help() {
        List<List<String>> commands = new ArrayList<>();

        commands.add(Arrays.asList("connect", "connect to database"));
        commands.add(Arrays.asList("disconnect", "disconnect from database"));
        commands.add(Arrays.asList("tables", "list of tables"));
        commands.add(Arrays.asList("databases", "list of databases"));
        commands.add(Arrays.asList("create database", "create new database with specific name"));
        commands.add(Arrays.asList("drop database", "drop selected database"));
        commands.add(Arrays.asList("create table ", "create new table with selected number of columns"));
        commands.add(Arrays.asList("truncate table", "truncate selected table"));
        commands.add(Arrays.asList("drop table", "drop selected table"));
        commands.add(Arrays.asList("rows", "rows of selected table"));
        commands.add(Arrays.asList("insert row", "insert new row in selected table"));
        commands.add(Arrays.asList("update row", "update selected row in table"));
        commands.add(Arrays.asList("delete row", "delete selected row in table"));

        return commands;
    }

    public List<List<String>> tables(DatabaseManager manager) {
        List<String> tableNames = new LinkedList<>(manager.getTableNames());
        List<List<String>> tablesWithSize = new LinkedList<>();
        for (String tableName : tableNames
                ) {
            List<String> row = new LinkedList<>();
            row.add(tableName);
            row.add(String.valueOf(manager.getTableSize(tableName)));
            tablesWithSize.add(row);
        }
        return tablesWithSize;
    }

    public List<List<String>> rows(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<Map<String, Object>> tableData = manager.getTableRows(tableName);

        result.add(columns);
        for (Map<String, Object> dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns) {
                row.add(dataSet.get(column).toString());
            }
        }
        return result;
    }

    public List<List<String>> row(DatabaseManager manager, String tableName, Integer id) {
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

    private void jsp(String jsp, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(jsp + ".jsp").forward(request, response);
    }

    private void redirect(String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(url));
    }
}
