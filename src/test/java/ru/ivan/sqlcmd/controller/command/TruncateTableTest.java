package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.controller.command.Command;
import ru.ivan.sqlcmd.controller.command.TruncateTable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


public class TruncateTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager=Mockito.mock(DatabaseManager.class);
        view=Mockito.mock(View.class);
        command =new TruncateTable(manager,view);
    }

    @Test
    public void testTruncateTable() {
        //given

        //when
        command.process("trunctateTable|users");
        //then
       verify(manager).truncateTable("users");
       verify(view).write("Таблица users была успешно удалена.");
    }


    @Test
    public void testClearTableWithParametersLessThen2() {
        //when
        try {
            command.process("trunctateTable");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'trunctateTable|tableName', а ты ввел: trunctateTable",e.getMessage());

        }
    }
    @Test
    public void testClearTableWithParametersMoreThen2() {
        //when
        try {
            command.process("trunctateTable|users|qwe");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'trunctateTable|tableName', а ты ввел: trunctateTable|users|qwe",e.getMessage());

        }
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        //given

        //whrn
        Boolean canProcess=command.canProcess("trunctateTable|users");

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        //given
        //whrn
        Boolean canProcess=command.canProcess("trunctateTable");

        assertFalse(canProcess);
    }


}
