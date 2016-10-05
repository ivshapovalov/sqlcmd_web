package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropAllTables extends Command {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

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
            view.write(ANSI_RED + "Удаляем все таблицы? Y/N" + ANSI_RESET);
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropAllTables();
                view.write("Все таблицы были успешно удалены.");
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления всех таблиц по причине: %s", e.getMessage()));
        }
    }
}