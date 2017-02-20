package ru.ivan.sqlcmd.view;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class WebConsole implements View {

    StringBuilder output=new StringBuilder();
    StringBuilder input=new StringBuilder();
    public static final String LINE_SEPARATOR = System.lineSeparator();


    public WebConsole(String outputText) {
        output=new StringBuilder(outputText);
    }

    @Override
    public void write(String message) {
        output.append(message).append(LINE_SEPARATOR);
    }

    @Override
    public String read() {
        return input.toString();
    }

    public StringBuilder getOutput() {
        return output;
    }

    public StringBuilder getInput() {
        return input;
    }

    public void setOutput(StringBuilder output) {
        this.output = output;
    }

    public void setInput(StringBuilder input) {
        this.input = input;
    }
}
