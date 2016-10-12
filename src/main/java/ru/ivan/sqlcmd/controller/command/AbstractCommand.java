package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class AbstractCommand {
    protected DatabaseManager manager;
    protected View view;

    protected final int parametersLength(final String COMMAND_SAMPLE) {
        return COMMAND_SAMPLE.split("[|]").length;
    }
}
