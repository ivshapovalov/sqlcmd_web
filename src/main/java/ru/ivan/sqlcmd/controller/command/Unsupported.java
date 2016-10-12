package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

public class Unsupported extends AbstractCommand implements Command {
    @Override
    public String description() {
        return "unsupported operation";
    }

    @Override
    public String format() {
        return "";
    }

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("This command does not exist - " + command);
    }
}
