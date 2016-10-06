package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;
import java.util.Map;

public class History extends Command {
    Map<String, String> history;
    public static Integer HISTORY_CAPACITY=3;

    public History() {
    }

    public History(View view, Map<String, String> history) {
        this.view=view;
        this.history=history;
    }

    @Override
    public String description() {
        return "список последних "+HISTORY_CAPACITY+" введенных команд";
    }

    @Override
    public String format() {
        return "history";
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals(format());
    }

    @Override
    public void process(String command) {
        for (Map.Entry<String,String> entry:history.entrySet()
             ) {
            view.write(entry.getKey()+". "+entry.getValue());
        }
    }
}
