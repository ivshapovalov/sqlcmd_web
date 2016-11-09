package ru.ivan.sqlcmd.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;

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
            if (manager == null) {
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            }
            return;
        }

        if (manager == null) {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
            return;
        }
        if (action.startsWith("/disconnect")) {
            manager = service.disconnect();
            req.getSession().setAttribute("db_manager", manager);
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
        } else if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.getMainMenu());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.setAttribute("commands", service.help());
            req.getRequestDispatcher("help.jsp").forward(req, resp);

        } else if (action.startsWith("/getRows")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("table", service.getRows(manager, tableName));
            req.getRequestDispatcher("getRows.jsp").forward(req, resp);
        } else if (action.startsWith("/getRow")) {
            String tableName = req.getParameter("table");
            int id = Integer.valueOf(req.getParameter("id"));
            req.setAttribute("tableName", tableName);
            req.setAttribute("id", id);
            req.setAttribute("table", service.getRow(manager, tableName, id));
            req.getRequestDispatcher("getRow.jsp").forward(req, resp);
        } else if (action.startsWith("/deleterow")) {
            String tableName = req.getParameter("table");
            int id = Integer.valueOf(req.getParameter("id"));

            try {
                service.deleteRow(manager,tableName, id);
                req.setAttribute("message", String.format("Row with id='%s' in table='%s' deleted successfully!", id,
                        tableName));
                req.getRequestDispatcher("message.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                        tableName));
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (action.startsWith("/droptable")) {
            String tableName = req.getParameter("table");

            try {
                service.dropTable(manager, tableName);
                req.setAttribute("message", String.format("Table '%s' dropped successfully!",
                        tableName));
                req.getRequestDispatcher("message.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be dropped!", tableName));
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (action.startsWith("/truncatetable")) {
            String tableName = req.getParameter("table");
            try {
                service.truncateTable(manager, tableName);
                req.setAttribute("message", String.format("Table '%s' truncated successfully!",
                        tableName));
                req.getRequestDispatcher("message.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be truncated!", tableName));
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }

        } else if (action.startsWith("/getTables")) {
            req.setAttribute("getTables", service.getTables(manager));
            req.getRequestDispatcher("getTables.jsp").forward(req, resp);

        } else if (action.startsWith("/getDatabases")) {
            req.setAttribute("getDatabases", service.getDatabases(manager));
            req.getRequestDispatcher("getDatabases.jsp").forward(req, resp);

        } else if (action.startsWith("/insertrow")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("columns", service.getTableColumns(manager, tableName));
            req.getRequestDispatcher("insertrow.jsp").forward(req, resp);
        } else if (action.startsWith("/createdatabase")) {
            req.getRequestDispatcher("createdatabase.jsp").forward(req, resp);
        } else if (action.startsWith("/database")) {
            String databaseName = req.getParameter("database");
            req.setAttribute("databaseName", databaseName);
            req.getRequestDispatcher("database.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
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
                DatabaseManager manager = service.connect(databaseName, userName, password);
                req.getSession().setAttribute("db_manager", manager);
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (action.startsWith("/insertrow")) {
            String tableName = req.getParameter("table");
            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                List<String> columnNames = service.getTableColumns(manager, tableName);
                Map<String, Object> row = new HashMap<>();
                for (String columnName : columnNames
                        ) {
                    String parameter = req.getParameter(columnName);
                    row.put(columnName, parameter);
                }
                try {
                    service.insertRow(manager, tableName, row);
                    req.setAttribute("message", "New getRow inserted successfully!");
                    req.getRequestDispatcher("message.jsp").forward(req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            }
        } else if (action.startsWith("/updaterow")) {
            String tableName = req.getParameter("tableName");
            int id = Integer.valueOf(req.getParameter("id"));

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                List<String> columnNames = service.getTableColumns(manager, tableName);
                Map<String, Object> row = new HashMap<>();
                for (String columnName : columnNames
                        ) {
                    String parameter = req.getParameter(columnName);
                    row.put(columnName, parameter);
                }
                row.remove("id");
                try {
                    service.updateRow(manager, tableName, "id", String.valueOf(id), row);
                    req.setAttribute("message", String.format("Row with id='%s' updated successfully!", id));
                    req.getRequestDispatcher("message.jsp").forward(req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            }

        } else if (action.startsWith("/createdatabase")) {
            String databaseName = req.getParameter("database");

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    service.createDatabase(manager,databaseName);
                    req.setAttribute("message", "New database created successfully!");
                    req.getRequestDispatcher("message.jsp").forward(req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect database name. Try again!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            }
        } else if (action.startsWith("/dropdatabase")) {
            String databaseName = req.getParameter("database");

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    service.dropDatabase(manager,databaseName);
                    req.setAttribute("message", String.format("Database '%s' dropped successfully!", databaseName));
                    req.getRequestDispatcher("message.jsp").forward(req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", String.format("Database '%s' cannot be dropped!",
                            databaseName));
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            }
        }
    }

}
