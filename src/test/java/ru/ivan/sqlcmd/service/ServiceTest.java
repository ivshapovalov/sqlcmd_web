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
    private static final String DB_NAME_DEFAULT = "sqlcmd";
    private static final String DB_NAME_LOG = "sqlcmd_log";
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    DatabaseManager manager = new PostgreSQLManager();
    @Autowired
    private Service service;

    @Test
    public void testCommandList() {
        assertEquals("[help, connect, databases, tables, disconnect, actions]",
                service.getMainMenu(manager).toString());
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
    public void testAllActionsOfUser() throws Exception {
        manager.connect(DB_NAME_LOG, DB_USER, DB_PASSWORD);
        manager.truncateTable("user_actions");
        DatabaseManager mockManager = mock(DatabaseManager.class);
        when(mockManager.getUserName()).thenReturn("postgres");

        when(mockManager.getUserName()).thenReturn("other");
        service.dropTable(mockManager, "mockTableName");
        List<UserAction> userActions = service.getAllActionsOfUser(DB_USER);

        List<String> actions = new LinkedList<>();
        for (int index = 0; index < userActions.size(); index++) {
            actions.add(index, userActions.get(index).getAction());
        }
        assertEquals("[CREATE DATABASE ('mockDatabase'), DROP DATABASE ('mockDatabase')]",
                actions.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllActionsFor_WithNullName() throws Exception {
        service.getAllActionsOfUser(null);
    }

    @Test
    public void testLogger() throws Exception {
        manager = service.connect(DB_NAME_LOG, DB_USER, DB_PASSWORD);
        manager.truncateTable("user_actions", "database_connections");
        DatabaseManager mockManager = mock(DatabaseManager.class);
        when(mockManager.getDatabaseName()).thenReturn("sqlcmd");
        when(mockManager.getUserName()).thenReturn("postgres");

        manager = service.connect(DB_NAME_DEFAULT, DB_USER, DB_PASSWORD);
        service.truncateTable(mockManager, "mockTable");
        service.tables(mockManager);
        service.rows(mockManager, "mockTable");
        service.deleteRow(mockManager, "mockTable", 0);
        service.dropTable(mockManager, "mockTable");
        service.insertRow(mockManager, "mockTable", new HashMap<>());
        service.createTable(mockManager, "mockTable");
        service.updateRow(mockManager, "mockTable", "mockKeyName", "mockKeyValue",
                new HashMap<>());

        manager = service.connect(DB_NAME_LOG, DB_USER, DB_PASSWORD);
        List<List<String>> actions = service.rows(manager, "user_actions");
        for (List<String> row : actions) {
            row.remove(0);
            row.remove(2);
        }
        actions.remove(0);
        List<List<String>> databaseConnection = service.rows(manager, "database_connections");
        databaseConnection.remove(0);
        String id1 = databaseConnection.get(0).get(0);
        String id2 = databaseConnection.get(1).get(0);
        assertEquals("[[" + id1 + ", sqlcmd, postgres], " +
                "[" + id2 + ", sqlcmd_log, postgres]]", databaseConnection.toString());
        assertEquals("[[CONNECT, " + id1 + "], " +
                        "[TRUNCATE TABLE ('mockTable'), " + id1 + "], " +
                        "[TABLES, " + id1 + "], " +
                        "[ROWS (TABLE 'mockTable'), " + id1 + "], " +
                        "[CREATE DATABASE ('mockDatabase'), " + id1 + "], " +
                        "[DROP DATABASE ('mockDatabase'), " + id1 + "], " +
                        "[DELETE ROW (TABLE 'mockTable', id= '0'), " + id1 + "], " +
                        "[DROP TABLE ('mockTable'), " + id1 + "], " +
                        "[INSERT ROW (TABLE 'mockTable'), " + id1 + "], " +
                        "[CREATE TABLE (mockTable), " + id1 + "], " +
                        "[UPDATE ROW (TABLE 'mockTable', 'mockKeyName'='mockKeyValue'), " + id1 + "], " +
                        "[CONNECT, " + id2 + "]]",
                actions.toString());

    }
}
