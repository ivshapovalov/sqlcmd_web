package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.DatabaseManagerException;
import ru.ivan.sqlcmd.view.View;

public class Size extends Command {

    public Size() {
    }

    public Size(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public String description() {
        return "размер таблицы";
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
            throw new IllegalArgumentException("Должно быть два параметра " +
                    "в формате "+format());

        }
        String tableName = data[1];
        Integer size=0;
        try {
           size = manager.getTableSize(tableName);
        } catch (DatabaseManagerException e) {
            throw e;
        }
        view.write(tableName + " size is "+ size);
 }

}
