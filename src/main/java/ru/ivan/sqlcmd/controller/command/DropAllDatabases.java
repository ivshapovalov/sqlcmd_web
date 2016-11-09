package ru.ivan.sqlcmd.controller.command;

class DropAllDatabases extends AbstractCommand  {

    DropAllDatabases() {
    }

    @Override
    public String getDescription() {
        return "delete all getDatabases";
    }

    @Override
    public String getCommandFormat() {
        return "dropAllDatabases";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        confirmAndDropAllDatabases();
    }

    private void confirmAndDropAllDatabases() {
        view.write("Do you wish to delete all getDatabases? Y/N");
        if (view.read().equalsIgnoreCase("y")) {
            manager.dropAllDatabases();
            view.write("All getDatabases  deleted successfully");
        }
    }
}
