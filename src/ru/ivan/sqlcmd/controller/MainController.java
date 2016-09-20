package ru.ivan.sqlcmd.controller;

import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.InMemoryDatabaseManager;
import ru.ivan.sqlcmd.model.JDBCDatabaseManager;
import ru.ivan.sqlcmd.view.Console;
import ru.ivan.sqlcmd.view.View;

/**
 * Created by Ivan on 20.09.2016.
 */
public class MainController {

    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        view.write("Привет, юзер");
        while (true) {
            view.write("Введи имя базы данных, имя пользователя и пароль в формате" +
                    " database|user|password");
            String string = view.read();
            String[] data = string.split("[|]");
            String database = data[0];
            String user = data[1];
            String password = data[2];

            try {
                manager.connect(database, user, password);
                view.write("Подключение успешно");
                break;
            } catch (Exception e) {
                String message=e.getMessage();
                if (e.getCause()!=null) {
                    message=message+" "+e.getCause().getMessage();
                }
                view.write("Неудача по причине: "+message);
                view.write("Повтори попытку");

            }
        }


    }
}
