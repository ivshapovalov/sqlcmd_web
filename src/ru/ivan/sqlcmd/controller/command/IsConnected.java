package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class IsConnected extends Command {

    private DatabaseManager manager;
    private View view;

    @Override
    public String description() {
        return "";
    }

    @Override
    public String format() {
        return "";
    }

    public IsConnected() {
    }

    public IsConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("Вы не можете пользоваться командой '%s' пока не подлючитесь с помощью команды connect|database|user|password",command));

    }

}
