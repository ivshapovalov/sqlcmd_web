package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

public class SizeTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Size(manager, view);
    }

    @Test
    public void testPrintTableSize() {
        //given
        Mockito.when(manager.getTableSize("users"))
                .thenReturn(2);

        //when
        command.process("size|users");

        //then
        String expected =
                "[Table 'users' size is 2]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessSizeWithParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("size|users");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessSizeWithoutParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("size");

        //then
        assertFalse(canProcess);
    }


    @Test
    public void testPrintEmptyTableSize() {
        //given
        Mockito.when(manager.getTableSize("users"))
                .thenReturn(0);

        //when
        command.process("size|users");

        //then
        String expected =
                "[Table 'users' size is 0]";
        shouldPrint(expected);
    }

}
