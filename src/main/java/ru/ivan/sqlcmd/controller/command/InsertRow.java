package ru.ivan.sqlcmd.controller.command;

import java.util.LinkedHashMap;
import java.util.Map;

class InsertRow extends AbstractCommand {

    InsertRow() {
    }

    @Override
    public String getDescription() {
        return "insert getRow in table";
    }

    @Override
    public String getCommandFormat() {
        return "insertRow|tableName|columnName1|value1|columnName2|value2|...|columnNameN|valueN";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("insertRow|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;

        String[] data = command.split("[|]");
        if (data.length % 2 == 1) {
            throw new IllegalArgumentException("Expect command parameters " +
                    "in format '" + getCommandFormat() + "'");

        }
        String table = data[INDEX_TABLE_NAME];
        Map<String, Object> tableData = extractTableDataFromParameters(data);

        manager.insertRow(table, tableData);

        view.write(String.format("Insert getRow '%s' into table '%s' successfully", tableData, table));
    }

    private Map<String, Object> extractTableDataFromParameters(final String[] data) {
        int parametersCount = data.length / 2 - 1;

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i <= parametersCount; i++) {
            String column = data[i * 2];
            String value = data[i * 2 + 1];
            tableData.put(column, value);
        }
        return tableData;
    }
}
