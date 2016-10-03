package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Exit extends Command {

    private View view;

    @Override
    public String description() {
        return "exit from application";
    }

    @Override
    public String format() {
        return "exit";
    }

    public Exit() {
    }

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
