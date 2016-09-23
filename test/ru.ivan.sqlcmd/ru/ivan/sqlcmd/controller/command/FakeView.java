package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

public class FakeView implements View {

    private String messages="";
    private String input=null;

    @Override
    public String read() {
        if (this.input==null) {
            throw new IllegalStateException("Для работы проинициализируйте метод read");
        }
        String result=this.input;
        this.input=null;
        return result;
    }

    @Override
    public void write(String message) {
        messages+=message+"\n";

    }
    public void addRead(String input){
        this.input=input;
    }

    public String getContent(){
        return messages;
    }
}

