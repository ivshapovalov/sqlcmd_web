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
        return "выход из приложения";
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
        view.write("До скорой встречи!");
        throw new ExitException();
    }
}
