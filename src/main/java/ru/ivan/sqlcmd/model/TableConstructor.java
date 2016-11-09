package ru.ivan.sqlcmd.model;

import ru.ivan.sqlcmd.controller.MainController;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableConstructor {

    private Set<String> columns;

    private List<Map<String, Object>> tableData;

    private StringBuilder table;

    public TableConstructor(Set<String> columns, List<Map<String, Object>> tableData) {
        this.columns = columns;
        this.tableData = tableData;
        this.table=new StringBuilder();
    }

    public String getTableString() {
        build();
        return table.toString();
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        for (String column : columns) {
            table.append(column).append("|");
        }
        table.append(MainController.LINE_SEPARATOR);
    }

    private void buildRows() {
        for (Map<String, Object> row : tableData) {
            for (Object value : row.values()) {
                String strValue;
                if (value != null) {
                    strValue = value.toString();
                } else {
                    strValue = "";
                }
                table.append(strValue).append("|");

            }
            table.append(MainController.LINE_SEPARATOR);
        }
    }
}