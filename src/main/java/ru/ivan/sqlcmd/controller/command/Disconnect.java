package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Disconnect extends AbstractCommand {

    public Disconnect() {
    }

    @Override
    public String getDescription() {
        return "disconnect from current database";
    }

    @Override
    public String getCommandFormat() {
        return "disconnect";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        manager.disconnect();
        view.write("Disconnect successful");
    }

}
