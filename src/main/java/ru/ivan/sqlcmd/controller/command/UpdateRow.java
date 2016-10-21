package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateRow extends AbstractCommand {
    private final Integer INDEX_DIGITAL_MARK = 0;
    private final String DIGITAL_MARK = "@";

    public UpdateRow() {
    }

    public UpdateRow(final DatabaseManager manager, final View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String getDescription() {
        return "update row in table with specific condition (mark digital columnNames as @columnName)";
    }

    @Override
    public String getCommandFormat() {
        return "updateRow|tableName|columnNameCondition|valueCondition|columnNameToSet1|valueToSet1|...|columnNameToSetN|valueToSetN";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("updateRow|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;
        final int INDEX_CONDITION_COLUMN_NAME = 2;
        final int INDEX_CONDITION_COLUMN_VALUE = 3;

        String[] data = command.split("[|]");
        if (data.length % 2 != 0 || data.length <= 5) {
            throw new IllegalArgumentException("Must be not even parameters equal to or greater than 6 " +
                    "in getCommandFormat '" + getCommandFormat() + "'");

        }
        String tableName = data[INDEX_TABLE_NAME];
        String conditionColumnName = data[INDEX_CONDITION_COLUMN_NAME];
        if (conditionColumnName.charAt(INDEX_DIGITAL_MARK) == DIGITAL_MARK.charAt(INDEX_DIGITAL_MARK)) {
            conditionColumnName = conditionColumnName.substring(INDEX_DIGITAL_MARK+1);
        }
        String conditionColumnValue = data[INDEX_CONDITION_COLUMN_VALUE];
        Map<String, Object> tableData = extractTableDataFromParameters(data);
        manager.updateRow(tableName, conditionColumnName, conditionColumnValue, tableData);

        view.write(String.format("Update row '%s' in table '%s' successfully", tableData, tableName));
    }

    private Map<String, Object> extractTableDataFromParameters(final String[] data) {
        int parametersCount = data.length / 2 - 2;
        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i <= parametersCount; i++) {
            String columnName = data[i * 2 + 2];
            String valueString = data[i * 2 + 3];
            Object value;
            if (columnName.charAt(INDEX_DIGITAL_MARK) == DIGITAL_MARK.charAt(INDEX_DIGITAL_MARK)) {
                columnName = columnName.substring(INDEX_DIGITAL_MARK+1);
                try {
                    value = Integer.valueOf(valueString);
                } catch (NumberFormatException e) {
                    value = valueString;
                }
            } else {
                value = valueString;
            }
            tableData.put(columnName, value);
        }
        return tableData;
    }
}
