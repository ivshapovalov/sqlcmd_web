package ru.ivan.sqlcmd.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ivan.sqlcmd.Main;
import ru.ivan.sqlcmd.controller.MainController;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.model.PropertiesLoader;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private static final String DB_TEST1 = "db_test";
    private final static String TABLE_TEST1 = "table_test1";
    private final static String NOT_EXIST_TABLE = "notexisttable";
    private final static String SQL_CREATE_TABLE = TABLE_TEST1 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String DB_TEST2 = "db_test2";
    private final static String TABLE_TEST2 = "table_test2";
    private final static String SQL_CREATE_TABLE2 = TABLE_TEST2 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";

    private static final PropertiesLoader pl = new PropertiesLoader();
    private static final String DB_USER = pl.getUserName();
    private static final String DB_PASSWORD = pl.getPassword();

    private static DatabaseManager manager;
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_TEST1);
        manager.dropDatabase(DB_TEST2);

        manager.createDatabase(DB_TEST1);
        manager.createDatabase(DB_TEST2);
        manager.disconnect();

        manager.connect(DB_TEST1, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);
        manager.createTable(SQL_CREATE_TABLE2);
        manager.disconnect();
    }

    @AfterClass
    public static void clearAfterAllTests() {

        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_TEST1);
        manager.dropDatabase(DB_TEST2);
    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello, user" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        "Existing program commands:" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'connect|sqlcmd|postgres|postgres' -- connect to database" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'createDatabase|databaseName' -- create new database" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'createTable|tableName()' -- create new table" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'databases' -- list of databases" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'deleteRow|tableName|ID' -- delete from table row with specific ID " + MainController.LINE_SEPARATOR + "" +
                        "\t\t'disconnect' -- disconnect from current database" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'dropTable|tableName' -- delete table" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'dropAllTables' -- delete all tables of current database" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'dropDatabase|databaseName' -- delete database" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'dropAllDatabases' -- delete all databases" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'exit' -- exit from application" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'help' -- list of all commands with description" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'history' -- list of last 'history capacity' inputted commands. history|N - set 'history capacity'" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN' -- insert row in table" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'rows|tableName' -- list of rows in table" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'size|tableName' -- size of the table" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'tables' -- list of tables in current database" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'truncateAll' -- clear all tables" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'truncateTable|tableName' -- clear the table" + MainController.LINE_SEPARATOR + "" +
                        "\t\t'updateRow|tableName|ID' -- update row with specific ID in table" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r" + MainController.LINE_SEPARATOR + "", "" + MainController.LINE_SEPARATOR + "");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testExit() {
        // given

        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //tables
                "You can't use 'tables'. First connect with the command 'connect|database|user|password'" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testRowsWithoutConnect() {
        // given
        in.add("rows|" + TABLE_TEST1);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //rows|TABLE_TEST1
                "You can't use 'rows|" + TABLE_TEST1 + "'. First connect with the command 'connect|database|user|password'" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //unsupported
                "You can't use 'unsupported'. First connect with the command 'connect|database|user|password'" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("unsupported");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //unsupported
                "This command does not exist - unsupported" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }


    @Test
    public void testTablesAfterConnect() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //tables
                "[" + TABLE_TEST2 + ", " + TABLE_TEST1 + "]" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testSizeAfterConnect() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|" + TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncate
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=1111, name=Peter, password=****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //size
                "Table '" + TABLE_TEST1 + "' size is 1" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testSizeWithoutParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncate table
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insert row
                "Insert row '{id=1111, name=Peter, password=****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Failure cause: Must be 2 parameters in format size|tableName" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testSizeWithTwoParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|" + TABLE_TEST1 + "|afa|adfasf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncate table
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Insert row '{id=1111, name=Peter, password=****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Failure cause: Must be 2 parameters in format size|tableName" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testSizeWithIllegalTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|" + NOT_EXIST_TABLE);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=1111, name=Peter, password=****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //size|NOT_EXIST_TABLE
                "Failure cause: It is not possible to obtain the size of the table '" + NOT_EXIST_TABLE + "' ОШИБКА: отношение \"notexisttable\" не существует" + MainController.LINE_SEPARATOR + "" +
                "  Позиция: 32" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testBadConnect() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD + "WRONG");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Failure cause: Unable to connect to database '" + DB_TEST1 + "', user 'postgres', password 'postgresWRONG' Ошибка при попытке подсоединения." + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Failure cause: Disconnect failed. You are not connected to any Database" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testRowsAfterTruncate() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_TEST1);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("rows|" + TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello, user" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //connect
                        "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //truncateTable
                        "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                        "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //insertRow
                        "Insert row '{id=1111, name=Peter, password=****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //rows
                        "+----+-----+--------+" + MainController.LINE_SEPARATOR + "" +
                        "|id  |name |password|" + MainController.LINE_SEPARATOR + "" +
                        "+----+-----+--------+" + MainController.LINE_SEPARATOR + "" +
                        "|1111|Peter|****    |" + MainController.LINE_SEPARATOR + "" +
                        "+----+-----+--------+" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //truncateTable
                        "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                        "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //rows
                        "+--+----+--------+" + MainController.LINE_SEPARATOR + "" +
                        "|id|name|password|" + MainController.LINE_SEPARATOR + "" +
                        "+--+----+--------+" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //disconnect
                        "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //exit
                        "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testRowsWithIllegalParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_TEST1 + "|dsfaf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello, user" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //connect
                        "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //truncate table
                        "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                        "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //insert row
                        "Insert row '{id=1111, name=Peter, password=****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        // rows|table|sdfasf
                        "Failure cause: Must be 2 parameters in format rows|tableName" + MainController.LINE_SEPARATOR + "" +
                        "Try again" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //disconnect
                        "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                        "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                        //exit
                        "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }


    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("connect|" + DB_TEST2 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //tables
                "[" + TABLE_TEST2 + ", " + TABLE_TEST1 + "]" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST2 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //tables
                "[]" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|" + DB_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect|dbname
                "Failure cause: Number of parameters, splitting by '|' - 2. Expected - 4" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Failure cause: Disconnect failed. You are not connected to any Database" +MainController.LINE_SEPARATOR+""+
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testInsertRowInTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");

        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("insertRow|" + TABLE_TEST1 + "|id|14|name|Pupkin|password|+++++");
        in.add("rows|" + TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=14, name=Pupkin, password=+++++}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //rows
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|id|name  |password|" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|13|Stiven|*****   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|14|Pupkin|+++++   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testInsertRowInIllegalTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("insertRow|" + NOT_EXIST_TABLE + "|id|13|name|Stiven|password|*****");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Failure cause: Cannot insert a row into a table '" + NOT_EXIST_TABLE + "'.  Table does not exists" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testInsertRowWithIllegalData() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id5|13|namef|Stiven|password|*****");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Failure cause: Cannot insert a row into a table '" + TABLE_TEST1 + "'. Столбец \"id5\" в таблице \"" + TABLE_TEST1 + "\" не существует" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testInsertRowWithoutParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow|
                "Failure cause: Expect command parameters в формате 'insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN'" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testHistory() {
        // given
        in.add("history|3");
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("asd");
        in.add("history");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //history|3
                "Size of commands history set to '3' " + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //asd
                "This command does not exist - asd" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //help
                "2. connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD + "" + MainController.LINE_SEPARATOR + "" +
                "3. asd" + MainController.LINE_SEPARATOR + "" +
                "4. history" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testHistoryWithNotNumericCapacity() {
        // given
        in.add("history|gfh");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //history|gfh
                "Failure cause: Size of commands history must be numeric, but actual 'gfh'" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Failure cause: Disconnect failed. You are not connected to any Database" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }


    @Test
    public void testUpdateRowInTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("rows|" + TABLE_TEST1);
        in.add("updateRow|" + TABLE_TEST1 + "|13|name|Pupkin|password|+++++");
        in.add("rows|" + TABLE_TEST1);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncate table
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insert row
                "Insert row '{id=13, name=Stiven, password=*****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //rows
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|id|name  |password|" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|13|Stiven|*****   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //update row
                "Update row '{name=Pupkin, password=+++++}' in table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|id|name  |password|" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|13|Pupkin|+++++   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncate table
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUpdateRowInNotExistingTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + NOT_EXIST_TABLE + "|14|name|Pupkin|password|+++++");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //updateRow|notExistTable
                "Failure cause: It is not possible to update a record with id=14 in table 'notexisttable'. Table does not exists" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUpdateRowWithNotExistingColumnInTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + TABLE_TEST1 + "|14|nam|Pupkin|password|+++++");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Failure cause: It is not possible to update a record with id=14 in table '" + TABLE_TEST1 + "'.Столбец \"nam\" в таблице \"" + TABLE_TEST1 + "\" не существует" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUpdateRowWithoutParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //updateRow|
                "Failure cause: Must be not even parameters equal to or greater than 4 in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUpdateRowWithTwoParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + TABLE_TEST1 + "");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //updateRow|tableName
                "Failure cause: Must be not even parameters equal to or greater than 4 in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUpdateRowWithThreeParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("updateRow|" + TABLE_TEST1 + "|13|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow|tableName|id
                "Failure cause: Must be not even parameters equal to or greater than 4 in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testUpdateRowWithNotNumericID() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + TABLE_TEST1 + "|fgr|name|Ivan");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //updateRow|tableName|fgr|name|Ivan
                "Failure cause: 3 parameter must be numeric!" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteRowInTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("insertRow|" + TABLE_TEST1 + "|id|14|name|Kevin|password|-----");
        in.add("rows|" + TABLE_TEST1);
        in.add("deleteRow|" + TABLE_TEST1 + "|13");
        in.add("rows|" + TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncate table
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insert row in table
                "Insert row '{id=13, name=Stiven, password=*****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insert row
                "Insert row '{id=14, name=Kevin, password=-----}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //rows of table
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|id|name  |password|" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|13|Stiven|*****   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|14|Kevin |-----   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //deleteRow 13
                "Delete row '13' from table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //rows of table
                "+--+-----+--------+" + MainController.LINE_SEPARATOR + "" +
                "|id|name |password|" + MainController.LINE_SEPARATOR + "" +
                "+--+-----+--------+" + MainController.LINE_SEPARATOR + "" +
                "|14|Kevin|-----   |" + MainController.LINE_SEPARATOR + "" +
                "+--+-----+--------+" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteNotExistingRowInTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("deleteRow|" + TABLE_TEST1 + "|14");
        in.add("rows|" + TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Delete row '14' from table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|id|name  |password|" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "|13|Stiven|*****   |" + MainController.LINE_SEPARATOR + "" +
                "+--+------+--------+" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteRowInNotExistingTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|" + NOT_EXIST_TABLE + "|13");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //deletRow|notExistTable
                "Failure cause: It is not possible to delete a record with id=13 in table '" + NOT_EXIST_TABLE + "'. Table does not exists" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteRowWithoutParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //deleteRow|
                "Failure cause: Expected command format 'deleteRow|tableName|ID', but actual 'deleteRow|'" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteRowWithTwoParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|" + TABLE_TEST1 + "");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //deleteRow|tableName
                "Failure cause: Expected command format 'deleteRow|tableName|ID', but actual 'deleteRow|" + TABLE_TEST1 + "'" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteWithMoreThanThreeParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("deleteRow|" + TABLE_TEST1 + "|13|adsf|asdfa");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //truncateTable
                "Do you wish to clear table '" + TABLE_TEST1 + "'. Y/N?" + MainController.LINE_SEPARATOR + "" +
                "Table '" + TABLE_TEST1 + "' cleared successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '" + TABLE_TEST1 + "' successfully" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //"deleteRow|" + TABLE_TEST1 + "|13|adsf|asdfa");
                "Failure cause: Expected command format 'deleteRow|tableName|ID', but actual 'deleteRow|" + TABLE_TEST1 + "|13|adsf|asdfa'" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }

    @Test
    public void testDeleteRowWithNotNumericID() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|" + TABLE_TEST1 + "|fgr");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //connect
                "Connecting to database '" + DB_TEST1 + "' is successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //deleteRow|tableName|fgr
                "Failure cause: 3 parameter must be numeric!" + MainController.LINE_SEPARATOR + "" +
                "Try again" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //disconnect
                "Disconnect successful" + MainController.LINE_SEPARATOR + "" +
                "Input command or 'help' for assistance" + MainController.LINE_SEPARATOR + "" +
                //exit
                "Good bye!" + MainController.LINE_SEPARATOR + "", getData());
    }
}
