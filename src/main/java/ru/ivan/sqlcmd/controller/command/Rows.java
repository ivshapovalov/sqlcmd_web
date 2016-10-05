package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.TableConstructor;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rows extends Command {

    @Override
    public String description() {
        return "list all rows in table";
    }

    @Override
    public String format() {
        return "rows|tableName";
    }

    public Rows() {
    }

    public Rows(DatabaseManager manager, View view) {
        this.manager=manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("rows|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        if (data.length!=2) {
            throw new IllegalArgumentException("Должно быть два параметра " +
                    "в формате rows|tableName");

        }
        String table = data[1];
        List<Map<String, Object>> tableData = manager.getTableRows(table);
        Set<String> tableHeaders = manager.getTableColumns(table);

        TableConstructor constructor = new TableConstructor(
                tableHeaders, tableData);
        view.write(constructor.getTableString());
    }

}
