package ru.ivan.sqlcmd.controller.web;


import ru.ivan.sqlcmd.model.DatabaseManager;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (action.startsWith("/connect")) {
            if (manager == null) {
                req.getRequestDispatcher("qwe.html").include(req, resp);
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            }
            return;
        }

//        if (manager == null) {
//            resp.sendRedirect(resp.encodeRedirectURL("connect"));
//            return;
//        }

        if (action.startsWith("/menu") || action.equals("/")) {
           // req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);

        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
        }
//        } else if (action.startsWith("/find")) {
//            String tableName = req.getParameter("table");
//            //req.setAttribute("table", service.find(manager, tableName));
//            req.getRequestDispatcher("find.jsp").forward(req, resp);
//
//        } else {
//            req.getRequestDispatcher("error.jsp").forward(req, resp);
//        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = getAction(req);
//
//        if (action.startsWith("/connect")) {
//            String databaseName = req.getParameter("dbname");
//            String userName = req.getParameter("username");
//            String password = req.getParameter("password");
//
//            try {
//                DatabaseManager manager = service.connect(databaseName, userName, password);
//                req.getSession().setAttribute("db_manager", manager);
//                resp.sendRedirect(resp.encodeRedirectURL("menu"));
//            } catch (Exception e) {
//                req.setAttribute("message", e.getMessage());
//                req.getRequestDispatcher("error.jsp").forward(req, resp);
//            }
//        }
//    }

}