package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class DeleteRow extends Command {

    public DeleteRow() {
    }

    public DeleteRow(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "delete the entry in the table using the ID";
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
        if (data.length != 3) {
            throw new IllegalArgumentException("Формат команды 'deleteRow|tableName|ID', а ты ввел: " + command);
        }
        String tableName = data[1];
        int id = Integer.parseInt(data[2]);

        try {
            manager.deleteRow(tableName, id);
        } catch (IllegalArgumentException e) {
            String originalMessage = e.getMessage();
            String newMessage = "";
            if (originalMessage.contains("отношение")) {
                newMessage = originalMessage.replace("отношение", "таблица");
            } else if (originalMessage.contains("столбец")) {
                newMessage = originalMessage.replace("столбец", "поле");
            }
            throw new IllegalArgumentException(newMessage);
        }

        view.write(String.format("В таблице '%s' успешно удалена запись c ID=%s", tableName, id));
    }
}
