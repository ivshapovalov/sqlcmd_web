package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropDatabase extends Command {

    @Override
    public String description() {
        return "drop database";
    }

    @Override
    public String format() {
        return "dropDatabase|databaseName";
    }

    public DropDatabase() {
    }

    public DropDatabase(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropDatabase|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'dropDatabase|databaseName', а ты ввел: " + command);
        }

        manager.dropDatabase(data[1]);

        view.write(String.format("База данных %s была успешно очищена.", data[1]));
    }
}
