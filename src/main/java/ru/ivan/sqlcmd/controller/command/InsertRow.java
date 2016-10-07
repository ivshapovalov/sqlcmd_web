package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.DatabaseManagerException;
import ru.ivan.sqlcmd.view.View;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertRow extends Command {
    private final static Integer INDEX_TABLE_NAME=1;

    InsertRow() {
    }

    public InsertRow(final DatabaseManager manager,final  View view) {
        this.manager=manager;

        this.view=view;
    }

    @Override
    public String description() {
        return "insert row in table";
    }

    @Override
    public String format() {
        return "insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("insertRow|");
    }

    @Override
    public void process(final String command) {
        String[] data=command.split("[|]");
        if (data.length%2==1) {
            throw new IllegalArgumentException("Expect command parameters " +
                    "в формате '"+format()+"'");

        }
        String table=data[INDEX_TABLE_NAME];
        Map<String, Object> tableData = extractTableDataFromParameters(data);

        manager.insertRow(table, tableData);

        view.write(String.format("Insert row '%s' into table '%s' successfully",tableData,table ));
    }

    private Map<String, Object> extractTableDataFromParameters(final String[] data) {
        int parametersCount=data.length/2-1;

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i <= parametersCount; i++) {
            String column=data[i*2];
            String value=data[i*2+1];
            tableData.put(column,value);
        }
        return tableData;
    }
}
