package ru.ivan.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ivan.sqlcmd.model.DatabaseConnectionRepository;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.entity.UserAction;
import ru.ivan.sqlcmd.model.UserActionRepository;

import java.util.*;

@Component
@Transactional
public abstract class ServiceImpl implements Service {

    public abstract DatabaseManager getManager();

    @Autowired
    private UserActionRepository userActions;

    @Autowired
    private DatabaseConnectionRepository databaseConnections;

    @Override
    public List<UserAction> getAllActionsOfUser(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("User name cant be null!");
        }
        return userActions.findByUserName(userName);
    }

    @Override
    public List<UserAction> getAllActionsOfUserAndDatabase(String userName, String dbName) {
        if (userName == null) {
            throw new IllegalArgumentException("User name cant be null!");
        }
        return userActions.findByUserNameAndDbName(userName,dbName);
    }

    @Override
    public DatabaseManager connect(final String databaseName, final String userName, final String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        userActions.createAction(userName, databaseName, "CONNECT");
        return manager;
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
                Object ob = dataSet.get(column);
                if (ob != null) {
                    row.add(ob.toString());
                } else {
                    row.add("");
                }
            }
        }
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(), "ROWS " +
                "(TABLE '"+ tableName + "')");
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
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(), "TABLES");

        return tablesWithSize;
    }

    @Override
    public List<String> getMainMenu(DatabaseManager manager) {
        if (manager != null) {
            userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                    "MENU");
        } else {
            userActions.createAction("", "", "MENU");
        }
        return Arrays.asList("help", "connect", "databases", "tables", "disconnect",
                "actions");
    }

    public List<List<String>> help(DatabaseManager manager) {
        List<List<String>> commands = new ArrayList<>();

        commands.add(Arrays.asList("connect", "connect to database"));
        commands.add(Arrays.asList("disconnect", "disconnect from database"));
        commands.add(Arrays.asList("tables", "list of tables"));
        commands.add(Arrays.asList("databases", "list of databases"));
        commands.add(Arrays.asList("create database", "create new database with specific name"));
        commands.add(Arrays.asList("drop database", "drop selected database"));
        commands.add(Arrays.asList("create table ", "create new table with selected number of columns"));
        commands.add(Arrays.asList("truncate table", "truncate selected table"));
        commands.add(Arrays.asList("drop table", "drop selected table"));
        commands.add(Arrays.asList("rows", "rows of selected table"));
        commands.add(Arrays.asList("insert row", "insert new row in selected table"));
        commands.add(Arrays.asList("update row", "update selected row in table"));
        commands.add(Arrays.asList("delete row", "delete selected row in table"));

        if (manager != null) {
            userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                    "HELP");
        } else {
            userActions.createAction("", "", "HELP");
        }

        return commands;
    }

    @Override
    public void createTable(DatabaseManager manager, String query) {
        manager.createTable(query);
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                "CREATE TABLE ("+query+")");
    }

    @Override
    public void truncateTable(DatabaseManager manager, String tableName) {
        manager.truncateTable(tableName);
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                "TRUNCATE TABLE ('"+tableName+"')");
    }

    @Override
    public void dropTable(DatabaseManager manager, String tableName) {
        manager.dropTable(tableName);
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                "DROP TABLE ('"+tableName+"')");
    }

    @Override
    public void truncateAllTables(DatabaseManager manager,String database) {
        manager.truncateAllTables();
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                "TRUNCATE DATABASE ('"+database+"')");
    }

    @Override
    public void deleteRow(DatabaseManager manager, String tableName, int id) {
        manager.deleteRow(tableName,id);
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                "DELETE ROW (TABLE '"+tableName+"', id= '"+id+"')");
    }

    @Override
    public void updateRow(DatabaseManager manager, String tableName, String conditionColumnName,
                          String conditionColumnValue, Map<String, Object> newRow) {
        manager.updateRow(tableName, conditionColumnName,conditionColumnValue,newRow);
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                String.format("UPDATE ROW (TABLE '%s', '%s'='%s')",
                        tableName,conditionColumnName,conditionColumnValue));
    }

    @Override
    public void insertRow(DatabaseManager manager, String tableName, Map<String, Object> newRow) {
        manager.insertRow(tableName, newRow);
        userActions.createAction(manager.getUserName(), manager.getDatabaseName(),
                String.format("INSERT ROW (TABLE '%s')",
                        tableName));
    }
}
