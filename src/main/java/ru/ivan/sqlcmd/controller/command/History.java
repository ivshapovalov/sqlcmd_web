package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class History extends AbstractCommand {
    private TreeMap<Integer, String> history;
    private static Integer HISTORY_CAPACITY = 5;
    private final static Integer INDEX_NEW_CAPACITY = 1;

    History() {
    }

    public History(final View view, final TreeMap<Integer, String> history) {
        this.view = view;
        this.history = history;
    }

    @Override
    public String description() {
        return "list last 'history capacity' commands. All inputted commands stores in memory." + format() + "|N - set 'history capacity'";
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
        if (data.length == INDEX_NEW_CAPACITY) {
            writeHistory();
        } else {
            try {
                setHistoryCapacity(data);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Size of commands history must be numeric, but actual '%s'", data[INDEX_NEW_CAPACITY]));
            }
        }
    }

    private void setHistoryCapacity(final String[] data) {
        int id;
        id = Integer.parseInt(data[INDEX_NEW_CAPACITY]);
        History.HISTORY_CAPACITY = id;
        view.write(String.format("Size of commands history set to '%s' ", id));
    }

    private void writeHistory() {

        SortedMap<Integer, String> tailMap = (SortedMap<Integer, String>) history.clone();
        if (history.size() >= HISTORY_CAPACITY) {
            tailMap = history.tailMap(history.size() - HISTORY_CAPACITY+1);
        }

        for (Map.Entry<Integer, String> entry : tailMap.entrySet()
                ) {
            view.write(entry.getKey() + ". " + entry.getValue());
        }
    }
}
