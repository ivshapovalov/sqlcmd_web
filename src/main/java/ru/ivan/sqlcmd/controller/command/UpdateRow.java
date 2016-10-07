package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.*;
import ru.ivan.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateRow extends Command {
    private final static Integer INDEX_TABLE_NAME=1;
    private final static Integer INDEX_UPDATING_ROW_ID =2;

    UpdateRow() {
    }

    public UpdateRow(final DatabaseManager manager,final  View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "update row with specific ID in table";
    }

    @Override
    public String format() {
        return "updateRow|tableName|ID";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("updateRow|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("[|]");
        if (data.length % 2 == 0 || data.length <=parametersLength(format())) {
            throw new IllegalArgumentException("Must be not even parameters equal to or greater than 4 " +
                    "in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN");

        }
        String tableName = data[INDEX_TABLE_NAME];
        int id;
        try {
             id= Integer.parseInt(data[INDEX_UPDATING_ROW_ID]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.valueOf(INDEX_UPDATING_ROW_ID+1)+" parameter must be numeric!");
        }
        Map<String, Object> tableData = extractTableDataFromParameters(data);

        manager.updateRow(tableName, id, tableData);

        view.write(String.format("Update row '%s' in table '%s' successfully", tableData,tableName));
    }

    private Map<String, Object> extractTableDataFromParameters(final String[] data) {
        int parametersCount=data.length/2-1;
        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i <= parametersCount; i++) {
            String column = data[i * 2 + 1];
            String value = data[i * 2 + 2];
            tableData.put(column, value);
        }
        return tableData;
    }
}
