package ru.ivan.sqlcmd.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ivan.sqlcmd.controller.Main;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.model.PropertiesLoader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTestCreateDropTruncate {

    private final static String DB_NAME = "dbtest";
    private final static String TABLE_NAME1 = "test1";
    private final static String TABLE_NAME2 = "test2";
    private static DatabaseManager manager;
    private static final PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
        manager.disconnect();
    }

    @AfterClass
    public static void clearAfterAllTests() {

    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
        manager.disconnect();
    }


    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testCreateAndDropDatabase() {
        // given
        String testDB = "dbtest2";
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("createDatabase|" + testDB);
        in.add("databases");
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("databases");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase
                "Do you wish to delete database '"+testDB+"'. Y/N?\n" +
                "Database '"+testDB+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //createDatabase
                "Database '"+testDB+"' created successfully\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                "dbtest\n" +
                ""+testDB+"\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase
                "Do you wish to delete database '"+testDB+"'. Y/N?\n" +
                "Database '"+testDB+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                "dbtest\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testCreateAndDropAllDatabases() {

        // Осторожно. Тест удаляет ВСЕ базы данных.

//        // given
//        String testDB = "dbtest2";
//        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
//        in.add("dropDatabase|" + testDB);
//        in.add("y");
//        in.add("createDatabase|" + testDB);
//        in.add("databases");
//        in.add("dropAllDatabases");
//        in.add("y");
//        in.add("databases");
//        in.add("disconnect");
//        in.add("exit");
//
//        // when
//        Main.main(new String[0]);
//
//        // then
//        assertEquals("Hello, user\n" +
//                "Input command or 'help' for assistance\n" +
//                //connect
//                "Connecting to database '' is successful\n" +
//                "Input command or 'help' for assistance\n" +
//                //dropDatabase|test1
//                "Do you wish to delete database '"+testDB+"'. Y/N?\n" +
//                "Database '"+testDB+"' deleted successful\n" +
//                "Input command or 'help' for assistance\n" +
//                //createDatabase
//                "Database '"+testDB+"' created successfully\n" +
//                "Input command or 'help' for assistance\n" +
//                //databases
//                "***Existing databases***\n" +
//                "postgres\n" +
//                "dbtest\n" +
//                "dbtest2\n" +
//                "Input command or 'help' for assistance\n" +
//                //dropAllaDatabases
//                "Do you wish to delete all databases? Y/N\n" +
//                "All databases  deleted successfully\n" +
//                "Input command or 'help' for assistance\n" +
//                //databases
//                "***Existing databases***\n" +
//                "postgres\n" +
//                "Input command or 'help' for assistance\n" +
//                //disconnect
//                "Disconnect successful\n" +
//                "Input command or 'help' for assistance\n" +
//                //exit
//                "Good bye!\n", getData());
    }

    @Test
    public void testDropCurrentDatabase() {
        // given
        String testDB = "dbtest2";
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("createDatabase|" + testDB);
        in.add("connect|" + testDB + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("databases");
        in.add("dropDatabase|"+testDB);
        in.add("y");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase
                "Do you wish to delete database '"+testDB+"'. Y/N?\n" +
                "Database '"+testDB+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //createDatabase
                "Database '"+testDB+"' created successfully\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+testDB+"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                "dbtest\n" +
                "dbtest2\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase
                "Do you wish to delete database '"+testDB+"'. Y/N?\n" +
                "Error while deleting database '"+testDB+"'. Cause: 'It is not possible to delete a table '"+testDB+"''\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testTruncateAllTables() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|" + TABLE_NAME1);
        in.add("y");
        in.add("createTable|" + TABLE_NAME1 + " (id INTEGER,name text,password text)");
        in.add("insertRow|" + TABLE_NAME1 + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_NAME1);
        in.add("createTable|" + TABLE_NAME2 + " (id INTEGER,name text,password text)");
        in.add("insertRow|" + TABLE_NAME2 + "|id|2222|name|Ivan|password|++++");
        in.add("rows|" + TABLE_NAME2);
        in.add("truncateAllTables");
        in.add("y");
        in.add("rows|" + TABLE_NAME1);
        in.add("rows|" + TABLE_NAME2);
        in.add("dropAllTables");
        in.add("y");
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello, user\n" +
                        "Input command or 'help' for assistance\n" +
                        //connect
                        "Connecting to database '"+DB_NAME+"' is successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //dropTable
                        "Do you wish to delete table '"+TABLE_NAME1+"'. Y/N?\n" +
                        "Table '"+TABLE_NAME1+"' deleted successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //createTable
                        "Table '"+TABLE_NAME1 + " (id INTEGER,name text,password text)' created successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //insertRow
                        "Insert row '{id=1111, name=Peter, password=****}' into table '"+TABLE_NAME1+"' successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //rows1
                        "+----+-----+--------+\n" +
                        "|id  |name |password|\n" +
                        "+----+-----+--------+\n" +
                        "|1111|Peter|****    |\n" +
                        "+----+-----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //createTable2
                        "Table '"+TABLE_NAME2 + " (id INTEGER,name text,password text)' created successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //insertRow
                        "Insert row '{id=2222, name=Ivan, password=++++}' into table '"+TABLE_NAME2+"' successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //rows2
                        "+----+----+--------+\n" +
                        "|id  |name|password|\n" +
                        "+----+----+--------+\n" +
                        "|2222|Ivan|++++    |\n" +
                        "+----+----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //truncateAll
                        "Do you wish to clear all tables?. Y/N\n" +
                        "All tables cleared successfully\n" +
                        //rows1
                        "Input command or 'help' for assistance\n" +
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //rows2
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //dropAllTables
                        "Do you wish to delete all tables? Y/N\n" +
                        "All tables deleted successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //tables
                        "[]\n" +
                        "Input command or 'help' for assistance\n" +
                        //disconnect
                        "Disconnect successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //exit
                        "Good bye!\n", getData());
    }


    @Test
    public void testTruncateWithError() {
        // given
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|sadfasd|fsf|fdsf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable|sadfasd|fsf|fdsf
                "Failure cause: Expected command format 'truncateTable|tableName', but actual 'truncateTable|sadfasd|fsf|fdsf'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testCreateTableWithIllegalParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|" + TABLE_NAME1);
        in.add("y");
        in.add("createTable|" + TABLE_NAME1 + "()|asfdasf|||");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+DB_NAME+"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to delete table '"+TABLE_NAME1+"'. Y/N?\n" +
                "Table '"+TABLE_NAME1+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //creatTable|tableName()||||
                "Failure cause: Expected command format 'createTable|tableName(column1,column2,..,columnN), but actual 'createTable|test1()|asfdasf|||'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testCreateDatabaseWithIllegalParameters() {
        // given

        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("createDatabase|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //createDatabase|
                "Failure cause: Expected command format 'createDatabase|databaseName', but actual 'createDatabase|'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testDropDatabaseWithoutParameters() {
        // given

        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase|
                "Failure cause: Expected command format 'dropDatabase|databaseName', but actual 'dropDatabase|'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testDropTableWithoutParameters() {
        // given

        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //dropTable|
                "Failure cause: Expected command format 'dropTable|tableName', but actual 'dropTable|'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }
}
