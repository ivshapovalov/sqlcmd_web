package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropAllDatabases extends Command {

    public DropAllDatabases() {
    }

    public DropAllDatabases(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "delete all databases";
    }

    @Override
    public String format() {
        return "dropAllDatabases";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith(format());
    }

    @Override
    public void process(String command) {
        confirmAndDropAllDatabases();
    }

    private void confirmAndDropAllDatabases() {
        view.write("Do you wish to delete all databases? Y/N");
        if (view.read().equalsIgnoreCase("y")) {
            manager.dropAllDatabases();
            view.write("All databases  deleted successfully");
        }
    }
}
