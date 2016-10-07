package ru.ivan.sqlcmd.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ivan.sqlcmd.Main;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.model.PropertiesLoader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private static final String DB_TEST1 ="db_test";
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
                "Hello, user\n" +
                        "Input command or 'help' for assistance\n" +
                        "Existing program commands:\n" +
                        "\t\tconnect|sqlcmd|postgres|postgres -- connect to database\n" +
                        "\t\tcreateDatabase|databaseName -- create new database\n" +
                        "\t\tcreateTable|tableName -- create new table\n" +
                        "\t\tdatabases -- list of databases\n" +
                        "\t\tdeleteRow|tableName|ID -- delete from table row with specific ID \n" +
                        "\t\tdisconnect -- disconnect from curent database\n" +
                        "\t\tdropTable|tableName -- delete table\n" +
                        "\t\tdropAllTables -- delete all tables of current database\n" +
                        "\t\tdropDatabase|databaseName -- delete database\n" +
                        "\t\tdropAllDatabases -- delete all databases\n" +
                        "\t\texit -- exit from application\n" +
                        "\t\thelp -- list of all commands with description\n" +
                        "\t\thistory -- list of last 'history capacity' inputed commands. history|N - set 'history capacity'\n" +
                        "\t\tinsertRow|tableName|column1|value1|column2|value2|...|columnN|valueN -- insert row in table\n" +
                        "\t\trows|tableName -- list of rows in table\n" +
                        "\t\tsize|tableName -- size of the table\n" +
                        "\t\ttables -- list of tables in current database\n" +
                        "\t\ttruncateAll -- clear all tables\n" +
                        "\t\ttruncateTable|tableName -- clear the table\n" +
                        "\t\tupdateRow|tableName|ID -- update row with specific ID in table\n" +
                        "Input command or 'help' for assistance\n" +
                        "Good bye!\n", getData());
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
    public void testExit() {
        // given

        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                "Good bye!\n", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //tables
                "You can't use 'tables'. First connect with the command 'connect|database|user|password'\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testRowsWithoutConnect() {
        // given
        in.add("rows|" + TABLE_TEST1);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //rows|TABLE_TEST1
                "You can't use 'rows|"+ TABLE_TEST1 +"'. First connect with the command 'connect|database|user|password'\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //unsupported
                "You can't use 'unsupported'. First connect with the command 'connect|database|user|password'\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //unsupported
                "This command does not exist - unsupported\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //tables
                "["+TABLE_TEST2+", "+TABLE_TEST1+"]\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testSizeAfterConnect() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|"+ TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncate
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //size
                "Table '"+ TABLE_TEST1 +"' size is 1\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncate table
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insert row
                "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                "Failure cause: Must be 2 parameters in format size|tableName\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testSizeWithTwoParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|"+ TABLE_TEST1 +"|afa|adfasf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncate table
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                "Failure cause: Must be 2 parameters in format size|tableName\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testSizeWithIllegalTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("size|"+NOT_EXIST_TABLE);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=1111, name=Peter, password=****}' into table '"+TABLE_TEST1+"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //size|NOT_EXIST_TABLE
                "Failure cause: It is not possible to obtain the size of the table '"+NOT_EXIST_TABLE+"' ОШИБКА: отношение \"notexisttable\" не существует\n" +
                "  Позиция: 32\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //wxit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Failure cause: Unable to connect to database '"+DB_TEST1+"', user 'postgres', password 'postgresWRONG' Ошибка при попытке подсоединения.\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
                "Hello, user\n" +
                        "Input command or 'help' for assistance\n" +
                        //connect
                        "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //truncateTable
                        "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                        "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //insertRow
                        "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //rows
                        "+----+-----+--------+\n" +
                        "|id  |name |password|\n" +
                        "+----+-----+--------+\n" +
                        "|1111|Peter|****    |\n" +
                        "+----+-----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //truncateTable
                        "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                        "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //rows
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //disconnect
                        "Disconnect successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //exit
                        "Good bye!\n", getData());
    }

    @Test
    public void testRowsWithIllegalParameters() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_TEST1 +"|dsfaf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello, user\n" +
                        "Input command or 'help' for assistance\n" +
                        //connect
                        "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //truncate table
                        "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                        "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //insert row
                        "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        // rows|table|sdfasf
                        "Failure cause: Must be 2 parameters in format rows|tableName\n" +
                        "Try again\n" +
                        "Input command or 'help' for assistance\n" +
                        //disconnect
                        "Disconnect successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //exit
                        "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //tables
                "["+TABLE_TEST2+", "+TABLE_TEST1+"]\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST2 +"' is successful\n" +
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
    public void testConnectWithError() {
        // given
        in.add("connect|" + DB_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect|dbname
                "Failure cause: Number of parameters, splitting by '|' - 2. Expected - 4\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=14, name=Pupkin, password=+++++}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //rows
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Stiven|*****   |\n" +
                "+--+------+--------+\n" +
                "|14|Pupkin|+++++   |\n" +
                "+--+------+--------+\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                "Failure cause: Cannot insert a row into a table '"+NOT_EXIST_TABLE+"'.  Table does not exists\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Failure cause: Cannot insert a row into a table '"+ TABLE_TEST1 +"'. Столбец \"id5\" в таблице \""+TABLE_TEST1+"\" не существует\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow|
                "Failure cause: Expect command parameters в формате 'insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //history|3
                "Size of commands history set to '3' \n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //asd
                "This command does not exist - asd\n" +
                "Input command or 'help' for assistance\n" +
                //help
                "2. connect|"+ DB_TEST1 +"|"+DB_USER+"|"+DB_PASSWORD+"\n" +
                "3. asd\n" +
                "4. history\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //history|gfh
                "Failure cause: Size of commands history must be numeric, but actual 'gfh'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                "Good bye!\n", getData());
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
        in.add("truncateTable|"+ TABLE_TEST1);
        in.add("y");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncate table
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insert row
                "Insert row '{id=13, name=Stiven, password=*****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //rows
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Stiven|*****   |\n" +
                "+--+------+--------+\n" +
                "Input command or 'help' for assistance\n"+
                //update row
                "Update row '{name=Pupkin, password=+++++}' in table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Pupkin|+++++   |\n" +
                "+--+------+--------+\n" +
                "Input command or 'help' for assistance\n" +
                //truncate table
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //updateRow|notExistTable
                "Failure cause: It is not possible to update a record with id=14 in table 'notexisttable'. Table does not exists\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                "Failure cause: It is not possible to update a record with id=14 in table '"+ TABLE_TEST1 +"'.Столбец \"nam\" в таблице \""+ TABLE_TEST1 +"\" не существует\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //updateRow|
                "Failure cause: Must be not even parameters equal to or greater than 4 in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //updateRow|tableName
                "Failure cause: Must be not even parameters equal to or greater than 4 in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow|tableName|id
                "Failure cause: Must be not even parameters equal to or greater than 4 in format updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //updateRow|tableName|fgr|name|Ivan
                "Failure cause: 3 parameter must be numeric!\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testDeleteRowInTable() {
        // given
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_TEST1);
        in.add("y");
        in.add("insertRow|" + TABLE_TEST1 + "|id|13|name|Stiven|password|*****");
        in.add("insertRow|" + TABLE_TEST1 + "|id|14|name|Kevin|password|-----");
        in.add("rows|"+ TABLE_TEST1);
        in.add("deleteRow|" + TABLE_TEST1 + "|13");
        in.add("rows|" + TABLE_TEST1);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncate table
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insert row in table
                "Insert row '{id=13, name=Stiven, password=*****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //insert row
                "Insert row '{id=14, name=Kevin, password=-----}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //rows of table
                        "+--+------+--------+\n" +
                        "|id|name  |password|\n" +
                        "+--+------+--------+\n" +
                        "|13|Stiven|*****   |\n" +
                        "+--+------+--------+\n" +
                        "|14|Kevin |-----   |\n" +
                        "+--+------+--------+\n"+
                "Input command or 'help' for assistance\n" +
                //deleteRow 13
                "Delete row '13' from table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //rows of table
                "+--+-----+--------+\n" +
                "|id|name |password|\n" +
                "+--+-----+--------+\n" +
                "|14|Kevin|-----   |\n" +
                "+--+-----+--------+\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                "Delete row '14' from table '"+TABLE_TEST1+"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Stiven|*****   |\n" +
                "+--+------+--------+\n" +
                "Input command or 'help' for assistance\n" +
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //deletRow|notExistTable
                "Failure cause: It is not possible to delete a record with id=13 in table '"+NOT_EXIST_TABLE+"'. Table does not exists\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //deleteRow|
                "Failure cause: Expected command format 'deleteRow|tableName|ID', but actual 'deleteRow|'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //deleteRow|tableName
                "Failure cause: Expected command format 'deleteRow|tableName|ID', but actual 'deleteRow|"+TABLE_TEST1+"'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //truncateTable
                "Do you wish to clear table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' cleared successful\n" +
                "Input command or 'help' for assistance\n" +
                //insertRow
                "Insert row '{id=13, name=Stiven, password=*****}' into table '"+TABLE_TEST1+"' successfully\n" +
                "Input command or 'help' for assistance\n" +
                //"deleteRow|" + TABLE_TEST1 + "|13|adsf|asdfa");
                "Failure cause: Expected command format 'deleteRow|tableName|ID', but actual 'deleteRow|"+TABLE_TEST1+"|13|adsf|asdfa'\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
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
        assertEquals("Hello, user\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //deleteRow|tableName|fgr
                "Failure cause: 3 parameter must be numeric!\n" +
                "Try again\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }
}
