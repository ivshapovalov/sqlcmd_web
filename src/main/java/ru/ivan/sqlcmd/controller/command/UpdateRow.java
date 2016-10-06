package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.*;
import ru.ivan.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateRow extends Command {

    public UpdateRow() {
    }

    public UpdateRow(DatabaseManager manager, View view) {
        this.manager = manager;

        this.view = view;
    }

    @Override
    public String description() {
        return "обновление строки с заданным ID в таблице";
    }

    @Override
    public String format() {
        return "updateRow|tableName|ID";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("updateRow|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        if (data.length % 2 == 0 || data.length <=parametersLength(format())) {
            throw new IllegalArgumentException("Должно быть четное количество параметров большее или равное 4 " +
                    "в формате updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN");

        }
        String tableName = data[1];
        int id;
        try {
             id= Integer.parseInt(data[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Третий параметр ID не может быть преобразован к числовому!");
        }

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i <= data.length / 2 - 1; i++) {
            String column = data[i * 2 + 1];
            String value = data[i * 2 + 2];
            tableData.put(column, value);
        }
        try {
            manager.updateRow(tableName, id, tableData);
        } catch (DatabaseManagerException e) {
            throw e;
        }

        view.write(String.format("В таблице '%s' успешно обновлена запись %s", tableName, tableData));
    }
}
