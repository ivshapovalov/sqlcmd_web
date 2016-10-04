package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {

    void connect(String databaseName, String user, String password);

    void createDatabase(String databaseName);

    Set<String> getTableColumns(String tableName);

    List<Map<String, Object>> getTableRows(String tableName);

    Integer getTableSize(String tableName);

    Set<String> getTableNames();

    void insertRow(String tableName, Map<String, Object> newRow);

    boolean isConnected();

    void updateRow(String tableName, int id, Map<String, Object> newRow);

    void createTable(String tableName);

    void disconnect();

    Set<String> getDatabasesNames();

    void truncateTable(String tableName);

    void dropTable(String tableName);

    void dropDatabase(String databaseName);

    void truncateAllTables();

    void dropAllTables();

    void dropAllDatabases();

    void deleteRow(String tableName, int id);
}
