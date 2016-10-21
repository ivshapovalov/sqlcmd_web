package ru.ivan.sqlcmd.controller.command;

class DropTable extends AbstractCommand  {

     DropTable() {
    }

    @Override
    public String getDescription() {
        return "delete table";
    }

    @Override
    public String getCommandFormat() {
        return "dropTable|tableName";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("dropTable|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;

        String[] data = command.split("\\|");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Expected command getCommandFormat '" + getCommandFormat() + "', but actual '" + command + "'");
        }
        confirmAndDropTable(data[INDEX_TABLE_NAME]);
    }

    private void confirmAndDropTable(final String tableName) {
        try {
            view.write(String.format("Do you wish to delete table '%s'. Y/N?", tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.dropTable(tableName);
                view.write(String.format("Table '%s' deleted successful", tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Error while deleting table '%s'. Cause: '%s'", tableName, e.getMessage()));
        }
    }
}
