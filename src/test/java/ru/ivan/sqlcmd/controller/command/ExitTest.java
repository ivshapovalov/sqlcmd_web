package ru.ivan.sqlcmd.controller.command;

import org.junit.Test;
import ru.ivan.sqlcmd.controller.MainController;

import static org.junit.Assert.*;

public class ExitTest {

    private FakeView view = new FakeView();

    @Test
    public void testCanProcessExitString() {
        //given
        Command command = new Exit(view);

        //when
        Boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given
        Command command = new Exit(view);

        //when
        Boolean canProcess = command.canProcess("qwe");

        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessExitCommand_ThrowsExitException() {
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {

        }
        //then
        assertEquals("Good bye!" + MainController.LINE_SEPARATOR + "", view.getContent());

    }
}
