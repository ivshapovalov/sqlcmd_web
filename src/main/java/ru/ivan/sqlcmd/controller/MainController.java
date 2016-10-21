package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.controller.command.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class MainController {

    public static final char LINE_SEPARATOR = '\n';
    private View view;
    private List<Command> commands;
    private TreeMap<Integer, String> history = new TreeMap<>();

    public MainController(View view, DatabaseManager manager) {
        commands=new Help().getCommands();
        this.view = view;
        for ( Command command:commands
             ) {
            AbstractCommand abstractCommand=(AbstractCommand)command;
            abstractCommand.setManager(manager);
            abstractCommand.setView(view);
            if (abstractCommand instanceof History) {
                ((History)abstractCommand).setHistory(history);
            }
        }
    }

    public void run() {

        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }
    @SuppressWarnings("while")
    private void doWork() {
        view.write("Hello, user");
        int historySize = 0;
        while (true) {
            view.write("Input command or 'help' for assistance");
            String input = view.read();

            String historyInput = checkHistoryInput(input);
            if (historyInput == null) {
                continue;
            } else {
                input = historyInput;
            }
            increaseInputHistory(++historySize, input);
            for (Command command : commands
                    ) {

                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (ExitException e) {
                    throw e;
                } catch (Exception e) {
                    printError(e);
                    break;
                }
            }
        }
    }

    private void increaseInputHistory(int historySize, String input) {
        history.put(historySize, input);
//        if (history.size() > History.HISTORY_CAPACITY) {
//            history.pollFirstEntry();
//        }
    }

    private String checkHistoryInput(String input) {
        try {
            int historyIndex = Integer.parseInt(input);
            String newInput = history.get(historyIndex);
            if (newInput != null) {
                view.write(newInput);
            } else {
                view.write(String.format("Index '%s' does not exist in command history", historyIndex));
            }
        } catch (NumberFormatException e) {
            //ввели не число
            return input;
        }
        return null;

    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message = message + " " + e.getCause().getMessage();
        }
        view.write("Failure cause: " + message);
        view.write("Try again");
    }
}
