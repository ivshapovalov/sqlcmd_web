package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.model.JDBCDatabaseManager;
import ru.ivan.sqlcmd.view.Console;

/**
 * Created by Ivan on 20.09.2016.
 */
public class Main {
    public static void main(String[] args) {
        MainController controller=new MainController(new Console(),new JDBCDatabaseManager());
        controller.run();
    }
}
