package ru.ivan.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.model.PropertiesLoader;
import ru.ivan.sqlcmd.model.entity.UserAction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:test-application-context.xml"))
public class ServiceTest {

    private static final PropertiesLoader pl = new PropertiesLoader();
    private static final String DB_NAME_DEFAULT = pl.getDatabaseName();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();

    @Autowired
    private Service service;

    DatabaseManager manager = new PostgreSQLManager();

    @Test
    public void testCommandList() {
        assertEquals("[connect, " +
                      "create-table, " +
                      "tables, " +
                      "create-database, " +
                      "delete-database]", service.getMainMenu(manager).toString());
    }

    @Test
    public void testConnect() {
        manager = service.connect(DB_NAME_DEFAULT, DB_USER, DB_PASSWORD);
        assertNotNull(manager);
    }

    @Test(expected = Exception.class)
    public void testConnect_WithIncorrectData() throws Exception {
        service.connect("qwe", "qwe", "qwe");
    }

    @Test
    public void testAllFor() throws Exception {
        manager.connect("sqlcmd_log", "postgres", "postgres");
        manager.truncateTable("user_actions");
        DatabaseManager mockManager = mock(DatabaseManager.class);
        when(mockManager.getUserName()).thenReturn("postgres");

        service.createDatabase(mockManager, "mockDatabase");
        service.dropDatabase(mockManager, "mockDatabase");
        when(mockManager.getUserName()).thenReturn("other");
        service.dropTable(mockManager, "mockTableName");
        List<UserAction> userActions = service.getAllActionsOfUser(DB_USER, DB_NAME_DEFAULT);

        List<String> actions = new LinkedList<>();
        for (int index = 0; index < userActions.size(); index++) {
            actions.add(index, userActions.get(index).getAction());
        }
        assertEquals("[CREATE DATABASE ( 'mockDatabase' ), " +
                      "DELETE DATABASE ( 'mockDatabase' )]", actions.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllFor_WithNullName() throws Exception {
        service.getAllActionsOfUser(null,null);
    }

    @Test
    public void testLogger() throws Exception {
        manager.connect("sqlcmd_log", "postgres", "postgres");
        manager.truncateTable("user_actions");
        manager.truncateTable("database_connection");
        DatabaseManager mockManager = mock(DatabaseManager.class);
        when(mockManager.getDatabaseName()).thenReturn("sqlcmd");
        when(mockManager.getUserName()).thenReturn("postgres");

        manager = service.connect(DB_NAME_DEFAULT, DB_USER, DB_PASSWORD);
        service.truncateTable(mockManager, "mockTable");
        service.tables(mockManager);
        service.rows(mockManager, "mockTable");
        service.createDatabase(mockManager, "mockDatabase");
        service.dropDatabase(mockManager, "mockDatabase");
        service.deleteRow(mockManager, "mockTable", 0);
        service.dropTable(mockManager, "mockTable");
        service.insertRow(mockManager, "mockTable", new HashMap<>());
        service.createTable(mockManager, "mockTable");
        service.updateRow(mockManager, "mockTable", "mockKeyName", "mockKeyValue",
                                                      new HashMap<>());

        service.connect("sqlcmd_log", "postgres", "postgres");
        List<List<String>> actions = service.rows(manager,"user_actions");
        for (List<String> row : actions) {
            row.remove(0);
        }
        List<List<String>> databaseConnection = service.rows(manager,"database_connection");
        String id1 = databaseConnection.get(0).get(0);
        String id2 = databaseConnection.get(1).get(0);
        assertEquals("[[" + id1 + ", sqlcmd, postgres], " +
                      "[" + id2 + ", sqlcmd_log, postgres]]", databaseConnection.toString());
        assertEquals("[[CONNECT, " + id1 + "], " +
                      "[CLEAR TABLE ( mockTable ), " + id1 + "], " +
                      "[GET TABLES LIST, " + id1 + "], " +
                      "[GET TABLE ( mockTable ), " + id1 + "], "  +
                      "[CREATE DATABASE ( mockDatabase ), " + id1 + "], " +
                      "[DELETE DATABASE ( mockDatabase ), " + id1 + "], " +
                      "[DELETE RECORD IN TABLE ( mockTable ) KEY = mockKeyValue, " + id1 + "], " +
                      "[DELETE TABLE ( mockTable ), " + id1 + "], " +
                      "[CREATE RECORD IN TABLE ( mockTable ), " + id1 + "], " +
                      "[CREATE TABLE ( mockTable ), " + id1 + "], " +
                      "[UPDATE RECORD IN TABLE ( mockTable ) KEY = mockKeyValue, " + id1 + "], " +
                      "[CONNECT, " + id2 + "]]", actions.toString());
    }
}
