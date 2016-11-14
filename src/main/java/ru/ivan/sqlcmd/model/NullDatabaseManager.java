package ru.ivan.sqlcmd.model;

import java.util.*;

public class NullDatabaseManager implements DatabaseManager {
    private Map<String, List<Map<String,Object>>> tables = new LinkedHashMap<>();

    private List<Map<String,Object>> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<>());
        }
        return tables.get(tableName);
    }

    @Override
    public void connect(String databaseName, String user, String password) {
        // do nothing
    }

    @Override
    public void createDatabase(String databaseName) {

    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<String>(Arrays.asList("name", "id"));
    }

    @Override
    public List<Map<String, Object>> getTableRows(String tableName) {
        return get(tableName);
    }

    @Override
    public Integer getTableSize(String tableName) {
        return get(tableName).size();
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public void insertRow(String tableName, Map<String, Object> newRow) {
        get(tableName).add(newRow);
    }

    @Override
    public Map<String, Object> getRow(String tableName, int id) {
        return null;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void updateRow(String tableName, String conditionColumnName, String conditionColumnValue, Map<String, Object> newRow) {
        for (Map<String,Object> dataSet : get(tableName)) {
            if (dataSet.get(conditionColumnName) == conditionColumnValue) {
                Set<String> columns = newRow.keySet();
                for (String name : columns) {
                    Object data = newRow.get(name);
                    dataSet.put(name, data);
                }
            }
        }
    }

    @Override
    public void createTable(String tableName) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public Set<String> getDatabasesNames() {
        return null;
    }

    @Override
    public void truncateTable(String tableName) {
        get(tableName).clear();

    }

    @Override
    public void dropTable(String tableName) {

    }

    @Override
    public void dropDatabase(String databaseName) {

    }

    @Override
    public void truncateAllTables() {

    }

    @Override
    public void dropAllTables() {

    }

    @Override
    public void dropAllDatabases() {

    }

    @Override
    public void deleteRow(String tableName, int id) {

    }
}
