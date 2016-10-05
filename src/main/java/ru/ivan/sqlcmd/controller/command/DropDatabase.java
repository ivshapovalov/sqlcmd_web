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
        confirmAndDropDatabase(data[1]);
    }

    private void confirmAndDropDatabase(String databaseName) {
        try {
            view.write(String.format("Удаляем базу данных '%s'. Y/N?", databaseName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropDatabase(databaseName);
                view.write(String.format("База данных %s была успешно удалена.", databaseName));
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления базы данных '%s', по причине: %s", databaseName, e.getMessage()));
        }
    }
}
