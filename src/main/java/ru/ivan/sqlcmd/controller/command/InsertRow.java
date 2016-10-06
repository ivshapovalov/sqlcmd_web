package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.DatabaseManagerException;
import ru.ivan.sqlcmd.view.View;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertRow extends Command {

    public InsertRow() {
    }

    public InsertRow(DatabaseManager manager, View view) {
        this.manager=manager;

        this.view=view;
    }

    @Override
    public String description() {
        return "вставка строки в таблицу";
    }

    @Override
    public String format() {
        return "insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insertRow|");
    }

    @Override
    public void process(String command) {
        String[] data=command.split("[|]");
        if (data.length%2==1) {
            throw new IllegalArgumentException("Должно быть четное количество параметров " +
                    "в формате "+format());

        }
        String table=data[1];

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i < data.length/2; i++) {
            String column=data[i*2];
            String value=data[i*2+1];
            tableData.put(column,value);
        }
        try {
            manager.insertRow(table, tableData);
        } catch (DatabaseManagerException e) {
            throw e;
        }
        view.write(String.format("В таблице '%s' успешно создана запись %s",table,tableData ));
    }
}
