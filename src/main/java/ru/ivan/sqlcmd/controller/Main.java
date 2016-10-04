package ru.ivan.sqlcmd.controller;

import org.apache.log4j.PropertyConfigurator;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.view.Console;

public class Main {

    public static void main(String[] args) {
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        MainController controller=new
                MainController(new Console(),new PostgreSQLManager());
        controller.run();
    }
}
