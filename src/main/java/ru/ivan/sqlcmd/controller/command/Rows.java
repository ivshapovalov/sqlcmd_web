package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.TableConstructor;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rows extends Command {
    private final static Integer INDEX_TABLE_NAME=1;
    public Rows() {
    }

    public Rows(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public String description() {
        return "list of rows in table";
    }

    @Override
    public String format() {
        return "rows|tableName";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("rows|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        if (data.length!=parametersLength(format())) {
            throw new IllegalArgumentException("Must be " +parametersLength(format())+" parameters"+
                    " in format "+format());

        }
        String table = data[INDEX_TABLE_NAME];
        List<Map<String, Object>> tableData = manager.getTableRows(table);
        Set<String> tableHeaders = manager.getTableColumns(table);

        TableConstructor constructor = new TableConstructor(
                tableHeaders, tableData);
        view.write(constructor.getTableString());
    }

}
