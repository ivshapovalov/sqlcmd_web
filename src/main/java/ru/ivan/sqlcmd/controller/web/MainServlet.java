package ru.ivan.sqlcmd.controller.web;


import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.service.Service;
import ru.ivan.sqlcmd.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainServlet extends HttpServlet {

    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();

        service = new ServiceImpl();
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
            req.getSession().setAttribute("db_manager", null);
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
        } else if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.mainMenuList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.setAttribute("commands", service.help());
            req.getRequestDispatcher("help.jsp").forward(req, resp);

        } else if (action.startsWith("/rows")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("table", service.rows(manager, tableName));
            req.getRequestDispatcher("rows.jsp").forward(req, resp);

        } else if (action.startsWith("/tables")) {
            req.setAttribute("tables", service.tables(manager));
            req.getRequestDispatcher("tables.jsp").forward(req, resp);

        } else if (action.startsWith("/databases")) {
            req.setAttribute("databases", service.databases(manager));
            req.getRequestDispatcher("databases.jsp").forward(req, resp);

        } else if (action.startsWith("/insertrow")) {
            String tableName = req.getParameter("table");
            req.setAttribute("tableName", tableName);
            req.setAttribute("columns", service.tableColumns(manager, tableName));
            req.getRequestDispatcher("insertrow.jsp").forward(req, resp);

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

                List<String> columnNames = service.tableColumns(manager, tableName);
                Map<String, Object> row = new HashMap<>();
                for (String columnName : columnNames
                        ) {
                    String parameter = req.getParameter(columnName);
                    row.put(columnName, parameter);
                }
                try {
                    manager.insertRow(tableName, row);
                    req.setAttribute("message", "New row inserted successfully!");
                    req.getRequestDispatcher("message.jsp").forward(req, resp);
                } catch (Exception e) {
                    req.setAttribute("message", "Incorrect data. Try again!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            }


        }
    }

}
