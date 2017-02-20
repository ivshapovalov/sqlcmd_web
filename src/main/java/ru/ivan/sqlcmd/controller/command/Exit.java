package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

public class Exit extends AbstractCommand {

    public Exit() {
    }

    public Exit(final View view) {
        this.view = view;
    }

    @Override
    public String getDescription() {
        return "exit from application";
    }

    @Override
    public String getCommandFormat() {
        return "exit";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.equals("exit");
    }

    @Override
    public void process(final String command) {
        view.write("Good bye!");
        throw new ExitException();
    }
}
