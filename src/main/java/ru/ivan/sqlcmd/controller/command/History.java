package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.Map;

public class History extends Command {
    Map<Integer, String> history;
    public static Integer HISTORY_CAPACITY = 10;

    public History() {
    }

    public History(View view, Map<Integer, String> history) {
        this.view = view;
        this.history = history;
    }

    @Override
    public String description() {
        return "список последних " + HISTORY_CAPACITY + " введенных команд";
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
        if (data.length == 1) {
            for (Map.Entry<Integer, String> entry : history.entrySet()
                    ) {
                view.write(entry.getKey() + ". " + entry.getValue());
            }
        } else {
            int id;
            try {
                id= Integer.parseInt(data[1]);
                HISTORY_CAPACITY=id;
                view.write(String.format("Установлен размер хранимой истории введенных команд. Новый размер - %s ", id));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Неверно указан размер хранимой истории!");
            }
        }


    }
}
