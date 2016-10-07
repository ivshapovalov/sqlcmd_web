package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropDatabase extends Command {
    private final static Integer INDEX_DATABASE_NAME=1;
    public DropDatabase() {
    }

    public DropDatabase(final DatabaseManager manager,final View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "delete database";
    }

    @Override
    public String format() {
        return "dropDatabase|databaseName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("dropDatabase|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("\\|");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Expected command format '"+format()+"', but actual '" + command+"'");
        }
        confirmAndDropDatabase(data[INDEX_DATABASE_NAME]);
    }

    private void confirmAndDropDatabase(final String databaseName) {
        try {
            view.write(String.format("Do you wish to delete database '%s'. Y/N?", databaseName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropDatabase(databaseName);
                view.write(String.format("Database '%s' deleted successful", databaseName));
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting database '%s'. Cause: '%s'", databaseName, e.getMessage()));
        }
    }
}
