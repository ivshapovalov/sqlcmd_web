package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.Map;

/**
 * Created by Ivan on 21.09.2016.
 */
public class DoFromHistory implements Command {

    private DatabaseManager manager;
    private View view;
    Map<String, String> history;


    public DoFromHistory(DatabaseManager manager,View view, Map<String, String> history) {
        this.view=view;
        this.history=history;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        try {
        Integer.valueOf(command);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public void process(String command) {



    }
}
