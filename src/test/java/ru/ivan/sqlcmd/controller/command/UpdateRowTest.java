package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;

public class UpdateRowTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new UpdateRow(manager, view);
    }

    @Test
    public void testUpdateRow() {
        //given

        //when
        command.process("updateRow|users|12|name|Ivan");

        //then
        String expected =
                "[В таблице 'users' успешно обновлена запись {name=Ivan}]";
        shouldPrint(expected);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessUpdateRowWithParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("updateRow|users|1|name|Ivan");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessUpdateRowWithoutParametersString() {
        //given

        //when
        Boolean canProcess = command.canProcess("updateRow");

        //then
        assertFalse(canProcess);
    }

}
