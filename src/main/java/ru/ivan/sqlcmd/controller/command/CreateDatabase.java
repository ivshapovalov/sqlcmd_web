package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class CreateDatabase extends Command {

    public CreateDatabase() {
    }

    public CreateDatabase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "создание новой базы данных";
    }

    @Override
    public String format() {
        return "createDatabase|databaseName";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createDatabase|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Формат команды '"+format()+"', а ты ввел: " + command);
        }
        manager.createDatabase(data[1]);

        view.write(String.format("База данных %s была успешно создана.", data[1]));
    }
}
