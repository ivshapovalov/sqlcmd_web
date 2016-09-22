package ru.ivan.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ivan on 19.09.2016.
 */
public abstract class DatabaseManagerTest {
    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() {
        manager = getDatabaseManager();
        String database = "sqlcmd";
        String user = "postgres";
        String password = "postgres";
        manager.connect(database, user, password);
        manager.clear("users");
    }

    @Test
    public void testGetAllTablesNames() {
        List<String> tables = manager.getTablesNames();
        assertEquals("[users]", tables.toString());
    }

    @Test
    public void testGetColumnNames() {
//        //given
//        manager.clear("users");
//
//        //when
//        List<String> columnNames = manager.getTableColumns("users");
//
//        //then
//        assertEquals("[id, name, password]", columnNames.toString());
    }

    @Test
    public void testGetTableData() {

        //given
        manager.clear("users");

        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");

        manager.create("users", input);

        //then
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());
        DataSet user = users.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, Stiven, pass]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {

        //given
        manager.clear("users");

        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");

        manager.create("users", input);

        //when
        DataSet output = new DataSet();
        output.put("name", "Ivan");
        output.put("password", "000");
        manager.update("users", 13, output);

        //then
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());
        DataSet user = users.get(0);
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, Ivan, 000]", user.getValues().toString());
    }



    @Test
    public void isConnected(){
        assertTrue(manager.isConnected());
    }

}
