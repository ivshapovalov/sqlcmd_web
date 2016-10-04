package ru.ivan.sqlcmd.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ivan.sqlcmd.controller.Main;
import ru.ivan.sqlcmd.model.DatabaseManager;
import ru.ivan.sqlcmd.model.PostgreSQLManager;
import ru.ivan.sqlcmd.model.PropertiesLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private final static String TABLE_NAME = "test";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String DB_NAME2 = "test";
    private final static String TABLE_NAME2 = "qwe";
    private final static String SQL_CREATE_TABLE2 = TABLE_NAME2 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";

    private static DatabaseManager manager;
    private static final PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    private final static String DB_NAME = pl.getDatabaseName();
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);

        manager.createDatabase(DB_NAME);
        manager.createDatabase(DB_NAME2);

        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);
        manager.disconnect();

        manager.connect(DB_NAME2, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE2);
        manager.disconnect();

    }

    @AfterClass
    public static void clearAfterAllTests() {

        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.dropDatabase(DB_NAME2);
    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Привет, юзер\n" +
                        "Введите команду или help для помощи\n" +
                        "Existing program commands:\n" +
                        "\t\thistory -- list history of commands\n" +
                        "\t\tconnect|sqlcmd|postgres|postgres -- connects to database\n" +
                        "\t\tcreateDatabase|databaseName -- create new database\n" +
                        "\t\tcreateTable|tableName -- create new table\n" +
                        "\t\tdatabases -- list all databases\n" +
                        "\t\tdropTable|tableName -- drop table \n" +
                        "\t\tdropAllTables -- drop all tables\n" +
                        "\t\tdropDatabase|databaseName -- drop database\n" +
                        "\t\tdropAllDatabases -- drop all databases\n" +
                        "\t\ttruncateTable|tableName -- delete all rows in table\n" +
                        "\t\ttruncateAll -- delete all rows in all tables\n" +
                        "\t\tsize|tableName -- return size of the table\n" +
                        "\t\thelp -- list all commands description\n" +
                        "\t\texit -- exit from application\n" +
                        "\t\tdisconnect -- disconnect from current database\n" +
                        "\t\tinsertRow|tableName|column1|value1|column2|value2|...|columnN|valueN -- insert row into the table\n" +
                        "\t\tupdateRow|tableName|ID -- update the entry in the table using the ID\n" +
                        "\t\tdeleteRow|tableName|ID -- delete the entry in the table using the ID\n" +
                        "\t\trows|tableName -- list all rows in table\n" +
                        "\t\ttables -- list all tables\n" +
                        "Введите команду или help для помощи\n" +
                        "До скорой встречи!\n", getData());
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testExit() {
        // given

        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Вы не можете пользоваться командой 'tables' пока не подлючитесь с помощью команды connect|database|user|password\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testRowsWithoutConnect() {
        // given
        in.add("rows|" + TABLE_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Вы не можете пользоваться командой 'rows|test' пока не подлючитесь с помощью команды connect|database|user|password\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Вы не можете пользоваться командой 'unsupported' пока не подлючитесь с помощью команды connect|database|user|password\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("unsupported");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Такая команда отсутствует - unsupported\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testTablesAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "[test]\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testBadConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD + "WRONG");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Невозможно подключиться к базе данных :sqlcmd, user:postgres, password:postgresWRONG Ошибка при попытке подсоединения.\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Вы не можете пользоваться командой 'disconnect' пока не подлючитесь с помощью команды connect|database|user|password\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testRowsAfterClear() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("create|" + TABLE_NAME + "|id|1111|name|serge|pass|****");
        in.add("rows|" + TABLE_NAME);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("rows|" + TABLE_NAME);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Привет, юзер\n" +
                        "Введите команду или help для помощи\n" +
                        "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                        "Введите команду или help для помощи\n" +
                        "Такая команда отсутствует - create|test|id|1111|name|serge|pass|****\n" +
                        "Введите команду или help для помощи\n" +
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "\u001B[31mУдаляем данные с таблицы 'test'. Y/N\u001B[0m\n" +
                        "Таблица test была успешно очищена.\n" +
                        "Введите команду или help для помощи\n" +
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "Отключение успешно\n" +
                        "Введите команду или help для помощи\n" +
                        "До скорой встречи!\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");

        in.add("connect|" + DB_NAME2 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "[test]\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'test' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "[qwe]\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|" + DB_NAME);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Количество параметров разделенных символом '|' - 2. Ожидается - 4\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Вы не можете пользоваться командой 'disconnect' пока не подлючитесь с помощью команды connect|database|user|password\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testInsertRowInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");

        in.add("insertRow|" + TABLE_NAME + "|id|13|name|Stiven|password|*****");
        in.add("insertRow|" + TABLE_NAME + "|id|14|name|Pupkin|password|+++++");
        in.add("rows|" + TABLE_NAME);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "\u001B[31mУдаляем данные с таблицы 'test'. Y/N\u001B[0m\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=13, name=Stiven, password=*****}\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=14, name=Pupkin, password=+++++}\n" +
                "Введите команду или help для помощи\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Stiven|*****   |\n" +
                "+--+------+--------+\n" +
                "|14|Pupkin|+++++   |\n" +
                "+--+------+--------+\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|sadfasd|fsf|fdsf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'truncateTable|tableName', а ты ввел: truncateTable|sadfasd|fsf|fdsf\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("create|" + TABLE_NAME + "|error");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Такая команда отсутствует - create|test|error\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }
}
