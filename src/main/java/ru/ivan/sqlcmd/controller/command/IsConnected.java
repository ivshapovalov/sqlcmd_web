package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class IsConnected extends Command {

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
