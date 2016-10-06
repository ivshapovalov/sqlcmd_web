package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CreateTable extends Command {

    public CreateTable() {
    }

    public CreateTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "создание новой таблицы";
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
            throw new IllegalArgumentException("Формат команды 'createTable|tableName(column1,column2,..,columnN), а ты ввел: " + command);
        }
        manager.createTable(data[1]);
        view.write(String.format("Таблица %s была успешно создана.", data[1]));
    }
}
