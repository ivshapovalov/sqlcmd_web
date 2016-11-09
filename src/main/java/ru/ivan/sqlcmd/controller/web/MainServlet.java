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
                jsp("connect",req,resp);
            } else {
                redirect("menu",resp);
            }
            return;
        }

        if (manager == null) {
            redirect("connect",resp);
            return;
        }
        if (action.startsWith("/disconnect")) {
            manager = service.disconnect();
            req.getSession().setAttribute("db_manager", manager);
            redirect("connect",resp);
        } else if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.getMainMenu());
            jsp("menu",req, resp);

        } else if (action.startsWith("/help")) {
            req.setAttribute("commands", service.help());
            jsp("help",req, resp);

        } else if (action.startsWith("/rows")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("table", service.rows(manager, tableName));
            jsp("rows",req, resp);
        } else if (action.startsWith("/row")) {
            String tableName = req.getParameter("table");
            int id = Integer.valueOf(req.getParameter("id"));
            req.setAttribute("tableName", tableName);
            req.setAttribute("id", id);
            req.setAttribute("table", service.row(manager, tableName, id));
            jsp("row",req, resp);
        } else if (action.startsWith("/deleterow")) {
            String tableName = req.getParameter("table");
            int id = Integer.valueOf(req.getParameter("id"));

            try {
                service.deleteRow(manager,tableName, id);
                req.setAttribute("message", String.format("Row with id='%s' in table='%s' deleted successfully!", id,
                        tableName));
                jsp("message",req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Row with id='%s' in table='%s' cannot be deleted!", id,
                        tableName));
                jsp("error",req, resp);
            }
        } else if (action.startsWith("/droptable")) {
            String tableName = req.getParameter("table");

            try {
                service.dropTable(manager, tableName);
                req.setAttribute("message", String.format("Table '%s' dropped successfully!",
                        tableName));
                jsp("message",req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be dropped!", tableName));
                jsp("error",req, resp);
            }
        } else if (action.startsWith("/truncatetable")) {
            String tableName = req.getParameter("table");
            try {
                service.truncateTable(manager, tableName);
                req.setAttribute("message", String.format("Table '%s' truncated successfully!",
                        tableName));
                jsp("message",req, resp);
            } catch (Exception e) {
                req.setAttribute("message", String.format("Table '%s' cannot be truncated!", tableName));
                jsp("error",req, resp);
            }

        } else if (action.startsWith("/tables")) {
            req.setAttribute("tables", service.tables(manager));
            jsp("tables",req, resp);

        } else if (action.startsWith("/databases")) {
            req.setAttribute("databases", service.databases(manager));
            jsp("databases",req, resp);

        } else if (action.startsWith("/insertrow")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("columns", service.getTableColumns(manager, tableName));
            jsp("insertrow",req, resp);
        } else if (action.startsWith("/createdatabase")) {
            jsp("createdatabase",req, resp);
        } else if (action.startsWith("/database")) {
            String databaseName = req.getParameter("database");
            req.setAttribute("databaseName", databaseName);
            jsp("database",req, resp);
        } else {
            jsp("error",req, resp);
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
                redirect("menu",resp);
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                jsp("error",req, resp);
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
                    req.setAttribute("message", "New row inserted successfully!");
                    jsp("message",req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    jsp("error",req, resp);
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
                    jsp("message",req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    jsp("error",req, resp);
                }
            }

        } else if (action.startsWith("/createdatabase")) {
            String databaseName = req.getParameter("database");

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    service.createDatabase(manager,databaseName);
                    req.setAttribute("message", "New database created successfully!");
                    jsp("message",req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect database name. Try again!");
                    jsp("error",req, resp);
                }
            }
        } else if (action.startsWith("/dropdatabase")) {
            String databaseName = req.getParameter("database");

            DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (manager != null) {
                try {
                    service.dropDatabase(manager,databaseName);
                    req.setAttribute("message", String.format("Database '%s' dropped successfully!", databaseName));
                    jsp("message",req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", String.format("Database '%s' cannot be dropped!",
                            databaseName));
                    jsp("error",req, resp);
                }
            }
        }
    }
    private void jsp(String jsp, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(jsp + ".jsp").forward(request, response);
    }

    private void redirect(String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(url));
    }
}
