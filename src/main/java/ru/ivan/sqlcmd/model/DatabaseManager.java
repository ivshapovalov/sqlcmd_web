package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {

    void connect(String databaseName, String user, String password);

    Set<String> getTableColumns(String tableName);

    Set<String> getTableColumnsWithType(String tableName);

    List<Map<String, Object>> getTableRows(String tableName);

    Integer getTableSize(String tableName);

    Set<String> getTableNames();

    void insertRow(String tableName, Map<String, Object> newRow);

    Map<String, Object> getRow(final String tableName, int id);

    boolean isConnected();

    void updateRow(String tableName, String conditionColumnName,String conditionColumnValue, Map<String, Object> newRow);

    void createTable(String tableName);

    void disconnect();

    void truncateTable(String... tableName);

    void dropTable(String tableName);

    void truncateAllTables();

    void dropAllTables();

    void deleteRow(String tableName, int id);

    String getDatabaseName();

    String getUserName();

}
