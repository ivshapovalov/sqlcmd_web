package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class TruncateAllTables extends Command {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public TruncateAllTables() {
    }

    public TruncateAllTables(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "delete all rows in all tables";
    }

    @Override
    public String format() {
        return "truncateAll";
    }


    @Override
    public boolean canProcess(String command) {
        return command.startsWith("truncateAll");
    }

    @Override
    public void process(String command) {

        confirmAndTruncateAllTables();
    }

    private void confirmAndTruncateAllTables() {
        try {
            view.write(ANSI_RED + "Удаляем данные из всех таблиц?. Y/N" + ANSI_RESET);
            if (view.read().equalsIgnoreCase("y")) {
                manager.truncateAllTables();
                view.write("Все таблицы были успешно очищены.");
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка очистки всех таблиц по причине: %s", e.getMessage()));
        }
    }
}
