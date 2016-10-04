package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Disconnect extends Command {

    @Override
    public String description() {
        return "disconnect from current database";
    }

    @Override
    public String format() {
        return "disconnect";
    }

    public Disconnect() {
    }

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
