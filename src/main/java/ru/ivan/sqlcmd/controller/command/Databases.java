package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Databases extends AbstractCommand {
    public Databases() {
    }

    public Databases(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String getDescription() {
        return "list of databases";
    }

    @Override
    public String getCommandFormat() {
        return "databases";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.equals(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        view.write("***Existing databases***");
        for (String database : manager.getDatabasesNames()) {
            view.write(database);
        }
    }
}
