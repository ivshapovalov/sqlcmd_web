package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Set;

public class Tables extends Command {

    @Override
    public String description() {
        return "список таблиц текущей базы данных";
    }

    public Tables() {
    }

    @Override
    public String format() {
        return "tables";
    }

    public Tables(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals(format());
    }

    @Override
    public void process(String command) {
        Set<String> tableNames = manager.getTableNames();

        String message = tableNames.toString();

        view.write(message);
    }
}
