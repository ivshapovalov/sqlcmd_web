package ru.ivan.sqlcmd.controller.command;

class CreateTable extends AbstractCommand {

    CreateTable() {
    }

    @Override
    public String getDescription() {
        return "create new table (type i.e. TEXT,INTEGER)";
    }

    @Override
    public String getCommandFormat() {
        return "createTable|tableName(columnName1 type, columnName2 type,...columnNameN type)";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith("createTable|");
    }

    @Override
    public void process(final String command) {
        final int INDEX_DATABASE_NAME = 1;
        String[] data = command.split("\\|");
        if (data.length != parametersLength(getCommandFormat())) {
            throw new IllegalArgumentException("Expected command getCommandFormat '"+ getCommandFormat()+"', but actual '" + command + "'");
        }
        manager.createTable(data[INDEX_DATABASE_NAME]);
        view.write(String.format("Table '%s' created successfully", data[1]));
    }
}
