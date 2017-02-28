package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.controller.command.Command;
import ru.ivan.sqlcmd.controller.command.Databases;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

public class DatabasesTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Databases(manager, view);
    }

    @Test
    public void testDatabases() {
        //given
        Mockito.when(manager.getDatabasesNames())
                .thenReturn(new LinkedHashSet<>(Arrays.asList("sqlcmd", "sqlcmd1", "sqlcmd2")));
        //when
        command.process("databases");

        //then
        String expected =
                "[***Existing databases***, sqlcmd, sqlcmd1, sqlcmd2]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessDatabasesWithParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("databases|users");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessDatabasesWithoutParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("databases");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testPrintNoDatabases() {
        //given
        Set<String> databases = new LinkedHashSet<>();
        Mockito.when(manager.getDatabasesNames())
                .thenReturn(databases);
        //when
        command.process("databases");

        //then
        String expected =
                "[***Existing databases***]";
        shouldPrint(expected);
    }

}
