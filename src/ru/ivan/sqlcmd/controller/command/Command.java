package ru.ivan.sqlcmd.controller.command;

/**
 * Created by Ivan on 21.09.2016.
 */
public interface Command {

    boolean canProcess (String command);

    void process (String command);
}
