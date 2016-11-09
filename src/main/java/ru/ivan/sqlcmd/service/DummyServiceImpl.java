package ru.ivan.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.controller.command.Unsupported;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
public class DummyServiceImpl implements Service {

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        return new PostgreSQLManager();
    }


    @Override
    public List<String> getDatabases(DatabaseManager manager) {
        return null;
    }

    @Override
    public List<String> getMainMenu() {
        return null;
    }

    @Override
    public List<List<String>> getRows(DatabaseManager manager, String tableName) {
        return null;
    }

    @Override
    public List<List<String>> getRow(DatabaseManager manager, String TableName, Integer id) {
        return null;
    }

    @Override
    public List<List<String>> getTables(DatabaseManager manager) {
        return null;
    }

    @Override
    public List<List<String>> help() {
        return null;
    }

    @Override
    public List<String> getTableColumns(DatabaseManager manager, String tableName) {
        return null;
    }
}
