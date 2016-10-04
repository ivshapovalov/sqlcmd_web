package ru.ivan.sqlcmd.controller;

import org.apache.log4j.PropertyConfigurator;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.view.Console;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

    public static void main(String[] args) {
//        String log4jConfPath = "src/main/resources/log4j.properties";
//        PropertyConfigurator.configure(log4jConfPath);

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);

        MainController controller=new
                MainController(new Console(),new PostgreSQLManager());
        controller.run();
    }
}
