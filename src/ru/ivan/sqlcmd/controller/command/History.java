package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.Map;

/**
 * Created by Ivan on 21.09.2016.
 */
public class History extends Command {

    Map<String, String> history;

    @Override
    public String description() {
        return "list history of commands";
    }

    @Override
    public String format() {
        return "history";
    }

    public History() {
    }

    public History(View view, Map<String, String> history) {
        this.view=view;
        this.history=history;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("history");
    }

    @Override
    public void process(String command) {
        for (Map.Entry<String,String> entry:history.entrySet()
             ) {
            view.write(entry.getKey()+"\t"+entry.getValue());
        }
    }
}
