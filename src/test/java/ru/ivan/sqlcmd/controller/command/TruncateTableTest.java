package ru.ivan.sqlcmd.controller.command;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ru.ivan.sqlcmd.controller.command.Command;
import ru.ivan.sqlcmd.controller.command.TruncateTable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.integration.ConfigurableInputStream;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.model.PropertiesLoader;
import ru.ivan.sqlcmd.view.View;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class TruncateTableTest {

    private View view;
    private Command command;

    private final static String DB_NAME = "dbtest";
    private final static String TABLE_NAME = "tabletest";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String DB_NAME2 = "test";
    private final static String TABLE_NAME2 = "qwe";
    private final static String SQL_CREATE_TABLE2 = TABLE_NAME2 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";

    private static DatabaseManager manager;
    private static final PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();


    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);
        manager.disconnect();

    }

    @AfterClass
    public static void clearAfterAllTests() {

        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
    }

    @Before
    public void setup() {
        manager=Mockito.mock(DatabaseManager.class);
        view=Mockito.mock(View.class);
        command =new TruncateTable(manager,view);
    }

    @Test
    public void testTruncateTableWithParametersLessThen2() {
        //when
        try {
            command.process("truncateTable");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'truncateTable|tableName', а ты ввел: truncateTable",e.getMessage());
        }
    }
    @Test
    public void testTruncateTableWithParametersMoreThen2() {
        //when
        try {
            command.process("truncateTable|users|qwe");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'truncateTable|tableName', а ты ввел: truncateTable|users|qwe",e.getMessage());
        }
    }

    @Test
    public void testCanProcessTruncateWithParametersString() {
        //given

        //when
        Boolean canProcess=command.canProcess("truncateTable|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessTruncateWithoutParametersString() {
        //given
        //when
        Boolean canProcess=command.canProcess("truncateTable");

        //then
        assertFalse(canProcess);
    }


}
