package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Help extends Command {

    private List<Command> commands;

    @Override
    public String description() {
        return "list all commands description";
    }

    @Override
    public String format() {
        return "help";
    }


    public Help(View view) {
        super(view);
        this.commands = new ArrayList<>(Arrays.asList(
                new History(),
                new Connect(),
                new CreateDatabase(),
                new CreateTable(),
                new Databases(),
                new DropDatabase(),
                new DropAllDatabases(),
                new DropTable(),
                new DropTable(),
                new TruncateAllTables(),
                new TruncateTable(),
                this,
                new Exit(),
                new Disconnect(),
                new InsertRow(),
                new UpdateRow(),
                new Rows(),
                new Tables()));

    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {

        view.write("Existing program commands:");
        for (Command currentCommand: commands) {
            view.write("\t\t" + currentCommand.format() + " -- " + currentCommand.description());
        }
    }
}
