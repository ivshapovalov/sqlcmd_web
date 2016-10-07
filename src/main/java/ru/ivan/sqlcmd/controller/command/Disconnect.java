package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Disconnect extends Command {

    public Disconnect() {
    }

    public Disconnect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "disconnect from curent database";
    }

    @Override
    public String format() {
        return "disconnect";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(format());
    }

    @Override
    public void process(final String command) {
        manager.disconnect();
        view.write("Disconnect successful");
    }

}
