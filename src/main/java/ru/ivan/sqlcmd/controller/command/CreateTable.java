package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class CreateTable extends AbstractCommand implements Command {
    private final static Integer INDEX_DATABASE_NAME = 1;

    CreateTable() {
    }

    public CreateTable(final DatabaseManager manager, final View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "create new table";
    }

    @Override
    public String format() {
        return "createTable|tableName(column1 type, column2 type,...columnN type)";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("createTable|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("\\|");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Expected command format '"+format()+"', but actual '" + command + "'");
        }
        manager.createTable(data[INDEX_DATABASE_NAME]);
        view.write(String.format("Table '%s' created successfully", data[1]));
    }
}
