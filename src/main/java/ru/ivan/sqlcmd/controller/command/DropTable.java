package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropTable extends Command {

    public DropTable() {
    }

    public DropTable(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "удаление таблицы";
    }

    @Override
    public String format() {
        return "dropTable|tableName";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Формат команды '"+format()+"', а ты ввел: " + command);
        }
        confirmAndDropTable(data[1]);
    }

    private void confirmAndDropTable(String tableName) {
        try {
            view.write(String.format("Удаляем таблицу '%s'. Y/N?", tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropTable(tableName);
                view.write(String.format("Таблица %s была успешно удалена.", tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления таблицы '%s', по причине: %s", tableName, e.getMessage()));
        }
    }
}
