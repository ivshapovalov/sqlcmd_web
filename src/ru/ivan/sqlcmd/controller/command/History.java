package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.Map;

/**
 * Created by Ivan on 21.09.2016.
 */
public class History implements Command {

    private View view;
    Map<String, String> history;

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
