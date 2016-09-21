package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.controller.command.*;
import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Arrays;


/**
 * Created by Ivan on 20.09.2016.
 */
public class MainController {

    private final View view;
    private final DatabaseManager manager;
    private final java.util.List<Command> commands;

    public MainController(View view, DatabaseManager manager) {
        this.manager = manager;
        this.view = view;
        this.commands = Arrays.asList(new Exit(view), new Help(view), new List(manager, view),
                new Find(manager, view),new Unsupported(view));
    }

    public void main(String[] args) {
        connectToDB();
    }

    public void run() {
        connectToDB();

        while (true) {
            String input = readCommand();
            for (Command command:commands
                 ) {
                if (command.canProcess(input)) {
                    command.process(input);
                }
                
            }
       }

    }


    private String readCommand() {
        view.write("Введите команду или help для помощи");
        return view.read();
    }

    private void connectToDB() {
        view.write("Привет, юзер");
        while (true) {
            view.write("Введи имя базы данных, имя пользователя и пароль в формате" +
                    " database|user|password");
            try {
                String string = view.read();
                string = "sqlcmd|postgres|postgres";
                String[] data = string.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Количество параметров - " + data.length + ". Ожидается - 3");
                }
                String database = data[0];
                String user = data[1];
                String password = data[2];
                manager.connect(database, user, password);
                view.write("Подключение успешно");
                break;
            } catch (Exception e) {
                printError(e);

            }

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
