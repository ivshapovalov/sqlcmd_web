package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Databases extends Command {
    public Databases() {
    }

    public Databases(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "list of databases";
    }

    @Override
    public String format() {
        return "databases";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.equals(format());
    }

    @Override
    public void process(final String command) {
        view.write("***Existing databases***");
        for (String database : manager.getDatabasesNames()) {
            view.write(database);
        }
    }
}
