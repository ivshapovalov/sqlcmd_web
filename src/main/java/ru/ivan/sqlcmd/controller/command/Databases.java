package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Set;

public class Databases extends Command {

    @Override
    public String description() {
        return "list all databases";
    }

    @Override
    public String format() {
        return "databases";
    }

    public Databases() {
    }

    public Databases(DatabaseManager manager, View view) {
        this.manager=manager;

        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("databases");
    }

    @Override
    public void process(String command) {
        Set<String> databasesNames = manager.getDatabasesNames();

        String message = databasesNames.toString();

        view.write(message);
    }
}
