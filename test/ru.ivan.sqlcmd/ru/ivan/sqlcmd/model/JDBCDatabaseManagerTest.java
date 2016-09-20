package ru.ivan.sqlcmd.model;

/**
 * Created by Ivan on 20.09.2016.
 */
public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return  new InMemoryDatabaseManager();
    }

 }
