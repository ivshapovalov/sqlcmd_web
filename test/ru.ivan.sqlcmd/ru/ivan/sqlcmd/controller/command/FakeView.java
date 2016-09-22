package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 22.09.2016.
 */
public class FakeView implements View {

    private String messages="";

    @Override
    public String read() {
        return null;
    }

    @Override
    public void write(String message) {
        messages+=message+"\n";

    }

    public String getContent(){
        return messages;
    }
}

