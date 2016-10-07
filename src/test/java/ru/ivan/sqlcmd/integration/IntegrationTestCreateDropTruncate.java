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
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r"+MainController.LINE_SEPARATOR+"", ""+MainController.LINE_SEPARATOR+"");
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropDatabase
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                "Database '"+DB_TEST2+"' deleted successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //createDatabase
                "Database '"+DB_TEST2+"' created successfully"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //databases
                "***Existing databases***"+MainController.LINE_SEPARATOR+"" +
                "postgres"+MainController.LINE_SEPARATOR+"" +
                ""+ DB_TEST1 +""+MainController.LINE_SEPARATOR+"" +
                ""+DB_TEST2+""+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropDatabase
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                "Database '"+DB_TEST2+"' deleted successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //databases
                "***Existing databases***"+MainController.LINE_SEPARATOR+"" +
                "postgres"+MainController.LINE_SEPARATOR+"" +
                ""+ DB_TEST1 +""+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropDatabase|test1
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                "Database '"+DB_TEST2+"' deleted successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+ MainController.LINE_SEPARATOR+"" +
                //createDatabase
                "Database '"+DB_TEST2+"' created successfully"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //databases
                "***Existing databases***"+MainController.LINE_SEPARATOR+"" +
                "postgres"+MainController.LINE_SEPARATOR+"" +
                ""+DB_TEST1+""+MainController.LINE_SEPARATOR+"" +
                ""+DB_TEST2+""+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropAllaDatabases
                "Do you wish to delete all databases? Y/N"+MainController.LINE_SEPARATOR+"" +
                "All databases  deleted successfully"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //databases
                "***Existing databases***"+MainController.LINE_SEPARATOR+"" +
                "postgres"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropDatabase
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                "Database '"+DB_TEST2+"' deleted successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //createDatabase
                "Database '"+DB_TEST2+"' created successfully"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '"+DB_TEST2+"' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //databases
                "***Existing databases***"+MainController.LINE_SEPARATOR+"" +
                "postgres"+MainController.LINE_SEPARATOR+"" +
                ""+ DB_TEST1 +""+MainController.LINE_SEPARATOR+"" +
                ""+DB_TEST2+""+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropDatabase
                "Do you wish to delete database '"+DB_TEST2+"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                "Error while deleting database '"+DB_TEST2+"'. Cause: 'It is not possible to delete a table '"+DB_TEST2+"''"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
                "Hello, user"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //connect
                        "Connecting to database '"+ DB_TEST1 +"' is successful"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //dropTable
                        "Do you wish to delete table '"+ TABLE_TEST1 +"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                        "Table '"+ TABLE_TEST1 +"' deleted successful"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //createTable
                        "Table '"+ TABLE_TEST1 + " (id INTEGER,name text,password text)' created successfully"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //insertRow
                        "Insert row '{id=1111, name=Peter, password=****}' into table '"+ TABLE_TEST1 +"' successfully"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //rows1
                        "+----+-----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "|id  |name |password|"+MainController.LINE_SEPARATOR+"" +
                        "+----+-----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "|1111|Peter|****    |"+MainController.LINE_SEPARATOR+"" +
                        "+----+-----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //createTable2
                        "Table '"+ TABLE_TEST2 + " (id INTEGER,name text,password text)' created successfully"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //insertRow
                        "Insert row '{id=2222, name=Ivan, password=++++}' into table '"+ TABLE_TEST2 +"' successfully"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //rows2
                        "+----+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "|id  |name|password|"+MainController.LINE_SEPARATOR+"" +
                        "+----+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "|2222|Ivan|++++    |"+MainController.LINE_SEPARATOR+"" +
                        "+----+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //truncateAll
                        "Do you wish to clear all tables?. Y/N"+MainController.LINE_SEPARATOR+"" +
                        "All tables cleared successfully"+MainController.LINE_SEPARATOR+"" +
                        //rows1
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        "+--+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "|id|name|password|"+MainController.LINE_SEPARATOR+"" +
                        "+--+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //rows2
                        "+--+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "|id|name|password|"+MainController.LINE_SEPARATOR+"" +
                        "+--+----+--------+"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //dropAllTables
                        "Do you wish to delete all tables? Y/N"+MainController.LINE_SEPARATOR+"" +
                        "All tables deleted successfully"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //tables
                        "[]"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //disconnect
                        "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                        "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                        //exit
                        "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //truncateTable|sadfasd|fsf|fdsf
                "Failure cause: Expected command format 'truncateTable|tableName', but actual 'truncateTable|sadfasd|fsf|fdsf'"+MainController.LINE_SEPARATOR+"" +
                "Try again"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '"+ DB_TEST1 +"' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //truncateTable
                "Do you wish to delete table '"+ TABLE_TEST1 +"'. Y/N?"+MainController.LINE_SEPARATOR+"" +
                "Table '"+ TABLE_TEST1 +"' deleted successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //creatTable|tableName()||||
                "Failure cause: Expected command format 'createTable|tableName(column1,column2,..,columnN), but actual 'createTable|"+TABLE_TEST1+"()|asfdasf|||'"+MainController.LINE_SEPARATOR+"" +
                "Try again"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //createDatabase|
                "Failure cause: Expected command format 'createDatabase|databaseName', but actual 'createDatabase|'"+MainController.LINE_SEPARATOR+"" +
                "Try again"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropDatabase|
                "Failure cause: Expected command format 'dropDatabase|databaseName', but actual 'dropDatabase|'"+MainController.LINE_SEPARATOR+"" +
                "Try again"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
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
        assertEquals("Hello, user"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //connect
                "Connecting to database '' is successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //dropTable|
                "Failure cause: Expected command format 'dropTable|tableName', but actual 'dropTable|'"+MainController.LINE_SEPARATOR+"" +
                "Try again"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //disconnect
                "Disconnect successful"+MainController.LINE_SEPARATOR+"" +
                "Input command or 'help' for assistance"+MainController.LINE_SEPARATOR+"" +
                //exit
                "Good bye!"+MainController.LINE_SEPARATOR+"", getData());
    }
}
