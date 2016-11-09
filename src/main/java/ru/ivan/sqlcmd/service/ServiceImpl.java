package ru.ivan.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.controller.command.Command;
import ru.ivan.sqlcmd.controller.command.Help;
import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.*;
@Component
public class ServiceImpl implements Service {

    @Autowired
    private DatabaseManagerFactory factory;

    @Override
    public List<String> getMainMenu() {
        return Arrays.asList("help", "menu", "connect", "databases", "tables", "disconnect");
    }

    @Override
    public void dropDatabase(DatabaseManager manager, String databaseName) {
        manager.dropDatabase(databaseName);
    }

    @Override
    public List<List<String>> help() {
        List<List<String>> commands = new ArrayList<>();

        for (Command command : new Help().getCommands()
                ) {
            if (command.showInHelp()) {
                List<String> row = new ArrayList<>();
                row.add(command.getCommandFormat());
                row.add(command.getDescription());
                commands.add(row);
            }
        }
        return commands;
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager= factory.createDatabaseManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public DatabaseManager disconnect() {
        return null;
    }

    @Override
    public void deleteRow(DatabaseManager manager, String tableName, int id) {
        manager.deleteRow(tableName, id);
    }

    @Override
    public void truncateTable(DatabaseManager manager, String tableName) {
        manager.truncateTable(tableName);

    }

    @Override
    public void insertRow(DatabaseManager manager, String tableName, Map<String, Object> row) {
        manager.insertRow(tableName, row);

    }

    @Override
    public void updateRow(DatabaseManager manager, String tableName, String id, String s, Map<String, Object> row) {
        manager.updateRow(tableName, "id", String.valueOf(id), row);

    }

    @Override
    public void createDatabase(DatabaseManager manager, String databaseName) {
        manager.createDatabase(databaseName);

    }

    @Override
    public void dropTable(DatabaseManager manager, String tableName) {
        manager.dropTable(tableName);

    }

    @Override
    public List<List<String>> rows(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<Map<String, Object>> tableData = manager.getTableRows(tableName);

        result.add(columns);
        for (Map<String, Object> dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns) {
                row.add(dataSet.get(column).toString());
            }
        }
        return result;
    }

    @Override
    public List<List<String>> row(DatabaseManager manager, String tableName, Integer id) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        Map<String, Object> tableData = manager.getRow(tableName, id);

        for (String column : columns) {
            List<String> row = new ArrayList<>(2);
            row.add(column);
            row.add(tableData.get(column).toString());
            result.add(row);
        }
        return result;
    }

    @Override
    public List<List<String>> tables(DatabaseManager manager) {
        List<String> tableNames = new LinkedList<>(manager.getTableNames());
        List<List<String>> tablesWithSize = new LinkedList<>();
        for (String tableName : tableNames
                ) {
            List<String> row = new LinkedList<>();
            row.add(tableName);
            row.add(String.valueOf(manager.getTableSize(tableName)));
            tablesWithSize.add(row);
        }
        return tablesWithSize;
    }

    @Override
    public List<String> databases(DatabaseManager manager) {
        return new LinkedList<>(manager.getDatabasesNames());
    }

    @Override
    public List<String> getTableColumns(DatabaseManager manager, String tableName) {
        return new LinkedList<>(manager.getTableColumns(tableName));
    }
}
