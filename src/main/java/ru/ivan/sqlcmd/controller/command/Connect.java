package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

public class Connect extends Command {

    public Connect() {
    }

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view=view;
    }

    @Override
    public String description() {
        return "подключение к базе данных";
    }

    @Override
    public String format() {
        return "connect|sqlcmd|postgres|postgres";
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
                String[] data = command.split("[|]");

                if (data.length != parametersLength(format())) {
                    throw new IllegalArgumentException(String.format("Количество параметров разделенных символом '|' - %s. Ожидается - %s",
                            data.length,parametersLength(format())));
                }
                String database = data[1];
                String user = data[2];
                String password = data[3];
                manager.connect(database, user, password);
                view.write(String.format("Подключение к базе '%s' прошло успешно!", database));
    }
}
