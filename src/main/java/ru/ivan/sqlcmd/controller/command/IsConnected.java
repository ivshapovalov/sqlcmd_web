package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class IsConnected extends AbstractCommand  {

    public IsConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String format() {
        return "";
    }

    @Override
    public boolean canProcess(final String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(final String command) {
        view.write(String.format("You can't use '%s'. First connect with the command 'connect|database|user|password'", command));

    }

}
