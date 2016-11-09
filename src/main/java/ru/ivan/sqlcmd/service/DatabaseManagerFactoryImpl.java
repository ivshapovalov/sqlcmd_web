package ru.ivan.sqlcmd.service;

import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;


@Component(value = "factory")
public class DatabaseManagerFactoryImpl implements DatabaseManagerFactory {

    private String className;

    @Override
    public DatabaseManager createDatabaseManager() {
//        try {
//            return (DatabaseManager)Class.forName(className).newInstance();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return new PostgreSQLManager();

    }

    public void setClassName(String className) {
        this.className = className;
    }
}
