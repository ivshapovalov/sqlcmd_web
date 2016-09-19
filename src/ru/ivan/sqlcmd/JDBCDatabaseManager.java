package ru.ivan.sqlcmd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 16.09.2016.
 */
public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {

        String database = "jdbc:postgresql://127.0.0.1:5432/sqlcmd";
        String user = "postgres";
        String password = "postgres";
        JDBCDatabaseManager JDBCDatabaseManager = new JDBCDatabaseManager();
        JDBCDatabaseManager.connect(database, user, password);

        Connection connection = JDBCDatabaseManager.getConnection();

        if (connection != null) {
            //System.out.println("You made it, take control your database now!");

//            //update
//            String SQL = "INSERT into users (name,password) values ('Stiven','Pupkin')";
//            Statement stmt = connection.createStatement();
//            update(SQL, stmt);
//
//            SQL = "DELETE from users where id<4";
//            stmt = connection.createStatement();
//            update(SQL, stmt);
//
//            SQL = "UPDATE users set name='ivan' where id<8";
//            PreparedStatement psmt = connection.prepareStatement(SQL);
//            update(SQL, stmt);

            JDBCDatabaseManager.clear("users");
            DataSet input = new DataSet();
            input.put("name", "Stiven");
            input.put("password", "pass");
            input.put("id", 13);
            JDBCDatabaseManager.create("users", input);

            List<String> tables = JDBCDatabaseManager.getTablesNames();

            String tableName = "users";
            List<DataSet> result = JDBCDatabaseManager.getTableData(tableName);
            System.out.println(result.toString());

        } else {
            System.out.println("Failed to make connection!");
        }
    }

    public List<DataSet> getTableData(String tableName) {
        List<DataSet> result = new ArrayList<>();
        Statement st = null;
        try {
            st = connection.createStatement();

            ResultSet rs = st.executeQuery("Select * from " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                DataSet dataSet = new DataSet();

                for (int i = 1; i <= columnCount; i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));

                }
                result.add(dataSet);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public List<String> getTablesNames() {
        List<String> tables = new ArrayList();
        Statement st = null;
        try {
            st = connection.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery("select table_name from information_schema.tables where table_schema='public' ");
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;

    }

    @Override
    public void connect(String database, String user, String password) {
        try {
            connection = DriverManager.getConnection(database
                    , user, password
            );

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            connection = null;

        }

    }

    private static void update(String SQL, Statement stmt) {
        try {
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear(String tableName) {

        try {
            String SQL = "DELETE from " + tableName;
            Statement stmt = connection.createStatement();
            update(SQL, stmt);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void create(String tableName, DataSet input) {
        try {
            String tableNames = "";
            for (String name : input.getNames()) {
                tableNames += name + ",";
            }
            tableNames = tableNames.substring(0, tableNames.length() - 1);
            String values = "";
            for (Object value : input.getValues()) {
                values += "'" + value.toString() + "'" + ",";
            }
            values = values.substring(0, values.length() - 1);
            String SQL = "INSERT into " + tableName + " (" + tableNames + ") VALUES (" + values + ")";
            Statement stmt = connection.createStatement();
            update(SQL, stmt);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String tableName, int id, DataSet input) {
        try {

            String condition = "";
            for (int i = 0; i < input.getNames().size(); i++) {
                condition += input.getNames().get(i) + "='" + input.getValues().get(i) + "',";
            }
            condition = condition.substring(0, condition.length() - 1);
            String SQL = "UPDATE " + tableName + " SET " + condition + " WHERE id='" + id + "'";
            PreparedStatement stmt = connection.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
