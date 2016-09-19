package ru.ivan.sqlcmd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ivan on 16.09.2016.
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        // connect to db


        //System.out.println("-------- PostgreSQL "
          //     + "JDBC Connection Testing ------------");

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
                    "899004");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            //System.out.println("You made it, take control your database now!");

            String SQL="INSERT into users (id,name,password) values (2,'Stiven','Pupkin')";
            Statement stmt=connection.createStatement();
            stmt.executeUpdate(SQL);
        } else {
            System.out.println("Failed to make connection!");
        }
    }


}
