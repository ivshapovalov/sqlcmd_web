package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Size extends AbstractCommand  {

    public Size() {
    }

    public Size(final DatabaseManager manager, final View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String getDescription() {
        return "size of the table";
    }

    @Override
    public String getCommandFormat() {
        return "size|tableName";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("size|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;

        String[] data = command.split("[|]");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Must be " + parametersLength(getCommandFormat()) + " parameters " +
                    "in getCommandFormat " + getCommandFormat());

        }
        String tableName = data[INDEX_TABLE_NAME];
        int size;
        size = manager.getTableSize(tableName);
        view.write(String.format("Table '%s' size is %s", tableName, size));
    }

}
