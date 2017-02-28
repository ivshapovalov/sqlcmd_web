package ru.ivan.sqlcmd.controller.command;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.controller.MainController;
import ru.ivan.sqlcmd.controller.command.Command;
import ru.ivan.sqlcmd.controller.command.Rows;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

public class RowsTest {

    private DatabaseManager manager;
    private View view;
    private Command command;
    private static final String LINE_SEPARATOR = "\n";


    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Rows(manager, view);

        //Disable log4j
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);

    }

    @Test
    public void testPrintTableData() {
        //given
        Mockito.when(manager.getTableColumns("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));

        Map<String, Object> user1 = new LinkedHashMap<>();
        user1.put("id", 12);
        user1.put("name", "Eva");
        user1.put("password", "*****");

        Map<String, Object> user2 = new LinkedHashMap<>();
        user2.put("id", 16);
        user2.put("name", "Steve");
        user2.put("password", "+++++");

        List<Map<String, Object>> data = new LinkedList<>(Arrays.asList(user1, user2));

        Mockito.when(manager.getTableRows("users"))
                .thenReturn(data);
        //when
        command.process("rows|users");

        //then
        String expected =
                "[+--+-----+--------+" + LINE_SEPARATOR + "" +
                        "|id|name |password|" + LINE_SEPARATOR + "" +
                        "+--+-----+--------+" + LINE_SEPARATOR + "" +
                        "|12|Eva  |*****   |" + LINE_SEPARATOR + "" +
                        "+--+-----+--------+" + LINE_SEPARATOR + "" +
                        "|16|Steve|+++++   |" + LINE_SEPARATOR + "" +
                        "+--+-----+--------+]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessRowsWithParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("rows|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessRowsWithoutParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("rows");

        //then
        assertFalse(canProcess);
    }


    @Test
    public void testCanProcessRowsWithIllegalParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("qwe|users");

        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        //given
        Mockito.when(manager.getTableColumns("users"))
                .thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));

        List<Map<String, Object>> data = new LinkedList<>();
        Mockito.when(manager.getTableRows("users"))
                .thenReturn(data);
        //when
        command.process("rows|users");

        //then
        String expected =
                "[+--+----+--------+" + LINE_SEPARATOR + "" +
                        "|id|name|password|" + LINE_SEPARATOR + "" +
                        "+--+----+--------+]";
        shouldPrint(expected);
    }

}
