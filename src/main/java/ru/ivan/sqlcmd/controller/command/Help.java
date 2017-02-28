package ru.ivan.sqlcmd.controller.command;

import java.util.Arrays;
import java.util.List;

public class Help extends AbstractCommand {

    public Help() {

    }

    public List<Command> getCommands() {
        return Arrays.asList(
                new Help(),
                new History(),
                new Exit(),
                new Disconnect(),
                new Connect(),
                new IsConnected(),
                new CreateTable(),
                new DeleteRow(),
                new DropAllTables(),
                new DropTable(),
                new InsertRow(),
                new Rows(),
                new Size(),
                new Tables(),
                new TruncateAllTables(),
                new TruncateTable(),
                new UpdateRow(),
                new Unsupported(),
                new HistoryCommand()
        );
    }

    @Override
    public String getDescription() {
        return "list of all commands with description";
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
                view.write("\t\t" + "'" + currentCommand.getCommandFormat() + "'" + " -- " +
                        currentCommand.getDescription());
            }
        }
    }
}
