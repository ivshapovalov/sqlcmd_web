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
        view.write("1...N    -  вызов команды из списка предыдущих команд (см. history)");
        view.write("history    -  вывод списка предыдущих команд");
        view.write("disconnect    -   отключение от базе данных");
        view.write("createDatabase|databaseName    -   создание база данных databaseName");
        view.write("dropTable|tableName    -   очистка таблицы tableName от данных");
        view.write("dropDatabase|databaseName    -   очистка базы данных databaseName от таблиц");
        view.write("dropAllTables    -   очистка всех таблиц от данных");
        view.write("dropAllDatabases    -   удаление всех баз данных");
        view.write("createTable|tableName    -   создание таблицы tableName");
        view.write("insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN    -   создание записи в таблице tableName");
        view.write("find|tableName    -   вывод содержимого таблицы tableName ");
        view.write("databases    -   вывод имен всех баз данных");
        view.write("tables    -   вывод имен всех таблиц базы");
        view.write("help    -   вывод списка всех команд");
        view.write("exit    -   выход из программы");


    }
}
