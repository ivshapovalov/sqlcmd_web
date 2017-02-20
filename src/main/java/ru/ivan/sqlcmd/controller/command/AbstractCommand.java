package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public abstract class AbstractCommand implements Command {
    DatabaseManager manager;
    View view;
    protected static final String LINE_SEPARATOR = System.lineSeparator();


    final int parametersLength(final String COMMAND_SAMPLE) {
        return COMMAND_SAMPLE.split("[|]").length;
    }

    public boolean showInHelp() {
        return true;
    }

    public void setManager(DatabaseManager manager) {
        this.manager = manager;
    }

    public void setView(View view) {
        this.view = view;
    }

    public AbstractCommand(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    AbstractCommand() {
    }
}
