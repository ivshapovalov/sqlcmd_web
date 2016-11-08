package ua.com.juja.sqlcmd.service;

import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.controller.command.Unsupported;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.InMemoryDatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleksandr.baglai on 30.10.2015.
 */
//@Component
public class DummyServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        return new InMemoryDatabaseManager();
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) {
        throw new UnsupportedOperationException();
    }
}
