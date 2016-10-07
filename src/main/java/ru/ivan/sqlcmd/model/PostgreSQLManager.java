package ru.ivan.sqlcmd.model;

import java.sql.*;
import java.util.*;

public class PostgreSQLManager implements DatabaseManager {

    private Connection connection;

    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String HOST = propertiesLoader.getServerName();
    private static final String PORT = propertiesLoader.getDatabasePort();
    private static final String DRIVER = propertiesLoader.getDriver();
    private static final String DATABASE_URL = DRIVER + HOST + ":" + PORT + "/";
    private static final String USER_NAME = propertiesLoader.getUserName();
    private static final String PASSWORD = propertiesLoader.getPassword();

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
    public void connect(final String database,final String userName,final String password) {

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
             ResultSet rs = statement.executeQuery("select count(*) as count from  " + tableName)) {
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
        for (String tableName : tables
                ) {
            truncateTable(tableName);
        }
    }

    @Override
    public void createDatabase(final String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE  " + databaseName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to create a table '%s'", databaseName), e);

        }
    }

    @Override
    public void dropDatabase(final String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + databaseName + ";");
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to delete a table '%s'", databaseName), e);

        }
    }

    @Override
    public void createTable(final String query) {

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE public." + query );
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("Query cannot be completed  '%s'",
                    query),
                    e);
        }
    }

    @Override
    public Set<String> getDatabasesNames() {
        Set<String> databases = new LinkedHashSet<String>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT datname as database_name FROM pg_database WHERE datistemplate = false;")) {
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
             ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName)) {
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
        Set<String> tables = new LinkedHashSet<String>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'")) {
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
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS public." + tableName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to delete a table '%s'", tableName), e);
        }
    }

    @Override
    public void truncateTable(final String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE public." + tableName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is not possible to clear the table '%s'",
                    tableName),
                    e);
        }
    }

    @Override
    public void insertRow(final String tableName,final  Map<String, Object> newRow) {
        String rowNames = getFormatedName(newRow, "\"%s\",");
        String values = getFormatedValues(newRow, "'%s',");
        String sql = createString("INSERT INTO ", tableName, " (", rowNames, ") ", "VALUES (", values, ")");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            String message = String.format("Cannot insert a row into a table '%s'. ",
                    tableName);
            String originalMessage = e.getMessage();
            if (originalMessage.contains("отношение")) {
                message = message.concat(" Table does not exists");
            } else if (originalMessage.contains("столбец")) {
                message = message.concat("С").concat(originalMessage.substring(originalMessage.indexOf(":") + 3,
                        originalMessage.indexOf("\n")));
            }
            throw new DatabaseManagerException(message);
        }
    }

    @Override
    public void updateRow(final String tableName,final  int id,final  Map<String, Object> newRow) {
        String tableNames = getFormatedName(newRow, "\"%s\" = ?,");
        String query = createString("UPDATE ", tableName, " SET ", tableNames, " WHERE id = ?");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int index = 1;
            for (Object value : newRow.values()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setObject(index, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            String message = String.format("It is not possible to update a record with id=%s in table '%s'.",
                    id,tableName);
            String originalMessage = e.getMessage();
            if (originalMessage.contains("отношение")) {
                message = message.concat(" Table does not exists");
            } else if (originalMessage.contains("столбец")) {
                message = message.concat("С").concat(originalMessage.substring(originalMessage.indexOf(":") + 3,
                        originalMessage.indexOf("\n")));
            }
            throw new DatabaseManagerException(message);
        }
    }

    @Override
    public void deleteRow(final String tableName,final  int id) {
        String query = createString("DELETE  FROM ", tableName, " WHERE id = ", String.valueOf(id));
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            String message = String.format("It is not possible to delete a record with id=%s in table '%s'.",
                    id,tableName);
            String originalMessage = e.getMessage();
            if (originalMessage.contains("отношение")) {
                message = message.concat(" Table does not exists");
            } else if (originalMessage.contains("столбец")) {
                message = message.concat("С").concat(originalMessage.substring(originalMessage.indexOf(":") + 3,
                        originalMessage.indexOf("\n")));
            }
            throw new DatabaseManagerException(message);
        }
    }

    @Override
    public Set<String> getTableColumns(final String tableName) {
        Set<String> tables = new LinkedHashSet<String>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName + "'")) {
            while (rs.next()) {
                tables.add(rs.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It is impossible to obtain a list of table '%s' columns",
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

    private String getFormatedName(final Map<String, Object> newValue,final  String format) {
        String string = "";
        for (String name : newValue.keySet()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getFormatedValues(final Map<String, Object> input,final  String format) {
        String values = "";
        for (Object value : input.values()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }
}
