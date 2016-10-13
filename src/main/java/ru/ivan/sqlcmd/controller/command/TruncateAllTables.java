package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class TruncateAllTables extends AbstractCommand {

    public TruncateAllTables() {
    }

    public TruncateAllTables(final DatabaseManager manager, final View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "clear all tables";
    }

    @Override
    public String format() {
        return "truncateAll";
    }


    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(format());
    }

    @Override
    public void process(final String command) {

        confirmAndTruncateAllTables();
    }

    private void confirmAndTruncateAllTables() {
        try {
            view.write("Do you wish to clear all tables?. Y/N");
            if (view.read().equalsIgnoreCase("y")) {
                manager.truncateAllTables();
                view.write("All tables cleared successfully");
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting all tables. Cause: '%s'", e.getMessage()));
        }
    }
}
