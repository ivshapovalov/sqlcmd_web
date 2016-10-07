package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropTable extends Command {
    private final static Integer INDEX_TABLE_NAME=1;
    public DropTable() {
    }

    public DropTable(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "delete table";
    }

    @Override
    public String format() {
        return "dropTable|tableName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("dropTable|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("\\|");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Expected command format '"+format()+"', but actual '" + command+"'");
        }
        confirmAndDropTable(data[INDEX_TABLE_NAME]);
    }

    private void confirmAndDropTable(final String tableName) {
        try {
            view.write(String.format("Do you wish to delete table '%s'. Y/N?", tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropTable(tableName);
                view.write(String.format("Table '%s' deleted successful", tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting table '%s'. Cause: '%s'", tableName, e.getMessage()));
        }
    }
}
