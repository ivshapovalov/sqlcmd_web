package ru.ivan.sqlcmd.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
@Scope(value = "prototype")
public class PostgreSQLManager implements DatabaseManager {

    private static final String QUERY_TABLE_SIZE = "SELECT COUNT(*) AS COUNT FROM %s";
    private static final String QUERY_DATABASE_CREATE = "CREATE DATABASE %s";
    private static final String QUERY_DATABASE_DROP = "DROP DATABASE IF EXISTS %s ;";
    private static final String QUERY_DATABASES_NAMES = "SELECT datname as database_name " +
            "FROM pg_database WHERE datistemplate = false ORDER BY datname;";
    private static final String QUERY_SELECT_ROWS = "SELECT * FROM %s ";
    private static final String QUERY_TABLE_NAMES = "SELECT table_name FROM information_schema.tables WHERE" +
            " table_schema='public' AND table_type='BASE TABLE' ORDER BY table_name";
    private static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS public.%s";
    private static final String QUERY_TRUNCATE_TABLE = "TRUNCATE TABLE %s";
    private static final String QUERY_TABLE_COLUMNS = "SELECT * FROM information_schema.columns WHERE table_schema = 'public' " +
            "AND table_name = '%s' ORDER BY table_name;";
    private static final String QUERY_DELETE_ROW = "DELETE  FROM %s WHERE id = %s";
    private static final String QUERY_COLUMN_TYPE = "SELECT column_name,data_type FROM information_schema.columns"
            + "  WHERE table_schema = 'public' AND table_name = '%s' AND column_name='%s'";
    private static final String QUERY_UPDATE_ROW = "UPDATE %s SET %s WHERE %s = ?";
    private static final String QUERY_INSERT_ROW = "INSERT INTO %s (%s) VALUES (%s)";

    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String HOST = propertiesLoader.getServerName();
    private static final String PORT = propertiesLoader.getDatabasePort();
    private static final String DRIVER = propertiesLoader.getDriver();
    private static final String DATABASE_URL = DRIVER + HOST + ":" + PORT + "/";
    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            try {
                DriverManager.registerDriver(new org.postgresql.Driver());
            } catch (SQLException e1) {
                try {
                    throw new SQLException("Couldn't register driver in case -", e1);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    @Override
    public void connect(final String database, final String userName, final String password) {

        closeOpenedConnection(connection);
        try {
            connection = DriverManager.getConnection(DATABASE_URL + database, userName, password);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("Unable to connect to database '%s', user '%s', password '%s'",
                    database, userName, password),
                    e);
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        } else {
            throw new DatabaseManagerException("Disconnect failed. You are not connected to any Database");
        }
    }

    private void closeOpenedConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException("Unable to close connection", e);
            }
        }
    }

    @Override
    public Integer getTableSize(final String tableName) {
        Integer result = 0;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(QUERY_TABLE_SIZE,tableName))) {
            while (rs.next()) {
                result = rs.getInt("count");
                return result;
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to obtain the size of the table '%s'", tableName), e);
        }
        return result;
    }

    @Override
    public void dropAllDatabases() {
        Set<String> databases = getDatabasesNames();
        for (String databaseName : databases) {
            if ("postgres".equals(databaseName.trim())) {
                continue;
            }
            dropDatabase(databaseName);
        }
    }

    @Override
    public void dropAllTables() {
        Set<String> tables = getTableNames();
        for (String tableName : tables
                ) {
            dropTable(tableName);
        }
    }

    @Override
    public void truncateAllTables() {
        Set<String> tables = getTableNames();
        truncateTable(tables.toArray(new String[tables.size()]));
    }

    @Override
    public void createDatabase(final String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(QUERY_DATABASE_CREATE,databaseName));
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to create a table '%s'", databaseName), e);

        }
    }

    @Override
    public void dropDatabase(final String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(QUERY_DATABASE_DROP,databaseName));
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to delete a table '%s'", databaseName), e);
        }
    }

    @Override
    public void createTable(final String query) {

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE public." + query);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("Query cannot be completed  '%s'",
                    query),
                    e);
        }
    }

    @Override
    public Set<String> getDatabasesNames() {
        Set<String> databases = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(QUERY_DATABASES_NAMES)) {
            while (rs.next()) {
                databases.add(rs.getString("database_name"));
            }
            return databases;
        } catch (SQLException e) {
            throw new DatabaseManagerException("It is not possible to obtain the list of databases", e);

        }
    }

    @Override
    public List<Map<String, Object>> getTableRows(final String tableName) {
        List<Map<String, Object>> result = new LinkedList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(QUERY_SELECT_ROWS,tableName))) {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> data = new LinkedHashMap<>();
                for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                    data.put(rsmd.getColumnName(index), rs.getObject(index));
                }
                result.add(data);
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to list table '%s' rows", tableName), e);
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(QUERY_TABLE_NAMES)) {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException | NullPointerException e) {
            throw new DatabaseManagerException("It is not possible to list all tables", e);
        }
    }

    @Override
    public void dropTable(final String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(QUERY_DROP_TABLE,tableName));
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to delete a table '%s'", tableName), e);
        }
    }

    @Override
    public void truncateTable(final String... tableName ) {

        StringBuilder tableList=new StringBuilder();
        for (String table:tableName
             ) {
            tableList.append("public.").append(table).append(",");
        }
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(QUERY_TRUNCATE_TABLE,tableList.substring(0,
                    tableList.length()-1)));
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to clear the table '%s'",
                    tableName),
                    e);
        }
    }

    @Override
    public void insertRow(final String tableName, final Map<String, Object> newRow) {
        String rowNames = getFormatedName(newRow, "\"%s\",");
        String values = getFormatedValues(newRow, "'%s',");
        String sql = createString(String.format(QUERY_INSERT_ROW,tableName,rowNames,values));
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            String message = String.format("Cannot insert a row into a table '%s'. Table or column does not exists.",
                    tableName);
            throw new DatabaseManagerException(message);
        }
    }

    @Override
    public Map<String, Object> getRow(final String tableName, int id) {
        Map<String, Object> result = new LinkedHashMap<>();
        String sql = String.format(QUERY_SELECT_ROWS + " where id=%s", tableName,id);
        try (Statement statement = connection.createStatement()) {
            ResultSet rs= statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                    result.put(rsmd.getColumnName(index), rs.getObject(index));
                }
            }
            return result;
        } catch (SQLException e) {
            String message = String.format("Cannot insert a row into a table '%s'. Table or column does not exists.",
                    tableName);
            throw new DatabaseManagerException(message);
        }
    }

    @Override
    public void updateRow(final String tableName, final String conditionColumnName, String conditionColumnValue, final Map<String, Object> newRow) {
        String tableNames = getFormatedName(newRow, "\"%s\" = ?,");
        String query = createString(String.format(QUERY_UPDATE_ROW,
                tableName,tableNames,conditionColumnName));

        Object id=getColumnType(tableName,conditionColumnName,conditionColumnValue);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int index = 1;
            for (Object value : newRow.values()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setObject(index, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            String message = String.format("It is not possible to update a record with '%s'=%s in table '%s'. " +
                    "Table or column does not exists.",
                    conditionColumnName,conditionColumnValue, tableName);
            throw new DatabaseManagerException(message);
        }
    }

    private Object getColumnType(final String tableName, final String conditionColumnName, final String conditionColumnValue) {
        String dataType = "";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(QUERY_COLUMN_TYPE,tableName,conditionColumnName))) {
            while (rs.next()) {
                dataType = rs.getString("data_type");
            }
            if (!"".equals(dataType)) {
                if (dataType.contains("text")) {
                    return conditionColumnValue;
                } else if (dataType.contains("numeric") || dataType.contains("integer")) {
                    return Integer.valueOf(conditionColumnValue);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is impossible to get data type of '%s' column in table '%s'",
                    conditionColumnName,tableName),
                    e);
        }
        return null;
    }

    @Override
    public void deleteRow(final String tableName, final int id) {
        String query = String.format(QUERY_DELETE_ROW,tableName,String.valueOf(id));
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            String message = String.format("It is not possible to delete a record with id=%s in table '%s'." +
                    " Table or column does not exists.",
                    id, tableName);
            throw new DatabaseManagerException(message);
        }
    }

    @Override
    public Set<String> getTableColumns(final String tableName) {
        Set<String> tables = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(QUERY_TABLE_COLUMNS,tableName))) {
            while (rs.next()) {
                tables.add(rs.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is impossible to obtain a list of table '%s' ",
                    tableName),
                    e);
        }
    }

    @Override
    public Set<String> getTableColumnsWithType(final String tableName) {
        Set<String> tables = new LinkedHashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(QUERY_TABLE_COLUMNS,
                     tableName))) {
            while (rs.next()) {
                tables.add(rs.getString("column_name").concat(" (").concat(rs.getString
                        ("data_type").concat(")")));
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is impossible to obtain a list of table '%s'",
                    tableName),
                    e);
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private String createString(final String... args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
        }
        return result.toString();
    }

    private String getFormatedName(final Map<String, Object> newValue, final String format) {
        String string = "";
        for (String name : newValue.keySet()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getFormatedValues(final Map<String, Object> input, final String format) {
        String values = "";
        for (Object value : input.values()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }
}
