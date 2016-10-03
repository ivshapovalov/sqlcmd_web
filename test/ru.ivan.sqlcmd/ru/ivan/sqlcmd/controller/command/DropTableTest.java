package ru.ivan.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Ivan on 22.09.2016.
 */
public class DropTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager=Mockito.mock(DatabaseManager.class);
        view=Mockito.mock(View.class);
        command =new DropTable(manager,view);
    }

    @Test
    public void testClearTable() {
        //given

        //when
        command.process("dropTable|users");
        //then
       verify(manager).dropTable("users");
       verify(view).write("Таблица users была успешно удалена.");
    }


    @Test
    public void testClearTableWithParametersLessThen2() {
        //when
        try {
            command.process("dropTable");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'dropTable|tableName', а ты ввел: dropTable",e.getMessage());

        }
    }
    @Test
    public void testClearTableWithParametersMoreThen2() {
        //when
        try {
            command.process("dropTable|users|qwe");
            fail ();
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'dropTable|tableName', а ты ввел: dropTable|users|qwe",e.getMessage());

        }
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        //given

        //whrn
        Boolean canProcess=command.canProcess("dropTable|users");

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        //given
        //whrn
        Boolean canProcess=command.canProcess("dropTable");

        assertFalse(canProcess);
    }


}
