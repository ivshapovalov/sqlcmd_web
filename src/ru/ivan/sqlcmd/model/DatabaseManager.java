package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ivan on 20.09.2016.
 */
public interface DatabaseManager {

    void dropTable(String tableName);

    void connect(String databaseName, String user, String password);

    void createDatabase(String databaseName);

    Set<String> getTableColumns(String tableName);

    List<Map<String, Object>> getTableData(String tableName);

    Set<String> getTableNames();

    void insertRow(String tableName, Map<String, Object> input);

    boolean isConnected();

    void updateRow(String tableName, int id, Map<String, Object> newValue);

    void createTable(String tableName);

    void disconnect();

    Set<String> getDatabasesNames();

    void dropDatabase(String databaseName);

    void dropAllTables();

    void dropAllDatabases();
}
