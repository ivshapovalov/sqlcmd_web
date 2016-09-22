package ru.ivan.sqlcmd.model;

import java.sql.*;

/**
 * Created by Ivan on 16.09.2016.
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        //System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {





            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/sqlcmd", "postgres",
                    "postgres");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            //System.out.println("You made it, take control your database now!");

            //update
            String SQL="INSERT into users (name,password) values ('Stiven','Pupkin')";
            Statement stmt=connection.createStatement();
            update(SQL, stmt);

//            SQL="DELETE from users where id<4";
//            stmt=connection.createStatement();
//            update(SQL, stmt);
//
//            SQL="UPDATE users set name='ivan' where id<8";
//            PreparedStatement psmt=connection.prepareStatement(SQL);
//            update(SQL, stmt);
//
//            Statement st=connection.createStatement();
//            ResultSet rs=st.executeQuery("select * from users");
//            while (rs.next()) {
//                System.out.println("id - "+rs.getString("id"));
//                System.out.println("name - "+rs.getString("name"));
//                System.out.println("pass - "+rs.getString("password"));
//
//            }
//            rs.close();
//            st.close();

            stmt.close();
            connection.close();
            //select

        } else {
            System.out.println("Failed to make connection!");
        }
    }

    private static void update(String SQL, Statement stmt) throws SQLException {
        stmt.executeUpdate(SQL);
    }


}
