package ru.ivan.sqlcmd;

import ru.ivan.sqlcmd.controller.MainController;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.view.Console;

public class Main {

    public static void main(String[] args) {

        MainController controller = new
                MainController(new Console(), new PostgreSQLManager());
        controller.run();
    }
}
