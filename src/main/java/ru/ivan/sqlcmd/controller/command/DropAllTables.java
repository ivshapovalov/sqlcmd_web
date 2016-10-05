package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropAllTables extends Command {

    @Override
    public String description() {
        return "drop all tables";
    }

    @Override
    public String format() {
        return "dropAllTables";
    }

    public DropAllTables() {
    }

    public DropAllTables(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropAllTables");
    }

    @Override
    public void process(String command) {

        confirmAndDropAllTables();
    }

    private void confirmAndDropAllTables() {
        try {
            view.write("Удаляем все таблицы? Y/N" );
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropAllTables();
                view.write("Все таблицы были успешно удалены.");
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления всех таблиц по причине: %s", e.getMessage()));
        }
    }
}
