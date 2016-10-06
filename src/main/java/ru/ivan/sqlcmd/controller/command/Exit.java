package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

public class Exit extends Command {

    public Exit() {
    }

    public Exit(View view) {
        this.view=view;
    }

    @Override
    public String description() {
        return "exit from application";
    }

    @Override
    public String format() {
        return "exit";
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("Good bye!");
        throw new ExitException();
    }
}
