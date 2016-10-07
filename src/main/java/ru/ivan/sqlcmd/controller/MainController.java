package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.controller.command.*;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.*;

public class MainController {

    public static final char LINE_SEPARATOR='\n';
    private final View view;
    private final DatabaseManager manager;
    private final java.util.List<Command> commands;
    private TreeMap<Integer, String> history = new TreeMap<>();

    public MainController(View view, DatabaseManager manager) {
        this.manager = manager;
        this.view = view;
        this.commands = Arrays.asList(
                new Help(view),
                new History(view, history),
                new Exit(view),
                new Disconnect(manager, view),
                new Connect(manager, view),
                new IsConnected(manager, view),
                new Size(manager, view),
                new CreateDatabase(manager, view),
                new DeleteRow(manager, view),
                new CreateTable(manager, view),
                new Databases(manager, view),
                new DropDatabase(manager, view),
                new DropAllDatabases(manager, view),
                new DropTable(manager, view),
                new DropAllTables(manager, view),
                new TruncateAllTables(manager, view),
                new TruncateTable(manager, view),
                new InsertRow(manager, view),
                new UpdateRow(manager, view),
                new Rows(manager, view),
                new Tables(manager, view),
                new Unsupported(view));
    }

    public void run() {

        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void doWork() {
        view.write("Hello, user");
        int historySize=0;
        while (true) {
            view.write("Input command or 'help' for assistance");
            String input = view.read();

            String historyInput=checkHistoryInput(input);
            if (historyInput==null) {
                continue;
            } else {
                input=historyInput;
            }
            increaseInputHistory(++historySize,input);
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

    private void increaseInputHistory(int historySize,String input) {
        history.put(historySize, input);
        if (history.size()>History.HISTORY_CAPACITY) {
            history.pollFirstEntry();
        }
    }

    private String checkHistoryInput(String input) {
        try {
            int historyIndex = Integer.parseInt(input);
            String newInput = history.get(historyIndex);
            if (newInput != null) {
                view.write(newInput);
            } else {
                view.write(String.format("Index '%s' does not exist in command history",historyIndex));
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
