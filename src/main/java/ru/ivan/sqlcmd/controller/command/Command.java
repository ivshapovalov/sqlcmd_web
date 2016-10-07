package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public abstract class Command {
    protected DatabaseManager manager;
    protected View view;

    public Command() {
    }

    public Command(View view) {
        this.view = view;
    }

    public abstract boolean canProcess(final String command);

    public abstract void process(final String command);

    public abstract String description();

    public abstract String format();

    protected int parametersLength(final String COMMAND_SAMPLE) {
        return COMMAND_SAMPLE.split("[|]").length;
    }
}
