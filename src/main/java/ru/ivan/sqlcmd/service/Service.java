package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.List;
import java.util.Map;


public interface Service {

    List<String> getMainMenu();

    DatabaseManager connect(String databaseName, String userName, String password);

    DatabaseManager disconnect();

    List<List<String>> rows(DatabaseManager manager, String tableName);

    List<List<String>> row(DatabaseManager manager, String TableName, Integer id);

    List<List<String>> tables(DatabaseManager manager);

    List<String> getTableColumns(DatabaseManager manager, String tableName);

    List<String> databases(DatabaseManager manager);

    List<List<String>> help();

    void deleteRow(DatabaseManager manager, String tableName, int id);

    void dropTable(DatabaseManager manager, String tableName);

    void truncateTable(DatabaseManager manager,String tableName);

    void insertRow(DatabaseManager manager, String tableName, Map<String, Object> row);

    void updateRow(DatabaseManager manager, String tableName, String idColumn, int idValue, Map<String, Object> row);

    void createDatabase(DatabaseManager manager,String databaseName);

    void dropDatabase(DatabaseManager manager, String databaseName);

    void createTable(DatabaseManager manager, String tableName, String keyName, Map<String, Object> columnParameters);
}
