package ru.ivan.sqlcmd.model;

public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return  new PostgreSQLManager();
    }

 }
