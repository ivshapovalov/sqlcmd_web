package ru.ivan.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
        Set<String> tables = manager.getTablesNames();
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

        DataSet input = new DataSetImpl();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");

        manager.create("users", input);

        //then
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());
        Iterator iterator = users.iterator();
        DataSet user=new DataSetImpl();
        if (iterator.hasNext()) {

            user = (DataSet)iterator.next();
        }
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, Stiven, pass]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {

        //given
        manager.clear("users");

        DataSet input = new DataSetImpl();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "pass");

        manager.create("users", input);

        //when
        DataSetImpl output = new DataSetImpl();
        output.put("name", "Ivan");
        output.put("password", "000");
        manager.update("users", 13, output);

        //then
        List<DataSet> users = manager.getTableData("users");
        assertEquals(1, users.size());
        Iterator iterator = users.iterator();
        DataSet user=new DataSetImpl();
        if (iterator.hasNext()) {

            user = (DataSet)iterator.next();
        }
        assertEquals("[id, name, password]", user.getNames().toString());
        assertEquals("[13, Ivan, 000]", user.getValues().toString());
    }


    @Test
    public void isConnected() {
        assertTrue(manager.isConnected());
    }

}
