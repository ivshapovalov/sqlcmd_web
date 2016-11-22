package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.entity.UserAction;

import java.util.List;
import java.util.Map;

public interface Service  {
    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> rows(DatabaseManager manager, String users);

    List<List<String>> tables(DatabaseManager manager);

    List<String>  getMainMenu(DatabaseManager manager);

    List<String>  databases(DatabaseManager manager);

    List<List<String>> help(DatabaseManager manager);

    List<UserAction> getAllActionsOfUser(String userName);

    List<UserAction> getAllActionsOfUserAndDatabase(String userName, String dbName);

    void createDatabase(DatabaseManager manager,String database);

    void dropDatabase(DatabaseManager manager, String database);

    void truncateTable(DatabaseManager manager, String tableName);

    void dropTable(DatabaseManager manager, String tableName);

    void truncateAllTables(DatabaseManager manager, String database);

    void deleteRow(DatabaseManager manager, String tableName, int id);

    void updateRow(DatabaseManager manager, String tableName, String conditionColumnName,String
            conditionColumnValue, Map<String, Object> newRow);

    void insertRow(DatabaseManager manager, String tableName, Map<String, Object> newRow);

    void createTable(DatabaseManager manager, String query);
}

