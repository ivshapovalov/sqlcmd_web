package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.DatabaseManagerException;
import ru.ivan.sqlcmd.view.View;

public class DeleteRow extends Command {

    public DeleteRow() {
    }

    public DeleteRow(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "удаление строки из таблицы с заданным ID";
    }

    @Override
    public String format() {
        return "deleteRow|tableName|ID";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("deleteRow|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException("Формат команды '"+format()+"', а ты ввел: " + command);
        }
        String tableName = data[1];
        int id;
        try {
            id= Integer.parseInt(data[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Третий параметр ID не может быть преобразован к числовому!");
        }
        try {
            manager.deleteRow(tableName, id);
        } catch (DatabaseManagerException e) {
            throw e;
        }
        view.write(String.format("В таблице '%s' успешно удалена запись c ID=%s", tableName, id));
    }
}
