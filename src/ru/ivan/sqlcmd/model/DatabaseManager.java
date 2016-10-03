package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ivan on 20.09.2016.
 */
public interface DatabaseManager {
    void clear(String tableName);

    void connect(String databaseName, String user, String password);

    void createDatabase(String databaseName);

//    void dropDatabase(String databaseName);
//
//    void dropTable(String tableName);

    Set<String> getTableColumns(String tableName);

    List<Map<String, Object>> getTableData(String tableName);

    Set<String> getTableNames();

    void insert(String tableName, Map<String, Object> input);

    boolean isConnected();

    void update(String tableName, int id, Map<String, Object> newValue);

    void createTable(String tableName);

    void disconnect();

    Set<String> getDatabasesNames();
}
