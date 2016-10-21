package ru.ivan.sqlcmd.controller.command;

class DropAllTables extends AbstractCommand {

    DropAllTables() {
    }


    @Override
    public String getDescription() {
        return "delete all tables of current database";
    }

    @Override
    public String getCommandFormat() {
        return "dropAllTables";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        confirmAndDropAllTables();
    }

    private void confirmAndDropAllTables() {
        try {
            view.write("Do you wish to delete all tables? Y/N");
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropAllTables();
                view.write("All tables deleted successfully");
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting all tables. Cause: '%s'", e.getMessage()));
        }
    }
}
