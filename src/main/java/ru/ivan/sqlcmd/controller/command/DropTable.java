package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropTable extends Command {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    @Override
    public String description() {
        return "drop table ";
    }

    @Override
    public String format() {
        return "dropTable|tableName";
    }

    public DropTable() {
    }

    public DropTable(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'dropTable|tableName', а ты ввел: " + command);
        }
        confirmAndDropTable(data[1]);
    }

    private void confirmAndDropTable(String tableName) {
        try {
            view.write(String.format(ANSI_RED + "Удаляем таблицу '%s'. Y/N?" + ANSI_RESET, tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropTable(tableName);
                view.write(String.format("Таблица %s была успешно удалена.", tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления таблицы '%s', по причине: %s", tableName, e.getMessage()));
        }
    }
}
