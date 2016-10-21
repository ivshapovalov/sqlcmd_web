package ru.ivan.sqlcmd.controller.command;

class IsConnected extends AbstractCommand  {

    IsConnected() {

    }

    public boolean showInHelp() {
        return false;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getCommandFormat() {
        return "";
    }

    @Override
    public boolean canProcess(final String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(final String command) {
        view.write(String.format("You can't use '%s'. First connect with the command 'connect|database|user|password'", command));

    }

}
