package ru.ivan.sqlcmd.controller.command;

class DropDatabase extends AbstractCommand  {

    DropDatabase() {
    }

    @Override
    public String getDescription() {
        return "delete database";
    }

    @Override
    public String getCommandFormat() {
        return "dropDatabase|databaseName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("dropDatabase|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_DATABASE_NAME = 1;

        String[] data = command.split("\\|");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Expected command getCommandFormat '" + getCommandFormat() + "', but actual '" + command + "'");
        }
        confirmAndDropDatabase(data[INDEX_DATABASE_NAME]);
    }

    private void confirmAndDropDatabase(final String databaseName) {
        try {
            view.write(String.format("Do you wish to delete database '%s'. Y/N?", databaseName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropDatabase(databaseName);
                view.write(String.format("Database '%s' deleted successful", databaseName));
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting database '%s'. Cause: '%s'", databaseName, e.getMessage()));
        }
    }
}
