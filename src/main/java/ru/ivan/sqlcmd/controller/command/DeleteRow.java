package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DeleteRow extends AbstractCommand implements Command {
    private final static Integer INDEX_TABLE_NAME = 1;
    private final static Integer INDEX_DELETING_ROW_ID = 2;

    public DeleteRow() {
    }

    public DeleteRow(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "delete from table row with specific ID ";
    }

    @Override
    public String format() {
        return "deleteRow|tableName|ID";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("deleteRow|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("[|]");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Expected command format '" + format() + "', but actual '" + command + "'");
        }
        String tableName = data[INDEX_TABLE_NAME];
        int id;
        try {
            id = Integer.parseInt(data[INDEX_DELETING_ROW_ID]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.valueOf(INDEX_DELETING_ROW_ID + 1) + " parameter must be numeric!");
        }
        manager.deleteRow(tableName, id);
        view.write(String.format("Delete row '%s' from table '%s' successfully", id, tableName));
    }
}
