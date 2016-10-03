package ru.ivan.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ivan on 19.09.2016.
 */
public abstract class DatabaseManagerTest {
    public static final String TABLE_NAME = "users";
    private DatabaseManager manager;


    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() {
        manager = getDatabaseManager();
        String database = "sqlcmd";
        String user = "postgres";
        String password = "postgres";
        manager.connect(database, user, password);
        manager.dropTable("users");

    }

    @Test
    public void testGetAllTablesNames() {

        //given
        manager.getTableData(TABLE_NAME);

        //when
        Set<String> tables = manager.getTableNames();

        //then
        assertEquals("["+TABLE_NAME+"]", tables.toString());
    }

    @Test
    public void testGetColumnNames() {
//        //given
//        manager.dropTable("users");
//
//        //when
//        Tables<String> columnNames = manager.getTableColumns("users");
//
//        //then
//        assertEquals("[id, name, password]", columnNames.toString());
    }

    @Test
    public void testGetTableData() {

        //given
        manager.dropTable(TABLE_NAME);

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", new BigDecimal(13));
        input.put("name", "Stiven");
        input.put("password", "pass");

        manager.insertRow(TABLE_NAME, input);

        //then
        List<Map<String,Object>> users = manager.getTableData(TABLE_NAME);
        assertEquals(1, users.size());

        Map<String, Object> user = manager.getTableData(TABLE_NAME).get(0);
        for (int i = 0; i < input.entrySet().size() ; i++) {
            Iterator iter1=input.entrySet().iterator();
            Iterator iter2=user.entrySet().iterator();
            Map.Entry<String,Object> entry1= (Map.Entry<String, Object>) iter1.next();
            Map.Entry<String,Object> entry2= (Map.Entry<String, Object>) iter2.next();
            assertEquals(entry1.getKey(), entry2.getKey());
            assertEquals(entry1.getValue(), entry2.getValue());
        }
    }



    @Test
    public void testUpdateTableData() {
        //given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 1);
        input.put("name", "Stiven");
        input.put("password", "pass");

        manager.insertRow(TABLE_NAME, input);

        //when
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("id", new BigDecimal(1));
        output.put("name", "Ivan");
        output.put("password", "000");

        manager.updateRow(TABLE_NAME, 1, output);

        //then
        Map<String, Object> user = manager.getTableData(TABLE_NAME).get(0);
        for (int i = 0; i < output.entrySet().size() ; i++) {
            Iterator iter1=output.entrySet().iterator();
            Iterator iter2=user.entrySet().iterator();
            Map.Entry<String,Object> entry1= (Map.Entry<String, Object>) iter1.next();
            Map.Entry<String,Object> entry2= (Map.Entry<String, Object>) iter2.next();
            assertEquals(entry1.getKey(), entry2.getKey());
            assertEquals(entry1.getValue(), entry2.getValue());
        }
    }



    @Test
    public void isConnected() {
        assertTrue(manager.isConnected());
    }

}
