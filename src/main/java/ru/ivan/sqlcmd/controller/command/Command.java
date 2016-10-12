package ru.ivan.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(final String command);

    void process(final String command);

    String description();

    String format();
}
