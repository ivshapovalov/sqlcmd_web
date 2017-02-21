package ru.ivan.sqlcmd.controller.command;

class HistoryCommand extends AbstractCommand {

    HistoryCommand() {
    }

    @Override
    public boolean showInHelp() {
        return false;
    }

    @Override
    public String getDescription() {
        return " command from history by number";
    }

    @Override
    public String getCommandFormat() {
        return "1 - N";
    }

    @Override
    public boolean canProcess(final String command) {
        return true;
    }

    @Override
    public void process(final String command) {

    }

}
