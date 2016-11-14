package ru.ivan.sqlcmd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NullAction implements Action {

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) {
        // do nothing
    }

    @Override
    public boolean canProcess(String url) {
        return false;
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {
        // do nothing
    }
}
