package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DataSetImpl;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;


public class Create implements Command {

    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {
        this.manager=manager;

        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data=command.split("[|]");
        if (data.length%2==1) {
            throw new IllegalArgumentException("Должно быть четное количество параметров " +
                    "в формате create|table|column1|value1|column2|value2|...|columnN|valueN");

        }
        String table=data[1];

        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int i = 1; i < data.length/2; i++) {
            String column=data[i*2];
            String value=data[i*2+1];
            tableData.put(column,value);
        }
        try {
            manager.insert(table, tableData);
        } catch (IllegalArgumentException e) {
            String originalMessage = e.getMessage();
            String newMessage="";
            if (originalMessage.contains("отношение")) {
                newMessage=originalMessage.replace("отношение","таблица");
            } else if (originalMessage.contains("столбец")) {
                newMessage=originalMessage.replace("столбец","поле");
            }
            throw new IllegalArgumentException(newMessage);
        }

        view.write(String.format("В таблице '%s' успешно создана запись %s",table,tableData ));



    }
}
