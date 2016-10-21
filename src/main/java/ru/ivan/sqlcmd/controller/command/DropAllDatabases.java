package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropAllDatabases extends AbstractCommand  {

    public DropAllDatabases() {
    }

    @Override
    public String getDescription() {
        return "delete all databases";
    }

    @Override
    public String getCommandFormat() {
        return "dropAllDatabases";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        confirmAndDropAllDatabases();
    }

    private void confirmAndDropAllDatabases() {
        view.write("Do you wish to delete all databases? Y/N");
        if (view.read().equalsIgnoreCase("y")) {
            manager.dropAllDatabases();
            view.write("All databases  deleted successfully");
        }
    }
}
