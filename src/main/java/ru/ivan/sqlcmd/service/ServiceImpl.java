package ru.ivan.sqlcmd.service;

import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.*;

@Component
public abstract class ServiceImpl implements Service {
    public abstract DatabaseManager getManager();

    @Override
    public List<String> getMainMenu() {
        return Arrays.asList("help", "connect", "databases", "tables", "disconnect");
    }

    @Override
    public void createTable(DatabaseManager manager, String tableName, String keyName, Map<String, Object> columnParameters) {
        String query = tableName + "(" + keyName + " INT  PRIMARY KEY NOT NULL" + getParameters(columnParameters) + ")";
        manager.createTable(query);
    }

    private String getParameters(Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
    }

    @Override
    public void dropDatabase(DatabaseManager manager, String databaseName) {
        manager.dropDatabase(databaseName);
    }

    @Override
    public List<List<String>> help() {
        List<List<String>> commands = new ArrayList<>();

        commands.add(Arrays.asList("connect", "connect to database"));
        commands.add(Arrays.asList("disconnect", "disconnect from database"));
        commands.add(Arrays.asList("tables", "list of tables"));
        commands.add(Arrays.asList("databases", "list of databases"));
        commands.add(Arrays.asList("create database", "create new database with specific name"));
        commands.add(Arrays.asList("drop database", "drop selected database"));
        commands.add(Arrays.asList("create table ", "create new table with selected number of columns"));
        commands.add(Arrays.asList("truncate table", "truncate selected table"));
        commands.add(Arrays.asList("drop table", "drop selected table"));
        commands.add(Arrays.asList("rows", "rows of selected table"));
        commands.add(Arrays.asList("insert row", "insert new row in selected table"));
        commands.add(Arrays.asList("update row", "update selected row in table"));
        commands.add(Arrays.asList("delete row", "delete selected row in table"));

        return commands;
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {

        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public DatabaseManager disconnect() {
        return null;
    }

    @Override
    public void deleteRow(DatabaseManager manager, String tableName, int id) {
        manager.deleteRow(tableName, id);
    }

    @Override
    public void truncateTable(DatabaseManager manager, String tableName) {
        manager.truncateTable(tableName);

    }

    @Override
    public void insertRow(DatabaseManager manager, String tableName, Map<String, Object> row) {
        manager.insertRow(tableName, row);

    }

    @Override
    public void updateRow(DatabaseManager manager, String tableName, String idColumn, int idValue, Map<String, Object> row) {
        manager.updateRow(tableName, idColumn, String.valueOf(idValue), row);

    }

    @Override
    public void createDatabase(DatabaseManager manager, String databaseName) {
        manager.createDatabase(databaseName);

    }

    @Override
    public void dropTable(DatabaseManager manager, String tableName) {
        manager.dropTable(tableName);

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
    public List<List<String>> row(DatabaseManager manager, String tableName, Integer id) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        Map<String, Object> tableData = manager.getRow(tableName, id);

        for (String column : columns) {
            List<String> row = new ArrayList<>(2);
            row.add(column);
            row.add(tableData.get(column).toString());
            result.add(row);
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

    @Override
    public List<String> databases(DatabaseManager manager) {
        return new LinkedList<>(manager.getDatabasesNames());
    }

    @Override
    public List<String> getTableColumns(DatabaseManager manager, String tableName) {
        return new LinkedList<>(manager.getTableColumns(tableName));
    }
}
