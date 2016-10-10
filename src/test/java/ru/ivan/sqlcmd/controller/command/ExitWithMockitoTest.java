package ru.ivan.sqlcmd.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ru.ivan.sqlcmd.view.View;

import static org.junit.Assert.*;

public class ExitWithMockitoTest {
    private View view = Mockito.mock(View.class);

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

        //then
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
        Mockito.verify(view).write("Good bye!");

    }
}
