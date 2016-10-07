package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.Map;

public class History extends Command {
    private Map<Integer, String> history;
    public static Integer HISTORY_CAPACITY = 10;
    private final static Integer INDEX_NEW_CAPACITY=1;

    History() {
    }

    public History(final View view,final  Map<Integer, String> history) {
        this.view = view;
        this.history = history;
    }

    @Override
    public String description() {
        return "list of last 'history capacity' inputed commands. "+format() + "|N - set 'history capacity'";
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
    public void process(final String command) {

        String[] data = command.split("[|]");
        if (data.length ==INDEX_NEW_CAPACITY) {
            writeHistory();
        } else {
            try {
                setHistoryCapacity(data);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Size of commands history must be numeric, but actual '%s'",data[INDEX_NEW_CAPACITY]));
            }
        }
    }

    private void setHistoryCapacity(final String[] data) {
        int id;
        id= Integer.parseInt(data[INDEX_NEW_CAPACITY]);
        History.HISTORY_CAPACITY=id;
        view.write(String.format("Size of commands history set to '%s' ", id));
    }

    private void writeHistory() {
        for (Map.Entry<Integer, String> entry : history.entrySet()
                ) {
            view.write(entry.getKey() + ". " + entry.getValue());
        }
    }
}
