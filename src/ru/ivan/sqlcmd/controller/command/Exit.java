package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Exit implements Command {

    private View view;

    public Exit(View view) {
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("До скорой встречи!");
        throw new ExitException();
    }
}
