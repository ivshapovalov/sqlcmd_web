package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.List;
import java.util.Map;

public interface ConnectionService {
    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> rows(DatabaseManager manager, String users);

    List<List<String>> tables(DatabaseManager manager);
}

