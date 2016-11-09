package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Set;

public class Tables extends AbstractCommand {

    @Override
    public String getDescription() {
        return "list of getTables in current database";
    }

    public Tables() {
    }

    @Override
    public String getCommandFormat() {
        return "getTables";
    }

    public Tables(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(final String command) {
        return command.equals(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        Set<String> tableNames = manager.getTableNames();

        String message = tableNames.toString();

        view.write(message);
    }
}
