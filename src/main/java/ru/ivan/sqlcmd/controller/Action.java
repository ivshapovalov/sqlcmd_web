package ru.ivan.sqlcmd.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Action {
    void get(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;

    boolean canProcess(String url);

    void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
