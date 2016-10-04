package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class CreateDatabase extends Command {

    @Override
    public String description() {
        return "create new database";
    }

    @Override
    public String format() {
        return "createDatabase|databaseName";
    }

    public CreateDatabase() {
    }

    public CreateDatabase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createDatabase|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'createDatabase|databaseName', а ты ввел: " + command);
        }
        manager.createDatabase(data[1]);

        view.write(String.format("База данных %s была успешно создана.", data[1]));
    }
}
