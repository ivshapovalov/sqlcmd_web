package ru.ivan.sqlcmd.model;

/**
 * Created by Ivan on 20.09.2016.
 */
public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return  new JDBCDatabaseManager();
    }

 }
