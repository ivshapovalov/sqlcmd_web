package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.controller.command.Command;
import ru.ivan.sqlcmd.controller.command.Tables;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

public class TablesTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Tables(manager, view);
    }

    @Test
    public void testTables() {
        //given
        Mockito.when(manager.getTableNames())
                .thenReturn(new LinkedHashSet<>(Arrays.asList("users", "users1", "users2")));
        //when
        command.process("tables");

        //then
        String expected =
                "[***Existing tables***, [users, users1, users2]]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessTablesWithParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("tables|users");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessTablesWithoutParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("tables");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testPrintNoTables() {
        //given
        Set<String> tables = new LinkedHashSet<>();
        Mockito.when(manager.getTableNames())
                .thenReturn(tables);
        //when
        command.process("tables");

        //then
        String expected =
                "[***Existing tables***, []]";
        shouldPrint(expected);
    }

}
