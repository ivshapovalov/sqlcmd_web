package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help extends Command {

    private List<Command> commands;

    public Help(View view) {
        super(view);
        this.commands = new ArrayList<>(Arrays.asList(
                new Connect(),
                new CreateDatabase(),
                new CreateTable(),
                new Databases(),
                new DeleteRow(),
                new Disconnect(),
                new DropTable(),
                new DropAllTables(),
                new DropDatabase(),
                new DropAllDatabases(),
                new Exit(),
                this,
                new History(),
                new InsertRow(),
                new Rows(),
                new Size(),
                new Tables(),
                new TruncateAllTables(),
                new TruncateTable(),
                new UpdateRow()
        ));

    }

    @Override
    public String description() {
        return "список возможных команд с описанием";
    }

    @Override
    public String format() {
        return "help";
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals(format());
    }

    @Override
    public void process(String command) {

        view.write("Existing program commands:");
        for (Command currentCommand : commands) {
            view.write("\t\t" + currentCommand.format() + " -- " + currentCommand.description());
        }
    }
}
