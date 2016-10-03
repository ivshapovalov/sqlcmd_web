package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 22.09.2016.
 */
public class TruncateAllTables extends Command {

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

        manager.truncateAllTables();
        view.write("Все таблицы были успешно очищены.");
    }
}
