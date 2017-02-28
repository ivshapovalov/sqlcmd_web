package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.PropertiesLoader;

class Connect extends AbstractCommand {

    Connect() {
    }

    @Override
    public String getDescription() {
        return "connect to database (all fields may be blank)";
        //return "connect to database (databaseName may be blank)";
    }

    @Override
    public String getCommandFormat() {
        return "connect";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("connect");
    }
//    public boolean canProcess(final String command) {
//        return command.startsWith("connect|");
//    }

    @Override
    public void process(final String command) {

        PropertiesLoader propertiesLoader = new PropertiesLoader();
        String database = propertiesLoader.getDatabaseName();
        String user = propertiesLoader.getUserName();
        String password = propertiesLoader.getPassword();
        manager.connect(database, user, password);
        view.write(String.format("Connecting to database '%s' is successful", database));
    }
//    @Override
//    public void process(final String command) {
//
//        final int INDEX_DATABASE_NAME = 1;
//        final int INDEX_USER_NAME = 2;
//        final int INDEX_PASSWORD = 3;
//
//        String[] data = command.split("[|]");
//
//        if (data.length != parametersLength(getCommandFormat())) {
//            throw new IllegalArgumentException(String.format("Number of parameters, splitting by '|' - %s. Expected - %s",
//                    data.length, parametersLength(getCommandFormat())));
//        }
//        String database = data[INDEX_DATABASE_NAME];
//        String user = data[INDEX_USER_NAME];
//        String password = data[INDEX_PASSWORD];
//        manager.connect(database, user, password);
//        view.write(String.format("Connecting to database '%s' is successful", database));
//    }
}
