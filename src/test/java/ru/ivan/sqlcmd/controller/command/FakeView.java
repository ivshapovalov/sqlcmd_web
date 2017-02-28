package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

public class FakeView implements View {

    private String messages = "";
    private String input = null;
    private static final String LINE_SEPARATOR ="\n";


    @Override
    public String read() {
        if (this.input == null) {
            throw new IllegalStateException("Initialize method read");
        }
        String result = this.input;
        this.input = null;
        return result;
    }

    @Override
    public void write(String message) {
        messages += message + "" + LINE_SEPARATOR + "";

    }

    public String getContent() {
        return messages;
    }


}

