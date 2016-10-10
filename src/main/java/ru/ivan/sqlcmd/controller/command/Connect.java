package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Connect extends Command {
    private final static Integer INDEX_DATABASE_NAME = 1;
    private final static Integer INDEX_USER_NAME = 2;
    private final static Integer INDEX_PASSWORD = 3;

    public Connect() {
    }

    public Connect(final DatabaseManager manager, final View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public String description() {
        return "connect to database";
    }

    @Override
    public String format() {
        return "connect|sqlcmd|postgres|postgres";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(final String command) {

        String[] data = command.split("[|]");

        if (data.length != parametersLength(format())) {
            throw new IllegalArgumentException(String.format("Number of parameters, splitting by '|' - %s. Expected - %s",
                    data.length, parametersLength(format())));
        }
        String database = data[INDEX_DATABASE_NAME];
        String user = data[INDEX_USER_NAME];
        String password = data[INDEX_PASSWORD];
        manager.connect(database, user, password);
        view.write(String.format("Connecting to database '%s' is successful", database));
    }
}
