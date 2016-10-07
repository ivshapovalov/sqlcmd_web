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

public class IntegrationTestCreateDropTruncate {

    private final static String DB_TEST1 = "db_test1";
    private final static String DB_TEST2 = "db_test2";
    private final static String TABLE_TEST1 = "table_test1";
    private final static String TABLE_TEST2 = "table_test2";
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
        manager.dropDatabase(DB_TEST1);
        manager.createDatabase(DB_TEST1);
        manager.disconnect();
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_TEST1);
        manager.dropDatabase(DB_TEST2);
        manager.disconnect();
    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_TEST1);
        manager.createDatabase(DB_TEST1);
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
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + DB_TEST2);
        in.add("y");
        in.add("createDatabase|" + DB_TEST2);
        in.add("databases");
        in.add("dropDatabase|" + DB_TEST2);
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
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?\n" +
                "Database '"+DB_TEST2+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //createDatabase
                "Database '"+DB_TEST2+"' created successfully\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                ""+ DB_TEST1 +"\n" +
                ""+DB_TEST2+"\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?\n" +
                "Database '"+DB_TEST2+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                ""+ DB_TEST1 +"\n" +
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

        // given
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + DB_TEST2);
        in.add("y");
        in.add("createDatabase|" + DB_TEST2);
        in.add("databases");
        in.add("dropAllDatabases");
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
                //dropDatabase|test1
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?\n" +
                "Database '"+DB_TEST2+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //createDatabase
                "Database '"+DB_TEST2+"' created successfully\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                ""+DB_TEST1+"\n" +
                ""+DB_TEST2+"\n" +
                "Input command or 'help' for assistance\n" +
                //dropAllaDatabases
                "Do you wish to delete all databases? Y/N\n" +
                "All databases  deleted successfully\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                "Input command or 'help' for assistance\n" +
                //disconnect
                "Disconnect successful\n" +
                "Input command or 'help' for assistance\n" +
                //exit
                "Good bye!\n", getData());
    }

    @Test
    public void testDropCurrentDatabase() {
        // given
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + DB_TEST2);
        in.add("y");
        in.add("createDatabase|" + DB_TEST2);
        in.add("connect|" + DB_TEST2 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("databases");
        in.add("dropDatabase|"+DB_TEST2);
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
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?\n" +
                "Database '"+DB_TEST2+"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //createDatabase
                "Database '"+DB_TEST2+"' created successfully\n" +
                "Input command or 'help' for assistance\n" +
                //connect
                "Connecting to database '"+DB_TEST2+"' is successful\n" +
                "Input command or 'help' for assistance\n" +
                //databases
                "***Existing databases***\n" +
                "postgres\n" +
                ""+ DB_TEST1 +"\n" +
                ""+DB_TEST2+"\n" +
                "Input command or 'help' for assistance\n" +
                //dropDatabase
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?\n" +
                "Error while deleting database '"+DB_TEST2+"'. Cause: 'It is not possible to delete a table '"+DB_TEST2+"''\n" +
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
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|" + TABLE_TEST1);
        in.add("y");
        in.add("createTable|" + TABLE_TEST1 + " (id INTEGER,name text,password text)");
        in.add("insertRow|" + TABLE_TEST1 + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_TEST1);
        in.add("createTable|" + TABLE_TEST2 + " (id INTEGER,name text,password text)");
        in.add("insertRow|" + TABLE_TEST2 + "|id|2222|name|Ivan|password|++++");
        in.add("rows|" + TABLE_TEST2);
        in.add("truncateAllTables");
        in.add("y");
        in.add("rows|" + TABLE_TEST1);
        in.add("rows|" + TABLE_TEST2);
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
                        "Connecting to database '"+ DB_TEST1 +"' is successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //dropTable
                        "Do you wish to delete table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                        "Table '"+ TABLE_TEST1 +"' deleted successful\n" +
                        "Input command or 'help' for assistance\n" +
                        //createTable
                        "Table '"+ TABLE_TEST1 + " (id INTEGER,name text,password text)' created successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //insertRow
                        "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //rows1
                        "+----+-----+--------+\n" +
                        "|id  |name |password|\n" +
                        "+----+-----+--------+\n" +
                        "|1111|Peter|****    |\n" +
                        "+----+-----+--------+\n" +
                        "Input command or 'help' for assistance\n" +
                        //createTable2
                        "Table '"+ TABLE_TEST2 + " (id INTEGER,name text,password text)' created successfully\n" +
                        "Input command or 'help' for assistance\n" +
                        //insertRow
                        "Insert row '{id=2222, name=Ivan, password=++++}' into table '"+ TABLE_TEST2 +"' successfully\n" +
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
        in.add("connect|" + DB_TEST1 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|" + TABLE_TEST1);
        in.add("y");
        in.add("createTable|" + TABLE_TEST1 + "()|asfdasf|||");
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
                "Do you wish to delete table '"+ TABLE_TEST1 +"'. Y/N?\n" +
                "Table '"+ TABLE_TEST1 +"' deleted successful\n" +
                "Input command or 'help' for assistance\n" +
                //creatTable|tableName()||||
                "Failure cause: Expected command format 'createTable|tableName(column1,column2,..,columnN), but actual 'createTable|"+TABLE_TEST1+"()|asfdasf|||'\n" +
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
