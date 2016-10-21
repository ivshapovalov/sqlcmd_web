package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.TableConstructor;
import ru.ivan.sqlcmd.view.View;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rows extends AbstractCommand {

    public Rows() {
    }

    public Rows(final DatabaseManager manager, final View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String getDescription() {
        return "list of rows in table";
    }

    @Override
    public String getCommandFormat() {
        return "rows|tableName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("rows|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;

        String[] data = command.split("[|]");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Must be " + parametersLength(getCommandFormat()) + " parameters" +
                    " in getCommandFormat " + getCommandFormat());

        }
        String table = data[INDEX_TABLE_NAME];
        List<Map<String, Object>> tableData = manager.getTableRows(table);
        Set<String> tableHeaders = manager.getTableColumns(table);

        TableConstructor constructor = new TableConstructor(
                tableHeaders, tableData);
        view.write(constructor.getTableString());
    }

}
