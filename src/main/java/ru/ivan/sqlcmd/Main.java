package ru.ivan.sqlcmd;

import org.apache.log4j.varia.NullAppender;
import ru.ivan.sqlcmd.controller.MainController;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.view.Console;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

    public static void main(String[] args) {

        Logger.getRootLogger().addAppender(new NullAppender());

        MainController controller=new
                MainController(new Console(),new PostgreSQLManager());
        controller.run();
    }
}
