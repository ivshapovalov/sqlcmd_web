package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Connect extends Command {

    private static String COMMAND_SAMPLE="connect|sqlcmd|postgres|postgres";
    private DatabaseManager manager;
    private View view;

    @Override
    public String description() {
        return "connects to database";
    }

    @Override
    public String format() {
        return "connect|sqlcmd|postgres|postgres";
    }

    public Connect() {
    }

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {

                String[] data = command.split("[|]");

                if (data.length != parametersLength()) {
                    throw new IllegalArgumentException(String.format("Количество параметров разделенных символом '|' - %s. Ожидается - %s",
                            data.length,parametersLength()));
                }
                String database = data[1];
                String user = data[2];
                String password = data[3];
                manager.connect(database, user, password);
                view.write("Подключение успешно");
    }

    private int parametersLength() {
        return COMMAND_SAMPLE.split("[|]").length;
    }



}
