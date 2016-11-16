package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface Service  {
    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> rows(DatabaseManager manager, String users);

    List<List<String>> tables(DatabaseManager manager);

    List<String>  getMainMenu();

    List<String>  databases(DatabaseManager manager);

    List<List<String>> help();

}

