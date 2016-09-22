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
        this.commands = Arrays.asList(
                new Connect(manager,view),
                new Help(view),
                new Exit(view),
               new IsConnected(manager,view),
                new Create(manager,view),
                new List(manager, view),
                new Clear(manager,view),
                new Find(manager, view),
                new Unsupported(view));
    }


    public void run() {

        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void doWork() {
        view.write("Привет, юзер");

        view.write("Введи имя базы данных, имя пользователя и пароль в формате" +
                " database|user|password");
        while (true) {
            String input = view.read();
            for (Command command:commands
                 ) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }

            }
            view.write("Введите команду или help для помощи");
        }
    }

}
