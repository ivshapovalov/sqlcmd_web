package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.List;


public interface Service {

    List<String> mainMenuList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> rows(DatabaseManager manager, String tableName);

    List<List<String>> row(DatabaseManager manager,String TableName, Integer id);

    List<List<String>> tables(DatabaseManager manager);

    List<String> databases(DatabaseManager manager);

    List<List<String>> help();

    List<String> tableColumns(DatabaseManager manager,String tableName);

}
