package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableConstructor {

    private Set<String> columns;

    private List<Map<String, Object>> tableData;

    public TableConstructor(Set<String> columns, List<Map<String, Object>> tableData) {
        this.columns = columns;
        this.tableData = tableData;
    }

    public String getTableString() {
        build();
        return "";
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        for (String column : columns) {

        }
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

            }
        }
    }
}