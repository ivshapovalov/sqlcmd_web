package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.DatabaseManagerException;
import ru.ivan.sqlcmd.view.View;

public class Size extends Command {
    private final static Integer INDEX_TABLE_NAME=1;

    public Size() {
    }

    public Size(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public String description() {
        return "size of the table";
    }

    @Override
    public String format() {
        return "size|tableName";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("size|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        if (data.length!=parametersLength(format())) {
            throw new IllegalArgumentException("Must be " +parametersLength(format())+" parameters "+
                    "in format "+format());

        }
        String tableName = data[INDEX_TABLE_NAME];
        Integer size=0;
        try {
           size = manager.getTableSize(tableName);
        } catch (DatabaseManagerException e) {
            throw e;
        }
        view.write(String.format("Table '%s' size is %s",tableName,size));
 }

}
