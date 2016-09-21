package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Connect implements Command {

    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {

            try {
                String[] data = command.split("[|]");

                if (data.length != 4) {
                    throw new IllegalArgumentException("Количество параметров - " + data.length + ". Ожидается - 4");
                }
                String database = data[1];
                String user = data[2];
                String password = data[3];
                manager.connect(database, user, password);
                view.write("Подключение успешно");
            } catch (Exception e) {
                printError(e);
            }
    }


    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message = message + " " + e.getCause().getMessage();
        }
        view.write("Неудача по причине: " + message);
        view.write("Повтори попытку");
    }
}
