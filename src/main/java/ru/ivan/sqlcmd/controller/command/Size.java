package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Size extends Command {

    @Override
    public String description() {
        return "return size of the table";
    }

    @Override
    public String format() {
        return "size|tableName";
    }

    public Size() {
    }

    public Size(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("size|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        if (data.length!=2) {
            throw new IllegalArgumentException("Должно быть два параметра " +
                    "в формате size|tableName");

        }
        String tableName = data[1];

        Integer size = manager.getTableSize(tableName);

        view.write(tableName + " size is "+ size);


 }

}
