package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 22.09.2016.
 */
public class DropAllDatabases implements Command {

    private DatabaseManager manager;
    private View view;

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
