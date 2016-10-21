package ru.ivan.sqlcmd.controller.command;

class Unsupported extends AbstractCommand  {
    Unsupported() {

    }

    @Override
    public String getDescription() {
        return "unsupported operation";
    }

    public boolean showInHelp() {
        return false;
    }

    @Override
    public String getCommandFormat() {
        return "";
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("This command does not exist - " + command);
    }
}
