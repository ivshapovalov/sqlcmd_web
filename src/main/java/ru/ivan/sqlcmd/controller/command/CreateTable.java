package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CreateTable extends Command {
    private final static Integer INDEX_DATABASE_NAME=1;

    public CreateTable() {
    }

    public CreateTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "create new table";
    }

    @Override
    public String format() {
        return "createTable|tableName";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length !=parametersLength(format()) ) {
            throw new IllegalArgumentException("Expected command format 'createTable|tableName(column1,column2,..,columnN), but actual '" + command+"'");
        }
        manager.createTable(data[INDEX_DATABASE_NAME]);
        view.write(String.format("Table '%s' created successfully", data[1]));
    }
}
