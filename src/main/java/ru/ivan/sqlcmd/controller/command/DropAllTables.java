package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropAllTables extends Command {

    public DropAllTables() {
    }

    public DropAllTables(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "delete all tables of current database";
    }

    @Override
    public String format() {
        return "dropAllTables";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith(format());
    }

    @Override
    public void process(String command) {
        confirmAndDropAllTables();
    }

    private void confirmAndDropAllTables() {
        try {
            view.write("Do you wish to delete all tables? Y/N");
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropAllTables();
                view.write("All tables deleted successfully");
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting all tables. Cause: '%s'", e.getMessage()));
        }
    }
}
