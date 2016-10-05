package ru.ivan.sqlcmd.model;

import junit.framework.*;
import org.junit.*;
import org.junit.Test;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;


public class PostgreSQLManagerTest {
    private final static String TABLE_NAME = "test";
    private final static String NOT_EXIST_TABLE = "notExistTable";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
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
        manager.createDatabase(DATABASE_NAME);
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DATABASE_NAME);
        manager.disconnect();
    }

    @Before
    public void setup() {
        manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);
    }

    @After
    public void clear() {
        manager.dropTable(TABLE_NAME);
        //manager.dropAllDatabases();
    }


    @Test
    public void testCreateDatabase() {
        //given

        String databaseName1="testcreatedb1";
        manager.dropDatabase(databaseName1);
        String databaseName2="testcreatedb2";
        manager.dropDatabase(databaseName2);
        manager.createDatabase(databaseName1);
        manager.createDatabase(databaseName2);

        //when
         Set<String> databases = manager.getDatabasesNames();

        //then
        if (!(databases.contains(databaseName1) || databases.contains(databaseName2))) {
            fail();
        }
        manager.dropTable(databaseName1);
        manager.dropTable(databaseName2);
    }
    @Test(expected = DatabaseManagerException.class)
    public void testCreateDatabaseWithInvalidCommand() {
        //given

        String databaseName1="testcreatedb1";
        manager.dropDatabase(databaseName1);
        manager.createDatabase(databaseName1);
        manager.createDatabase(databaseName1);

        //when

        //then

        manager.dropTable(databaseName1);

    }

    @Test
    public void testTruncate() {
        //given
        List<Map<String, Object>> expected = new ArrayList<>();

        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Kevin");
        newData.put("password", "*****");
        newData.put("id", 1);
        manager.insertRow(TABLE_NAME, newData);

        //when
        manager.truncateTable(TABLE_NAME);
        List<Map<String, Object>> actual = manager.getTableRows(TABLE_NAME);

        //then
        junit.framework.Assert.assertEquals(expected, actual);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testTruncateNotExistTable() {
        //when
        manager.truncateTable(NOT_EXIST_TABLE);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToNotExistDatabase() {
        //when
        try {
            manager.connect(NOT_EXIST_TABLE, DB_USER, DB_PASSWORD);
            fail();
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToDatabaseWhenIncorrectUserAndPassword() {
        //when
        try {
            manager.connect(DATABASE_NAME, "notExistUser", "qwertyuiop");
            fail();
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToServerWhenIncorrectUserAndPassword() {
        //when
        try {
            manager.connect("", "notExistUser", "qwertyuiop");
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testCreateTableWrongQuery() {
        //given
        String query = "testTable(qwerty)";

        //when
        manager.createTable(query);
    }

    @Test
    public void testDropDatabase() {
        //given
        String newDatabase = "dropdatabasetest";
        manager.createDatabase(newDatabase);

        //when
        manager.dropDatabase(newDatabase);

        //then
        Set<String> databases = manager.getDatabasesNames();
        if (databases.contains(newDatabase)) {
            fail();
        }
    }
    @Test(expected = DatabaseManagerException.class)
    public void testDropDatabaseWithInvalidCommand() {
        //given

        String databaseName1="postgres";
        manager.dropDatabase(databaseName1);

        //when

        //then

    }

    @Test
    public void testDropTable() {
        //given
        String tableName = "secondTest";
        Set<String> expected = new LinkedHashSet<>(Collections.singletonList(TABLE_NAME));
        manager.createTable(tableName + "(id serial PRIMARY KEY)");

        //when
        manager.dropTable(tableName);

        //then
        Set<String> actual = manager.getTableNames();
        assertEquals(expected, actual);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testDropTableWithInvalidCommand() {
        //given

       manager.dropTable("%%%dfd");

        //when

        //then

    }

    @Test
    public void testGetDatabases() {
        //given
        //when
        Set<String> actual = manager.getDatabasesNames();

        //then
        assertNotNull(actual);
    }

    @Test
    public void testGetTableColumns() {
        //given
        Set<String> expected = new LinkedHashSet<>(Arrays.asList("id", "username", "password"));

        //when
        Set<String> actual = manager.getTableColumns(TABLE_NAME);

        //then
        assertEquals(expected, actual);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testGetTableColumnsOfIllegalTable() {
        //given


        //when
        Set<String> actual = manager.getTableColumns("'adfsf'");

        //then


    }

    @Test
    public void testGetTableNames() {
        //given
        Set<String> expected = new LinkedHashSet<>(Collections.singletonList(TABLE_NAME));

        //when
        Set<String> actual = manager.getTableNames();

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTableSize() {
        //given
        manager.truncateTable(TABLE_NAME);
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Bob");
        newData.put("password", "*****");
        newData.put("id", 1);
        manager.insertRow(TABLE_NAME, newData);

        Integer expected = 1;

        //when
       Integer actual = manager.getTableSize(TABLE_NAME);

        //then
        assertEquals(expected, actual);
        manager.dropTable(TABLE_NAME);
    }

    @Test
    public void testGetTableSizeOfEmptyTable() {
        //given
        manager.truncateTable(TABLE_NAME);
        Integer expected = 0;

        //when
        Integer actual = manager.getTableSize(TABLE_NAME);

        //then
        assertEquals(expected, actual);
        //manager.dropTable(TABLE_NAME);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testGetTableSizeOfIllegalTable() {
        //given
        //when
        Integer actual = manager.getTableSize(NOT_EXIST_TABLE);

        //then

    }

    @Test(expected = DatabaseManagerException.class)
    public void testDeleteFromNotExistTable() {
        //given
        //when
        //then
        manager.deleteRow(NOT_EXIST_TABLE, 1);
    }


    @Test(expected = DatabaseManagerException.class)
    public void testInsertNotExistTable() {
        //given
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Bob");
        newData.put("password", "*****");
        newData.put("id", 1);

        //when
        //then
        manager.insertRow(NOT_EXIST_TABLE, newData);
    }

    @Test
    public void testInsertWithId() {
        //given
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Bob");
        newData.put("password", "*****");
        newData.put("id", 1);

        //when
        manager.insertRow(TABLE_NAME, newData);

        //then
        Map<String, Object> user = manager.getTableRows(TABLE_NAME).get(0);
        assertEquals(newData, user);
    }

    @Test
    public void testUpdate() {
        //given
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "testUser");
        newData.put("password", "azerty");
        newData.put("id", 2);

        manager.insertRow(TABLE_NAME, newData);

        //when
        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("username", "Bill");
        updateData.put("password", "qwerty");
        updateData.put("id", 2);

        manager.updateRow(TABLE_NAME, 2, updateData);

        //then
        Map<String, Object> user = manager.getTableRows(TABLE_NAME).get(0);
        assertEquals(updateData, user);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testUpdateNotExistTable() {
        //when
        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("username", "Bill");
        updateData.put("password", "qwerty");
        updateData.put("id", 1);

        //then
        manager.updateRow(NOT_EXIST_TABLE, 1, updateData);
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }

}
