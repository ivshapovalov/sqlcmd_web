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

        confirmAndDropAllDatabases();
    }

    private void confirmAndDropAllDatabases() {

        try {
            view.write("Удаляем все базы данных? Y/N");
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropAllDatabases();
                view.write("Все базы данных были успешно удалены.");
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления всех баз данных по причине: %s", e.getMessage()));
        }
    }
}
