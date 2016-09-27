package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DataSetImpl;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

/**
 * Created by Ivan on 22.09.2016.
 */
public class FindTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Find(manager, view);
    }

    @Test
    public void testPrintTableData() {
        //given

        Mockito.when(manager.getTableColumns("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));

        DataSet user1 = new DataSetImpl();
        user1.put("id", 12);
        user1.put("name", "Eva");
        user1.put("password", "*****");

        DataSet user2 = new DataSetImpl();
        user2.put("id", 16);
        user2.put("name", "Steve");
        user2.put("password", "+++++");

        List<DataSet> data = new ArrayList<>(Arrays.asList(user1, user2));

        Mockito.when(manager.getTableData("users"))
                .thenReturn(data);
        //when
        command.process("find|users");

        //then
        String expected =
                "[id\t|name\t|password\t|," +
                        " 12\t|Eva\t|*****\t|," +
                        " 16\t|Steve\t|+++++\t|" +

                        "]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        //given

        //whrn
        Boolean canProcess = command.canProcess("find|users");

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFindWithoutParametersString() {
        //given
        //whrn
        Boolean canProcess = command.canProcess("find");

        assertFalse(canProcess);
    }


    @Test
    public void testCanProcessFindWithIllegalParametersString() {
        //given

        //whrn
        Boolean canProcess = command.canProcess("qwe|users");

        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        //given
        Mockito.when(manager.getTableColumns("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));

        List<DataSet> data = new ArrayList<>();
        Mockito.when(manager.getTableData("users"))
                .thenReturn(data);
        //when
        command.process("find|users");

        //then
        String expected =
                "[id\t|name\t|password\t|]";
        shouldPrint(expected);
    }

}
