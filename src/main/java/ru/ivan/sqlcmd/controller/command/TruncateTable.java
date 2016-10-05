package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class TruncateTable extends Command {

    public TruncateTable() {
    }

    public TruncateTable(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "delete all rows in table";
    }

    @Override
    public String format() {
        return "truncateTable|tableName";
    }


    @Override
    public boolean canProcess(String command) {
        return command.startsWith("truncateTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'truncateTable|tableName', а ты ввел: " + command);
        }

        confirmAndTruncateTable(data[1]);
    }
    private void confirmAndTruncateTable(String tableName) {
        try {
            view.write(String.format("Удаляем данные с таблицы '%s'. Y/N", tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.truncateTable(tableName);
                view.write(String.format("Таблица %s была успешно очищена.", tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления данных из таблицы '%s', по причине: %s", tableName, e.getMessage()));
        }
    }

}
