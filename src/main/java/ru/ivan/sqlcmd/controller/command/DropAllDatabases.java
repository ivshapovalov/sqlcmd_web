package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class DropAllDatabases extends Command {
    @Override
    public String description() {
        return "drop all databases";
    }

    @Override
    public String format() {
        return "dropAllDatabases";
    }

    public DropAllDatabases() {
    }

    public DropAllDatabases(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropAllDatabases");
    }

    @Override
    public void process(String command) {

        manager.dropAllDatabases();

        view.write("Все базы данных были успешно очищены");
    }
}
