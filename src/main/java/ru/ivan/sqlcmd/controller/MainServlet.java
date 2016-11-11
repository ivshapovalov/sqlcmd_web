package ru.ivan.sqlcmd.controller;


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
    public void init(final ServletConfig config) throws ServletException {

        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        String action = getAction(req);

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        String currentDatabase = (String) req.getSession().getAttribute("db_name");

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
            disconnect(req, resp);
        } else if (action.startsWith("/menu") || action.equals("/")) {
            menu(req, resp);

        } else if (action.startsWith("/help")) {
            help(req, resp);

        } else if (action.startsWith("/rows")) {
            rows(manager, req, resp);
        } else if (action.startsWith("/row")) {
            row(manager, req, resp);
        } else if (action.startsWith("/deleterow")) {
            deleteRow(manager, req, resp);
        } else if (action.startsWith("/droptable")) {
            dropTable(manager, req, resp);
        } else if (action.startsWith("/truncatetable")) {
            truncateTable(manager, req, resp);
        } else if (action.startsWith("/truncatedatabase")) {
            truncateDatabase(manager, req, resp);
        } else if (action.startsWith("/tables")) {
            tables(manager, req, resp);
        } else if (action.startsWith("/databases")) {
            databases(manager,currentDatabase, req, resp);
        } else if (action.startsWith("/createtable")) {
            jsp("createtable", req, resp);
        } else if (action.startsWith("/newtable")) {
            newTable(req, resp);
        } else if (action.startsWith("/insertrow")) {
            insertRow(manager, req, resp);
        } else if (action.startsWith("/createdatabase")) {
            jsp("createdatabase", req, resp);
        } else if (action.startsWith("/database")) {
            database(manager, currentDatabase, req, resp);
        } else if (action.startsWith("/dropdatabase")) {
            dropDatabase(req, resp);
        } else {
            jsp("error", req, resp);
        }
    }

    private void dropDatabase(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
            IOException {
        DatabaseManager manager;
        String databaseName = req.getParameter("database");
        manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        if (manager != null) {
            try {
                manager.dropDatabase(databaseName);
                req.setAttribute("message", String.format("Database '%s' dropped successfully!", databaseName));
                req.setAttribute("link", "databases");
                req.setAttribute("title", "Back to databases list");
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Database '%s' cannot be dropped!",
                        databaseName));
                jsp("error", req, resp);
            }
        }
    }

    private void database(final DatabaseManager manager,final String currentDatabase,final HttpServletRequest req,final HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("database");

        if (currentDatabase != null) {
            if (currentDatabase.equals(databaseName)) {
                req.setAttribute("currentDatabase", true);
            }
        }
        req.setAttribute("databaseName", databaseName);
        jsp("database", req, resp);
    }

    private void insertRow(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp) throws
            ServletException, IOException {
        String tableName = req.getParameter("table");
        req.setAttribute("tableName", tableName);
        req.setAttribute("columns", new LinkedList<>(manager.getTableColumns(tableName)));
        jsp("insertrow", req, resp);
    }

    private void newTable(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));
        req.setAttribute("tableName", tableName);
        req.setAttribute("columnCount", columnCount);
        jsp("newtable", req, resp);
    }

    private void databases(final DatabaseManager manager,final String currentDatabase,final HttpServletRequest req,
                           final HttpServletResponse resp) throws
            ServletException, IOException {

        if (currentDatabase != null) {
                req.setAttribute("currentDatabase", currentDatabase);
        }
        req.setAttribute("databases", new LinkedList<>(manager.getDatabasesNames()));
        jsp("databases", req, resp);
    }

    private void tables(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp) throws
            ServletException, IOException {
        req.setAttribute("tables", connectionService.tables(manager));
        jsp("tables", req, resp);
    }

    private void truncateTable(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp)
            throws ServletException, IOException {
        String tableName = req.getParameter("table");
        try {
            manager.truncateTable(tableName);
            req.setAttribute("message", String.format("Table '%s' truncated successfully!",
                    tableName));
            req.setAttribute("link", "tables");
            req.setAttribute("title", "Back to tables list");
            jsp("message", req, resp);
        } catch (Exception e) {
            req.setAttribute("message", String.format("Table '%s' cannot be truncated!", tableName));
            jsp("error", req, resp);
        }
    }

    private void truncateDatabase(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp)
            throws
            ServletException, IOException {
        String databaseName = req.getParameter("database");
        try {
            manager.truncateAllTables();
            req.setAttribute("message", String.format("All tables in database '%s' truncated successfully!",
                    databaseName));
            req.setAttribute("link", "databases");
            req.setAttribute("title", "Back to databases list");
            jsp("message", req, resp);
        } catch (Exception e) {
            req.setAttribute("message", String.format("Database '%s' cannot be truncated!", databaseName));
            jsp("error", req, resp);
        }
    }

    private void dropTable(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp) throws
            ServletException, IOException {
        String tableName = req.getParameter("table");

        try {
            manager.dropTable(tableName);
            req.setAttribute("message", String.format("Table '%s' dropped successfully!",
                    tableName));
            req.setAttribute("link", "tables");
            req.setAttribute("title", "Back to tables list");
            jsp("message", req, resp);
        } catch (Exception e) {
            req.setAttribute("message", String.format("Table '%s' cannot be dropped!", tableName));
            jsp("error", req, resp);
        }
    }

    private void deleteRow(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp) throws
            ServletException, IOException {
        String tableName = req.getParameter("table");
        int id = Integer.valueOf(req.getParameter("id"));

        try {
            manager.deleteRow(tableName, id);
            req.setAttribute("message", String.format("Row with id='%s' in table='%s' deleted successfully!", id,
                    tableName));
            req.setAttribute("link", "rows?table=" + tableName);
            req.setAttribute("title", String.format("Back to tables '%s' rows ", tableName));
            jsp("message", req, resp);
        } catch (Exception e) {
            req.setAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                    tableName));
            jsp("error", req, resp);
        }
    }

    private void row(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp) throws
            ServletException, IOException {
        String tableName = req.getParameter("table");
        int id = Integer.valueOf(req.getParameter("id"));
        req.setAttribute("tableName", tableName);
        req.setAttribute("id", id);
        req.setAttribute("table", getRow(manager, tableName, id));
        jsp("row", req, resp);
    }

    private void rows(final DatabaseManager manager,final HttpServletRequest req,final HttpServletResponse resp) throws
            ServletException, IOException {
        String tableName = req.getParameter("table");
        req.setAttribute("tableName", tableName);
        req.setAttribute("table", connectionService.rows(manager, tableName));
        jsp("rows", req, resp);
    }

    private void help(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("commands", help());
        jsp("help", req, resp);
    }

    private void menu(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
            IOException {
        req.setAttribute("items", getMainMenu());
        jsp("menu", req, resp);
    }

    private void disconnect(final HttpServletRequest req,final HttpServletResponse resp) throws IOException {
        req.getSession().setAttribute("db_manager", null);
        req.getSession().setAttribute("db_name", null);
        redirect("connect", resp);
    }

    private String getAction(final HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.startsWith("/connect")) {
            connect(req, resp);
        } else if (action.startsWith("/insertrow")) {
            saveNewRow(req, resp);
        } else if (action.startsWith("/newtable")) {
            saveNewTable(req, resp);
        } else if (action.startsWith("/updaterow")) {
            updateRow(req, resp);
        } else if (action.startsWith("/createdatabase")) {
            createDatabase(req, resp);
        } else if (action.startsWith("/dropdatabase")) {
            dropDatabase(req, resp);
        }
    }

    private void saveNewRow(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
            IOException {
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
                req.setAttribute("title", String.format("Back to table '%s' rows ", tableName));
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", "Incorrect data. Try again!");
                jsp("error", req, resp);
            }
        }
    }

    private void saveNewTable(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
            IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));
        String keyName = req.getParameter("keyName");

        Map<String, Object> columnParameters = new HashMap<>();
        for (int index = 1; index < columnCount; index++) {
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
                req.setAttribute("title", "Back to tables list");
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' not created. Try again!", tableName));
                jsp("error", req, resp);
            }
        }
    }

    private void updateRow(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
            IOException {
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
                req.setAttribute("title", String.format("Back to table '%s' rows ", tableName));
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", "Incorrect data. Try again!");
                jsp("error", req, resp);
            }
        }
    }

    private void createDatabase(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
            IOException {
        String databaseName = req.getParameter("database");

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        if (manager != null) {
            try {
                manager.createDatabase(databaseName);
                req.setAttribute("message", "New database created successfully!");
                req.setAttribute("link", "databases");
                req.setAttribute("title", "Back to databases list");
                jsp("message", req, resp);
            } catch (Exception e) {
                req.setAttribute("message", "Incorrect database name. Try again!");
                jsp("error", req, resp);
            }
        }
    }

    private void connect(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            DatabaseManager manager = connectionService.connect(databaseName, userName, password);
            req.getSession().setAttribute("db_manager", manager);
            req.getSession().setAttribute("db_name", databaseName);
            redirect("menu", resp);
        } catch (Exception e) {
            req.setAttribute("message", e.getMessage());
            jsp("error", req, resp);
        }
    }

    private String getParameters(final Map<String, Object> columnParameters) {
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
        List<List<String>> actions = new ArrayList<>();

        actions.add(Arrays.asList("connect", "connect to database"));
        actions.add(Arrays.asList("disconnect", "disconnect from database"));
        actions.add(Arrays.asList("tables", "list of tables"));
        actions.add(Arrays.asList("databases", "list of databases"));
        actions.add(Arrays.asList("create database", "create new database with specific name"));
        actions.add(Arrays.asList("drop database", "drop selected database"));
        actions.add(Arrays.asList("create table ", "create new table with selected number of columns"));
        actions.add(Arrays.asList("truncate table", "truncate selected table"));
        actions.add(Arrays.asList("drop table", "drop selected table"));
        actions.add(Arrays.asList("rows", "rows of selected table"));
        actions.add(Arrays.asList("insert row", "insert new row in selected table"));
        actions.add(Arrays.asList("update row", "update selected row in table"));
        actions.add(Arrays.asList("delete row", "delete selected row in table"));
        return actions;
    }

    public List<List<String>> getRow(final DatabaseManager manager,final  String tableName,final int id) {
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

    private void jsp(final String jsp,final HttpServletRequest request,final HttpServletResponse response) throws
            ServletException, IOException {
        request.getRequestDispatcher(jsp + ".jsp").forward(request, response);
    }

    private void redirect(final String url,final HttpServletResponse response) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(url));
    }
}
