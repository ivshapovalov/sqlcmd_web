package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.Map;

public class History extends Command {
    Map<Integer, String> history;
    public static Integer HISTORY_CAPACITY = 10;
    private final static Integer INDEX_NEW_CAPACITY=1;

    public History() {
    }

    public History(View view, Map<Integer, String> history) {
        this.view = view;
        this.history = history;
    }

    @Override
    public String description() {
        return "list of lase " + HISTORY_CAPACITY + " inputed commands";
    }

    @Override
    public String format() {
        return "history";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith(format());
    }

    @Override
    public void process(String command) {

        String[] data = command.split("[|]");
        if (data.length ==INDEX_NEW_CAPACITY) {
            for (Map.Entry<Integer, String> entry : history.entrySet()
                    ) {
                view.write(entry.getKey() + ". " + entry.getValue());
            }
        } else {
            int id;
            try {
                id= Integer.parseInt(data[INDEX_NEW_CAPACITY]);
                History.HISTORY_CAPACITY=id;
                view.write(String.format("Size of commands history set to '%s' ", id));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Size of commands history must be numeric, but actual '%s'",data[INDEX_NEW_CAPACITY]));
            }
        }


    }
}
