package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Disconnect implements Command {

    private DatabaseManager manager;
    private View view;

    public Disconnect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("disconnect");
    }

    @Override
    public void process(String command) {
        manager.disconnect();
        view.write("Отключение успешно");
    }

}
