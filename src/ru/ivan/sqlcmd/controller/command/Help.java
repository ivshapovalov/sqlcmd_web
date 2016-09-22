package ru.ivan.sqlcmd.controller.command;

import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 21.09.2016.
 */
public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view=view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Существующие команды:");
        view.write("connect|database|user|password    -   подключение к базе данных");
        view.write("clear|table    -   очистка таблицы table от данных"); //TODO переспросить юзера вдруг случайно
        view.write("create|table|column1|value1|column2|value2|...|columnN|valueN    -   создание записи в таблиц table");
        view.write("list    -   вывод имен всех таблиц базы");
        view.write("help    -   вывод списка всех команд");
        view.write("exit    -   выход из программы");
        view.write("find|table    -   вывод содержимого таблицы table ");

    }
}
