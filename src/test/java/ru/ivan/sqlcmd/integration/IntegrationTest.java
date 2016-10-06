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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;
import static ru.ivan.sqlcmd.controller.command.History.*;

public class IntegrationTest {
    private final static String TABLE_NAME = "test";
    private final static String NOT_EXIST_TABLE = "notexisttable";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String DB_NAME2 = "test";
    private final static String TABLE_NAME2 = "qwe";
    private final static String SQL_CREATE_TABLE2 = TABLE_NAME2 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";

    private static final PropertiesLoader pl = new PropertiesLoader();
    private static final String DB_USER = pl.getUserName();
    private static final String DB_PASSWORD = pl.getPassword();
    private static final String DB_NAME = pl.getDatabaseName();
    private static DatabaseManager manager;
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.dropDatabase(DB_NAME2);

        manager.createDatabase(DB_NAME);
        manager.createDatabase(DB_NAME2);
        manager.disconnect();

        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);
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
                        "\t\tconnect|sqlcmd|postgres|postgres -- подключение к базе данных\n" +
                        "\t\tcreateDatabase|databaseName -- создание новой базы данных\n" +
                        "\t\tcreateTable|tableName -- создание новой таблицы\n" +
                        "\t\tdatabases -- список баз данных\n" +
                        "\t\tdeleteRow|tableName|ID -- удаление строки из таблицы с заданным ID\n" +
                        "\t\tdisconnect -- отключение от базы данных\n" +
                        "\t\tdropTable|tableName -- удаление таблицы\n" +
                        "\t\tdropAllTables -- удаление всех таблиц\n" +
                        "\t\tdropDatabase|databaseName -- удаление базы данных\n" +
                        "\t\tdropAllDatabases -- удаление всех баз данных\n" +
                        "\t\texit -- выход из приложения\n" +
                        "\t\thelp -- список возможных команд с описанием\n" +
                        "\t\thistory -- список последних 3 введенных команд\n" +
                        "\t\tinsertRow|tableName|column1|value1|column2|value2|...|columnN|valueN -- вставка строки в таблицу\n" +
                        "\t\trows|tableName -- список строк таблицы\n" +
                        "\t\tsize|tableName -- размер таблицы\n" +
                        "\t\ttables -- список таблиц текущей базы данных\n" +
                        "\t\ttruncateAll -- очистка всех таблиц\n" +
                        "\t\ttruncateTable|tableName -- очистка таблицы\n" +
                        "\t\tupdateRow|tableName|ID -- обновление строки с заданным ID в таблице\n" +
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
                "[qwe, test]\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testSizeAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|1111|name|Peter|password|****");
        in.add("size|"+TABLE_NAME);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                "Введите команду или help для помощи\n" +
                "test size is 1\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testSizeWithoutParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|1111|name|Peter|password|****");
        in.add("size|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Должно быть два параметра в формате size|tableName\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testSizeWithTwoParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|1111|name|Peter|password|****");
        in.add("size|"+TABLE_NAME+"|afa|adfasf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Должно быть два параметра в формате size|tableName\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testSizeWithIllegalTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|1111|name|Peter|password|****");
        in.add("size|"+NOT_EXIST_TABLE);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Не возможно получить размер таблицы notexisttable ОШИБКА: отношение \"notexisttable\" не существует\n" +
                "  Позиция: 32\n" +
                "Повтори попытку\n" +
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
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testRowsAfterTruncate() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|1111|name|Peter|password|****");
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
                        "Удаляем данные с таблицы 'test'. Y/N\n" +
                        "Таблица test была успешно очищена.\n" +
                        "Введите команду или help для помощи\n" +
                        "В таблице 'test' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                        "Введите команду или help для помощи\n" +
                        "+----+-----+--------+\n" +
                        "|id  |name |password|\n" +
                        "+----+-----+--------+\n" +
                        "|1111|Peter|****    |\n" +
                        "+----+-----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "Удаляем данные с таблицы 'test'. Y/N\n" +
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
    public void testRowsWithIllegalParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_NAME+"|dsfaf");
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
                        "Удаляем данные с таблицы 'test'. Y/N\n" +
                        "Таблица test была успешно очищена.\n" +
                        "Введите команду или help для помощи\n" +
                        "В таблице 'test' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                        "Введите команду или help для помощи\n" +
                        "Неудача по причине: Должно быть два параметра в формате rows|tableName\n" +
                        "Повтори попытку\n" +
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
                "[qwe, test]\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'test' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "[]\n" +
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
                "Отключение успешно\n" +
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
                "Удаляем данные с таблицы 'test'. Y/N\n" +
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
    public void testInsertRowInIllegalTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("insertRow|" + NOT_EXIST_TABLE + "|id|13|name|Stiven|password|*****");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Невозможно вставить строку в таблицу notexisttable.  Таблицы не существует\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testInsertRowWithIllegalData() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id5|13|namef|Stiven|password|*****");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Невозможно вставить строку в таблицу test. Столбец \"id5\" в таблице \"test\" не существует\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testInsertRowWithoutParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Должно быть четное количество параметров в формате insertRow|tableName|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testHistory() {
        // given
        in.add("history|3");
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("tables");
        in.add("history");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Установлен размер хранимой истории введенных команд. Новый размер - 3 \n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "[qwe, test]\n" +
                "Введите команду или help для помощи\n" +
                "3. truncateTable|test\n" +
                "4. tables\n" +
                "5. history\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testHistoryWithNotNumericCapacity() {
        // given
        in.add("history|gfh");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Неверно указан размер хранимой истории!\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }



    @Test
    public void testUpdateRowInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|13|name|Stiven|password|*****");
        in.add("updateRow|" + TABLE_NAME + "|13|name|Pupkin|password|+++++");
        in.add("rows|" + TABLE_NAME);
        in.add("truncateTable|"+TABLE_NAME);
        in.add("y");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=13, name=Stiven, password=*****}\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно обновлена запись {name=Pupkin, password=+++++}\n" +
                "Введите команду или help для помощи\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Pupkin|+++++   |\n" +
                "+--+------+--------+\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUpdateRowInNotExistingTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + NOT_EXIST_TABLE + "|14|name|Pupkin|password|+++++");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: В таблице notexisttable не возможно обновить запись с id=14.  Таблицы не существует\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUpdateRowWithNotExistingColumnInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + TABLE_NAME + "|14|nam|Pupkin|password|+++++");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: В таблице test не возможно обновить запись с id=14. Столбец \"nam\" в таблице \"test\" не существует\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUpdateRowWithoutParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Должно быть четное количество параметров большее или равное 4 в формате updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUpdateRowWithTwoParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + TABLE_NAME + "");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Должно быть четное количество параметров большее или равное 4 в формате updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUpdateRowWithThreeParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|13|name|Stiven|password|*****");
        in.add("updateRow|" + TABLE_NAME + "|13|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=13, name=Stiven, password=*****}\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Должно быть четное количество параметров большее или равное 4 в формате updateRow|tableName|ID|column1|value1|column2|value2|...|columnN|valueN\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUpdateRowWithNotNumericID() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("updateRow|" + TABLE_NAME + "|fgr|name|Ivan");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Третий параметр ID не может быть преобразован к числовому!\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDeleteRowInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|13|name|Stiven|password|*****");
        in.add("insertRow|" + TABLE_NAME + "|id|14|name|Kevin|password|-----");
        in.add("deleteRow|" + TABLE_NAME + "|13");
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
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=13, name=Stiven, password=*****}\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=14, name=Kevin, password=-----}\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно удалена запись c ID=13\n" +
                "Введите команду или help для помощи\n" +
                "+--+-----+--------+\n" +
                "|id|name |password|\n" +
                "+--+-----+--------+\n" +
                "|14|Kevin|-----   |\n" +
                "+--+-----+--------+\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDeleteNotExistingRowInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|13|name|Stiven|password|*****");
        in.add("deleteRow|" + TABLE_NAME + "|14");
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
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=13, name=Stiven, password=*****}\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно удалена запись c ID=14\n" +
                "Введите команду или help для помощи\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|13|Stiven|*****   |\n" +
                "+--+------+--------+\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }
    @Test
    public void testDeleteRowInNotExistingTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|" + NOT_EXIST_TABLE + "|13");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Невозможно удалить строку из таблицы notexisttable.  Таблицы не существует\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDeleteRowWithoutParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'deleteRow|tableName|ID', а ты ввел: deleteRow|\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDeleteRowWithTwoParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|" + TABLE_NAME + "");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'deleteRow|tableName|ID', а ты ввел: deleteRow|test\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDeleteWithMoreThanThreeParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|" + TABLE_NAME);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME + "|id|13|name|Stiven|password|*****");
        in.add("deleteRow|" + TABLE_NAME + "|13|adsf|asdfa");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем данные с таблицы 'test'. Y/N\n" +
                "Таблица test была успешно очищена.\n" +
                "Введите команду или help для помощи\n" +
                "В таблице 'test' успешно создана запись {id=13, name=Stiven, password=*****}\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'deleteRow|tableName|ID', а ты ввел: deleteRow|test|13|adsf|asdfa\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDeleteRowWithNotNumericID() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("deleteRow|" + TABLE_NAME + "|fgr");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Третий параметр ID не может быть преобразован к числовому!\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }
}
