package ru.ivan.sqlcmd.controller.command;

import java.util.Arrays;
import java.util.List;

public class Help extends AbstractCommand {

    public Help() {

    }

    public List<Command> getCommands() {
        List<Command> commands=
                Arrays.asList(
                        new Help(),
                        new History(),
                        new Exit(),
                        new Disconnect(),
                        new Connect(),
                        new IsConnected(),
                        new Size(),
                        new CreateDatabase(),
                        new DeleteRow(),
                        new CreateTable(),
                        new Databases(),
                        new DropDatabase(),
                        new DropAllDatabases(),
                        new DropTable(),
                        new DropAllTables(),
                        new TruncateAllTables(),
                        new TruncateTable(),
                        new InsertRow(),
                        new UpdateRow(),
                        new Rows(),
                        new Tables(),
                        new Unsupported());
        return commands;
    }

    @Override
    public String getDescription() {
        return "list of all commands with getDescription";
    }

    @Override
    public String getCommandFormat() {
        return "help";
    }

    @Override
    public boolean canProcess(final String command) {
        return command.equals(getCommandFormat());
    }

    @Override
    public void process(final String command) {
        List<Command> commands = getCommands();
        view.write("Existing program commands:");
        for (Command currentCommand : commands) {
            if (currentCommand.showInHelp()) {
                view.write("\t\t" + "'" + currentCommand.getCommandFormat() + "'" + " -- " + currentCommand.getDescription());
            }
        }
    }
}
