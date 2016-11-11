package ru.ivan.sqlcmd.service;

import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public abstract class ConnectionServiceImpl implements ConnectionService {
    public abstract DatabaseManager getManager();

    @Override
    public DatabaseManager connect(final String databaseName, final String userName, final String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public List<List<String>> rows(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<Map<String, Object>> tableData = manager.getTableRows(tableName);
        result.add(columns);
        for (Map<String, Object> dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns) {
                row.add(dataSet.get(column).toString());
            }
        }
        return result;
    }

    @Override
    public List<List<String>> tables(DatabaseManager manager) {
        List<String> tableNames = new LinkedList<>(manager.getTableNames());
        List<List<String>> tablesWithSize = new LinkedList<>();
        for (String tableName : tableNames
                ) {
            List<String> row = new LinkedList<>();
            row.add(tableName);
            row.add(String.valueOf(manager.getTableSize(tableName)));
            tablesWithSize.add(row);
        }
        return tablesWithSize;
    }
}
