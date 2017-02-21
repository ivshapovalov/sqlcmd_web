package ru.ivan.sqlcmd.controller.command;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class History extends AbstractCommand {
    private TreeMap<Integer, String> history;
    public static Integer historyCapacity = 5;
    private final static Integer INDEX_NEW_CAPACITY = 1;

    History() {

    }

    @Override
    public boolean showInHelp() {
        return false;
    }

    @Override
    public String getDescription() {
        return "list last 'history capacity' commands. All inputted commands stores in memory." + getCommandFormat() + "|N - set 'history capacity'";
    }

    @Override
    public String getCommandFormat() {
        return "history";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith(getCommandFormat());
    }

    public void setHistory(TreeMap<Integer, String> history) {
        this.history = history;
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
        History.historyCapacity = id;
        view.write(String.format("Size of commands history set to '%s' ", id));
    }

    private void writeHistory() {

        SortedMap<Integer, String> tailMap = (SortedMap<Integer, String>) history.clone();
        if (history.size() >= historyCapacity) {
            tailMap = history.tailMap(history.size() - historyCapacity +1);
        }

        for (Map.Entry<Integer, String> entry : tailMap.entrySet()
                ) {
            view.write(entry.getKey() + ". " + entry.getValue());
        }
    }
}
