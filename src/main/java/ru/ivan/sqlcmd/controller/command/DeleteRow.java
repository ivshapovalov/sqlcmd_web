package ru.ivan.sqlcmd.controller.command;

class DeleteRow extends AbstractCommand {

    DeleteRow() {
    }

    @Override
    public String getDescription() {
        return "delete from table row with specific ID ";
    }

    @Override
    public String getCommandFormat() {
        return "deleteRow|tableName|ID";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("deleteRow|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_TABLE_NAME = 1;
        final int INDEX_DELETING_ROW_ID = 2;

        String[] data = command.split("[|]");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Expected command format '" + getCommandFormat() + "', but actual '" +
                    command + "'");
        }
        String tableName = data[INDEX_TABLE_NAME];
        int id;
        try {
            id = Integer.parseInt(data[INDEX_DELETING_ROW_ID]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.valueOf(INDEX_DELETING_ROW_ID + 1) + " parameter must be numeric!");
        }
        manager.deleteRow(tableName, id);
        view.write(String.format("Delete row '%s' from table '%s' successfully", id, tableName));
    }
}
