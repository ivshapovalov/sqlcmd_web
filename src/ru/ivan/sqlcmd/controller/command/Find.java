package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Find implements Command {

    private final DatabaseManager manager;
    private View view;

    public Find(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        String table = data[1];
        java.util.List tableData = manager.getTableData(table);
        java.util.List tableHeaders = manager.getTableColumns(table);
        printHeader(tableHeaders);
        printTable(tableData);
    }

    private void printTable(java.util.List<DataSet> tableData) {
        for (DataSet row : tableData
                ) {
            printRow(row);
        }
    }
    private void printRow(DataSet row) {
        java.util.List values = row.getValues();
        String string = "";
        for (Object column : values
                ) {
            string += column + "\t" + "|";
        }
        view.write(string);
    }

    private void printHeader(java.util.List<String> tableHeaders) {
        String header = "";
        for (String column : tableHeaders
                ) {
            header += column + "\t" + "|";
        }
        view.write(header);
    }
}
