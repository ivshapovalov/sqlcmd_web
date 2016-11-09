package ru.ivan.sqlcmd.service;

import ru.ivan.sqlcmd.model.DatabaseManager;

public interface DatabaseManagerFactory {
    DatabaseManager createDatabaseManager();
}
