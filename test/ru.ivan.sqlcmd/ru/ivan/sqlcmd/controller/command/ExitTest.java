package ru.ivan.sqlcmd.controller.command;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ivan on 22.09.2016.
 */
public class ExitTest {

    private FakeView view=new FakeView();

    @Test
    public void testCanProcessExitString() {
        //given
        Command command =new Exit(view);

        //whrn
        Boolean canProcess=command.canProcess("exit");

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given
        Command command =new Exit(view);

        //whrn
        Boolean canProcess=command.canProcess("qwe");

        assertFalse(canProcess);
    }


    @Test
    public void testCanProcessExitCommand_ThrowsExitException() {
        //given
        Command command =new Exit(view);

        //whrn
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {

        }
        assertEquals("До скорой встречи!\n",view.getContent());

        //then throws exit exc
    }
}
