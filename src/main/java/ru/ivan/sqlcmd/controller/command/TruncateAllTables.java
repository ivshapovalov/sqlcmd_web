package ru.ivan.sqlcmd.controller.command;

class TruncateAllTables extends AbstractCommand {

    TruncateAllTables() {
    }

    @Override
    public String getDescription() {
        return "clear all getTables";
    }

    @Override
    public String getCommandFormat() {
        return "truncateAll";
    }


    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(getCommandFormat());
    }

    @Override
    public void process(final String command) {

        confirmAndTruncateAllTables();
    }

    private void confirmAndTruncateAllTables() {
        try {
            view.write("Do you wish to clear all getTables?. Y/N");
            if (view.read().equalsIgnoreCase("y")) {
                manager.truncateAllTables();
                view.write("All getTables cleared successfully");
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting all getTables. Cause: '%s'", e.getMessage()));
        }
    }
}
