package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class TruncateAllTables extends Command {

    public TruncateAllTables() {
    }

    public TruncateAllTables(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "очистка всех таблиц";
    }

    @Override
    public String format() {
        return "truncateAll";
    }


    @Override
    public boolean canProcess(String command) {
        return command.startsWith(format());
    }

    @Override
    public void process(String command) {

        confirmAndTruncateAllTables();
    }

    private void confirmAndTruncateAllTables() {
        try {
            view.write("Удаляем данные из всех таблиц?. Y/N");
            if (view.read().equalsIgnoreCase("y")) {
                manager.truncateAllTables();
                view.write("Все таблицы были успешно очищены.");
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка очистки всех таблиц по причине: %s", e.getMessage()));
        }
    }
}
