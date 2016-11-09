package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.List;
import java.util.Map;


public interface Service {

    List<String> getMainMenu();

    DatabaseManager connect(String databaseName, String userName, String password);

    DatabaseManager disconnect();

    List<List<String>> getRows(DatabaseManager manager, String tableName);

    List<List<String>> getRow(DatabaseManager manager, String TableName, Integer id);

    List<List<String>> getTables(DatabaseManager manager);

    List<String> getTableColumns(DatabaseManager manager, String tableName);

    List<String> getDatabases(DatabaseManager manager);

    
    List<List<String>> help();

    void deleteRow(DatabaseManager manager, String tableName, int id);

    void dropTable(DatabaseManager manager, String tableName);

    void truncateTable(DatabaseManager manager,String tableName);

    void insertRow(DatabaseManager manager, String tableName, Map<String, Object> row);

    void updateRow(DatabaseManager manager, String tableName, String id, String s, Map<String, Object> row);

    void createDatabase(DatabaseManager manager,String databaseName);

    void dropDatabase(DatabaseManager manager, String databaseName);
}
