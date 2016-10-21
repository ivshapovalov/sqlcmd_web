package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

public class TruncateTableTest {

    private View view;
    private Command command;
    private static DatabaseManager manager;


    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new TruncateTable(manager, view);
    }

    @Test
    public void testTruncateTableWithParametersLessThen2() {
        //when
        try {
            command.process("truncateTable");
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Expected command getCommandFormat 'truncateTable|tableName', but actual 'truncateTable'", e.getMessage());
        }
    }

    @Test
    public void testTruncateTableWithParameters2() {
        //given
        Mockito.when(view.read()).thenReturn("y");

        //when
        command.process("truncateTable|users");

        //then
        String expected =
                "[Do you wish to clear table 'users'. Y/N?, Table 'users' cleared successful]";
        shouldPrint(expected);
    }

    @Test
    public void testTruncateTableWithParametersMoreThen2() {
        //when
        try {
            command.process("truncateTable|users|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Expected command getCommandFormat 'truncateTable|tableName', but actual 'truncateTable|users|qwe'", e.getMessage());
        }
    }

    @Test
    public void testCanProcessTruncateWithParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("truncateTable|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessTruncateWithoutParametersString() {
        //given
        //when
        Boolean canProcess = command.canProcess("truncateTable");

        //then
        assertFalse(canProcess);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }


}
