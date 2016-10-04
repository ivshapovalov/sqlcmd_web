package ru.ivan.sqlcmd.model;

import org.junit.*;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;


public class PostgreSQLManagerForMultiCommandsTest {
    private final static String TABLE_NAME1 = "test1";
    private final static String TABLE_NAME2 = "test2";
    private final static String NOT_EXIST_TABLE = "notExistTable";
    private final static String SQL_CREATE_TABLE1 = TABLE_NAME1 + " (id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String SQL_CREATE_TABLE2 = TABLE_NAME2 + " (id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private static final PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    private final static String DATABASE_NAME = pl.getDatabaseName();
    private static DatabaseManager manager;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DATABASE_NAME);
        //manager.createDatabase(DATABASE_NAME);

    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DATABASE_NAME);
        manager.disconnect();
    }

    @Before
    public void setup() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE1);
        manager.createTable(SQL_CREATE_TABLE2);

    }

    @After
    public void clear() {
        manager.dropTable(TABLE_NAME1);
        manager.dropTable(TABLE_NAME2);
        manager.dropAllDatabases();
        manager.disconnect();
    }

    @Test
    public void testTruncateAllTables() {
        //given
        List<Map<String, Object>> expected = new ArrayList<>();

        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Kevin");
        newData.put("password", "*****");
        newData.put("id", 1);
        manager.insertRow(TABLE_NAME1, newData);
        manager.insertRow(TABLE_NAME2, newData);

        //when
        manager.truncateAllTables();
        List<Map<String, Object>> actual1 = manager.getTableRows(TABLE_NAME1);
        List<Map<String, Object>> actual2 = manager.getTableRows(TABLE_NAME2);
        for (Map<String, Object> map : actual1
                ) {
            actual2.add(map);
        }
        //then
        assertEquals(expected, actual2);
    }

    @Test
    public void testDropAllDatabases() {

        //given
        String newDatabase1 = "dropdatabasetest1";
        manager.createDatabase(newDatabase1);
        String newDatabase2 = "dropdatabasetest2";
        manager.createDatabase(newDatabase2);

        //when
        manager.dropAllDatabases();

        //then
        Set<String> databases = manager.getDatabasesNames();
        if (databases.size() != 1 && databases.contains("postgres")) {
            fail();
        }


    }

    @Test
    public void testDropAllTables() {
        //given
        Set<String> expected = new LinkedHashSet<>();
        String tableName1 = "table1";
        manager.createTable(tableName1 + "(id serial PRIMARY KEY)");
        String tableName2 = "table1";
        manager.createTable(tableName2 + "(id serial PRIMARY KEY)");

        //when
        manager.dropAllTables();

        //then
        Set<String> actual = manager.getTableNames();
        assertEquals(expected, actual);
    }

}
