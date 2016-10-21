package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

public class Unsupported extends AbstractCommand  {
    public Unsupported() {

    }

    @Override
    public String getDescription() {
        return "unsupported operation";
    }

    public boolean showInHelp() {
        return false;
    }

    @Override
    public String getCommandFormat() {
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
