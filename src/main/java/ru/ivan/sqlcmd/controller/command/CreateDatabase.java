package ru.ivan.sqlcmd.controller.command;

class CreateDatabase extends AbstractCommand {

    CreateDatabase() {
    }

    @Override
    public String getDescription() {
        return "create new database";
    }

    @Override
    public String getCommandFormat() {
        return "createDatabase|databaseName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("createDatabase|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_DATABASE_NAME = 1;
        String[] data = command.split("\\|");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Expected command format '" + getCommandFormat() + "', but actual '" +
                    command + "'");
        }
        manager.createDatabase(data[INDEX_DATABASE_NAME]);

        view.write(String.format("Database '%s' created successfully", data[1]));
    }
}
