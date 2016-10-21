package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class TruncateTable extends AbstractCommand {

    public TruncateTable() {
    }

    public TruncateTable(final DatabaseManager manager, final View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String getDescription() {
        return "clear the table";
    }

    @Override
    public String getCommandFormat() {
        return "truncateTable|tableName";
    }


    @Override
    public boolean canProcess(String command) {
        return command.startsWith("truncateTable|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;

        String[] data = command.split("\\|");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Expected command getCommandFormat '" + getCommandFormat() + "', but actual '" + command + "'");
        }

        confirmAndTruncateTable(data[INDEX_TABLE_NAME]);
    }

    private void confirmAndTruncateTable(final String tableName) {
        try {
            view.write(String.format("Do you wish to clear table '%s'. Y/N?", tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.truncateTable(tableName);
                view.write(String.format("Table '%s' cleared successful", tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting table '%s'. Cause: '%s'", tableName, e.getMessage()));
        }
    }

}
