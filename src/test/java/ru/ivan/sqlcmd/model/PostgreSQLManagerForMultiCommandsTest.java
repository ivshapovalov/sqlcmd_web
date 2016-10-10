package ru.ivan.sqlcmd.model;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PostgreSQLManagerForMultiCommandsTest {
    private final static String DB_NAME = "db_test";
    private final static String TABLE_TEST1 = "table_test1";
    private final static String TABLE_TEST2 = "table_test2";
    private final static String SQL_CREATE_TABLE1 = TABLE_TEST1 + " (id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String SQL_CREATE_TABLE2 = TABLE_TEST2 + " (id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private static final PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();

    private static DatabaseManager manager;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.disconnect();
    }

    @Before
    public void setup() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.dropTable(TABLE_TEST1);
        manager.dropTable(TABLE_TEST2);
        manager.createTable(SQL_CREATE_TABLE1);
        manager.createTable(SQL_CREATE_TABLE2);

    }

    @After
    public void clear() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
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
        manager.insertRow(TABLE_TEST1, newData);
        manager.insertRow(TABLE_TEST2, newData);

        //when
        manager.truncateAllTables();
        List<Map<String, Object>> actual1 = manager.getTableRows(TABLE_TEST1);
        List<Map<String, Object>> actual2 = manager.getTableRows(TABLE_TEST2);
        for (Map<String, Object> map : actual1
                ) {
            actual2.add(map);
        }
        //then
        assertEquals(expected, actual2);
    }

    @Test  @Ignore ("Тест удаляет ВСЕ базы данных")
    public void testDropAllDatabases() {
        manager.connect("", DB_USER, DB_PASSWORD);
        //given
        String newDatabase1 = "dropdatabasetest1";
        manager.dropDatabase(newDatabase1);
        manager.createDatabase(newDatabase1);
        String newDatabase2 = "dropdatabasetest2";
        manager.dropDatabase(newDatabase2);
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
        String tableName2 = "table2";
        manager.dropTable(tableName1);
        manager.dropTable(tableName2);
        manager.createTable(tableName1 + "(id serial PRIMARY KEY)");
        manager.createTable(tableName2 + "(id serial PRIMARY KEY)");

        //when
        manager.dropAllTables();

        //then
        Set<String> actual = manager.getTableNames();
        assertEquals(expected, actual);
    }
}
