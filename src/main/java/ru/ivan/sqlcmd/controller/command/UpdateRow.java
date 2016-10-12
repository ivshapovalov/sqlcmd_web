package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateRow extends AbstractCommand implements Command {
    private final static Integer INDEX_TABLE_NAME = 1;
    private final static Integer INDEX_CONDITION_COLUMN_NAME = 2;
    private final static Integer INDEX_CONDITION_COLUMN_VALUE = 3;

    UpdateRow() {
    }

    public UpdateRow(final DatabaseManager manager, final View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "update row with specific ID in table";
    }

    @Override
    public String format() {
        return "updateRow|tableName|columnCondition|valueCondition|columnToSet1|valueToSet1|...|columnToSetN|valueToSetN";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("updateRow|");
    }

    @Override
    public void process(final String command) {
        String[] data = command.split("[|]");
        if (data.length % 2 != 0 || data.length <=5) {
            throw new IllegalArgumentException("Must be not even parameters equal to or greater than 6 " +
                    "in format '"+format()+"'");

        }
        String tableName = data[INDEX_TABLE_NAME];
        String conditionColumnName=data[INDEX_CONDITION_COLUMN_NAME];
        String conditionColumnValue=data[INDEX_CONDITION_COLUMN_VALUE];
//        int id;
//        try {
//            id = Integer.parseInt(data[INDEX_CONDITION_COLUMN_NAME]);
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException(String.valueOf(INDEX_CONDITION_COLUMN_NAME + 1) + " parameter must be numeric!");
//        }
        Map<String, Object> tableData = extractTableDataFromParameters(data);

        manager.updateRow(tableName, conditionColumnName,conditionColumnValue, tableData);

        view.write(String.format("Update row '%s' in table '%s' successfully", tableData, tableName));
    }

    private Map<String, Object> extractTableDataFromParameters(final String[] data) {
        int parametersCount = data.length / 2 - 2;
        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i <= parametersCount; i++) {
            String column = data[i * 2+2];
            String value = data[i * 2 + 3];
            tableData.put(column, value);
        }
        return tableData;
    }
}
