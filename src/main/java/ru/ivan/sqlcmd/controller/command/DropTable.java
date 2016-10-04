package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropTable extends Command {

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
        manager.dropTable(data[1]);
        view.write(String.format("Таблица %s была успешно удалена.", data[1]));
    }
}
