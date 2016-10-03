package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public abstract class Command {

    protected DatabaseManager manager;
    protected View view;

    public abstract boolean canProcess(String command);

    public abstract void process(String command);

    public abstract String description();

    public abstract String format();

    public Command(DatabaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    public Command(View view) {
        this.view = view;
    }

    public Command() {
    }

}
