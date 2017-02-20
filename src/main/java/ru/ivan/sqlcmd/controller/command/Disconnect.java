package ru.ivan.sqlcmd.controller.command;

class Disconnect extends AbstractCommand {

    Disconnect() {
    }

    @Override
    public String getDescription() {
        return "disconnect from current database";
    }

    @Override
    public String getCommandFormat() {
        return "disconnect";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.startsWith(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        manager.disconnect();
        view.write("Disconnect successful");
    }

}
