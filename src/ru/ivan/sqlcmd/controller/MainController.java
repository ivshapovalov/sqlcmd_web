package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.model.DataSet;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.view.View;

import java.util.List;

/**
 * Created by Ivan on 20.09.2016.
 */
public class MainController {

    private final View view;
    private final DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.manager = manager;
        this.view = view;
    }

    public void main(String[] args) {
        connectToDB();
    }

    public void run() {
        connectToDB();

        while (true) {
            String command = readCommand();
            if (command.equals("list")) {
                doList();
            } else if (command.equals("help")) {
                doHelp();     }
            else if (command.startsWith("find")) {
                doFind(command);
            } else if (command.equals("exit")) {
               System.exit(0);
            } else {
                view.write("Такая команда отсутствует");
            }

        }

    }

    private void doFind(String command) {
        String[] data=command.split("[|]");
        String table=data[1];
        List<DataSet> tableData=manager.getTableData(table);
        List<String> tableHeaders=manager.getTableColumns(table);
        printHeader(tableHeaders);
        printTable(tableData);

    }

    private void printTable(List<DataSet> tableData) {
        for (DataSet row:tableData
             ) {
            printRow(row);
        }


    }

    private void printRow(DataSet row) {
        List<Object> values=row.getValues();
        String string="";
        for (Object column:values
                ) {
            string+=column+"\t"+"|";
        }
        view.write(string);
    }

    private void printHeader(List<String> tableHeaders) {
        String header="";
        for (String column:tableHeaders
                ) {
            header+=column+"\t"+"|";
        }
        view.write(header);
    }

    private void doHelp() {
        view.write("Существующие команды:");
        view.write("list    -   вывод имен всех таблиц базы");
        view.write("help    -   вывод списка всех команд");
        view.write("exit    -   выход из программы");
        view.write("find|table    -   вывод содержимого таблицы table ");

    }

    private void doList() {
        view.write(manager.getTablesNames().toString());
    }

    private String readCommand() {
        view.write("Введите команду или help для помощи");
        return view.read();
    }

    private void connectToDB() {
        view.write("Привет, юзер");
        while (true) {
            view.write("Введи имя базы данных, имя пользователя и пароль в формате" +
                    " database|user|password");
            try {
                String string = view.read();
                string="sqlcmd|postgres|postgres";
                String[] data = string.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Количество параметров - " + data.length + ". Ожидается - 3");
                }
                String database = data[0];
                String user = data[1];
                String password = data[2];
                manager.connect(database, user, password);
                view.write("Подключение успешно");
                break;
            } catch (Exception e) {
                printError(e);

            }

        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message = message + " " + e.getCause().getMessage();
        }
        view.write("Неудача по причине: " + message);
        view.write("Повтори попытку");
    }
}
