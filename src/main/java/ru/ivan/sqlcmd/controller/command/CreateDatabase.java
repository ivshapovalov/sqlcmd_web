package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class CreateDatabase extends Command {
    private final static Integer INDEX_DATABASE_NAME = 1;

    public CreateDatabase() {
    }

    public CreateDatabase(final DatabaseManager manager, final View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "create new database";
    }

    @Override
    public String format() {
        return "createDatabase|databaseName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("createDatabase|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("\\|");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Expected command format '" + format() + "', but actual '" + command + "'");
        }
        manager.createDatabase(data[INDEX_DATABASE_NAME]);

        view.write(String.format("Database '%s' created successfully", data[1]));
    }
}
