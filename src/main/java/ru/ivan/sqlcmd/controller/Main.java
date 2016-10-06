package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.view.Console;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);

        MainController controller=new
                MainController(new Console(),new PostgreSQLManager());
        controller.run();
    }
}
