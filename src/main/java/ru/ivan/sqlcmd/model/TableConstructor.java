package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class TableConstructor {

    private Set<String> columns;
    private Table table;
    private List<Map<String, Object>> tableData;

    public TableConstructor(Set<String> columns, List<Map<String, Object>> tableData) {
        this.columns = columns;
        this.tableData = tableData;
        table = new Table(columns.size(), BorderStyle.CLASSIC, ShownBorders.ALL);
    }

    public String getTableString() {
        build();
        return table.render();
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        for (String column: columns) {
            table.addCell(column);
        }
    }

    private void buildRows() {
        for (Map<String, Object> row: tableData) {
            for (Object value: row.values()) {
                String strValue="";
                if (value!=null) {
                    strValue = value.toString();
                } else {
                    strValue="";
                }
                table.addCell(strValue);
            }
        }
    }
}