package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by Ivan on 22.09.2016.
 */
public class ClearTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager=Mockito.mock(DatabaseManager.class);
        view=Mockito.mock(View.class);
        command =new Clear(manager,view);
    }

    @Test
    public void testClearTable() {
        //given

        //when
        command.process("clear|users");
        //then
       verify(manager).clear("users");
       verify(view).write("Таблица users была успешно очищена.");
    }


    @Test
    public void testClearTableWithParametersLessThen2() {
        //when
        try {
            command.process("clear");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'clear|tableName', а ты ввел: clear",e.getMessage());

        }
    }
    @Test
    public void testClearTableWithParametersMoreThen2() {
        //when
        try {
            command.process("clear|users|qwe");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'clear|tableName', а ты ввел: clear|users|qwe",e.getMessage());

        }
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        //given

        //whrn
        Boolean canProcess=command.canProcess("clear|users");

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        //given
        //whrn
        Boolean canProcess=command.canProcess("clear");

        assertFalse(canProcess);
    }


}
