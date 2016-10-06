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

public class IntegrationTestCreateDropTruncate {

    private final static String DB_NAME = "dbtest";
    private final static String TABLE_NAME1 = "test1";
    private final static String TABLE_NAME2 = "test2";
    private static DatabaseManager manager;
    private static final PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
        manager.disconnect();
    }

    @AfterClass
    public static void clearAfterAllTests() {

    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDatabase(DB_NAME);
        manager.createDatabase(DB_NAME);
        manager.disconnect();
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
    public void testCreateAndDropDatabase() {
        // given
        String testDB = "dbtest2";
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("createDatabase|" + testDB);
        in.add("databases");
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("databases");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем базу данных 'dbtest2'. Y/N?\n" +
                "База данных dbtest2 была успешно удалена.\n" +
                "Введите команду или help для помощи\n" +
                "База данных dbtest2 была успешно создана.\n" +
                "Введите команду или help для помощи\n" +
                "***Текущие базы данных***\n" +
                "postgres\n" +
                "dbtest\n" +
                "dbtest2\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем базу данных 'dbtest2'. Y/N?\n" +
                "База данных dbtest2 была успешно удалена.\n" +
                "Введите команду или help для помощи\n" +
                "***Текущие базы данных***\n" +
                "postgres\n" +
                "dbtest\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateAndDropAllDatabases() {

        // Осторожно. Тест удаляет ВСЕ базы данных вообще.

        // given
        String testDB = "dbtest2";
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("createDatabase|" + testDB);
        in.add("databases");
        in.add("dropAllDatabases");
        in.add("y");
        in.add("databases");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем базу данных 'dbtest2'. Y/N?\n" +
                "База данных dbtest2 была успешно удалена.\n" +
                "Введите команду или help для помощи\n" +
                "База данных dbtest2 была успешно создана.\n" +
                "Введите команду или help для помощи\n" +
                "***Текущие базы данных***\n" +
                "postgres\n" +
                "dbtest\n" +
                "dbtest2\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем все базы данных? Y/N\n" +
                "Все базы данных были успешно удалены.\n" +
                "Введите команду или help для помощи\n" +
                "***Текущие базы данных***\n" +
                "postgres\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDropCurrentDatabase() {
        // given
        String testDB = "dbtest2";
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|" + testDB);
        in.add("y");
        in.add("createDatabase|" + testDB);
        in.add("connect|" + testDB + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("databases");
        in.add("dropDatabase|"+testDB);
        in.add("y");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем базу данных 'dbtest2'. Y/N?\n" +
                "База данных dbtest2 была успешно удалена.\n" +
                "Введите команду или help для помощи\n" +
                "База данных dbtest2 была успешно создана.\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'dbtest2' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "***Текущие базы данных***\n" +
                "postgres\n" +
                "dbtest\n" +
                "dbtest2\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем базу данных 'dbtest2'. Y/N?\n" +
                "Ошибка удаления базы данных 'dbtest2', по причине: Не возможно удалить таблицу dbtest2\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testTruncateAllTables() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|" + TABLE_NAME1);
        in.add("y");
        in.add("createTable|" + TABLE_NAME1 + " (id INTEGER,name text,password text)");
        in.add("truncateTable|" + TABLE_NAME1);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME1 + "|id|1111|name|Peter|password|****");
        in.add("rows|" + TABLE_NAME1);
        in.add("createTable|" + TABLE_NAME2 + " (id INTEGER,name text,password text)");
        in.add("truncateTable|" + TABLE_NAME2);
        in.add("y");
        in.add("insertRow|" + TABLE_NAME2 + "|id|2222|name|Ivan|password|++++");
        in.add("rows|" + TABLE_NAME2);
        in.add("truncateAllTables");
        in.add("y");
        in.add("rows|" + TABLE_NAME1);
        in.add("rows|" + TABLE_NAME2);
        in.add("dropAllTables");
        in.add("y");
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Привет, юзер\n" +
                        "Введите команду или help для помощи\n" +
                        "Подключение к базе 'dbtest' прошло успешно!\n" +
                        "Введите команду или help для помощи\n" +
                        "Удаляем таблицу 'test1'. Y/N?\n" +
                        "Таблица test1 была успешно удалена.\n" +
                        "Введите команду или help для помощи\n" +
                        "Таблица test1 (id INTEGER,name text,password text) была успешно создана.\n" +
                        "Введите команду или help для помощи\n" +
                        "Удаляем данные с таблицы 'test1'. Y/N\n" +
                        "Таблица test1 была успешно очищена.\n" +
                        "Введите команду или help для помощи\n" +
                        "В таблице 'test1' успешно создана запись {id=1111, name=Peter, password=****}\n" +
                        "Введите команду или help для помощи\n" +
                        "+----+-----+--------+\n" +
                        "|id  |name |password|\n" +
                        "+----+-----+--------+\n" +
                        "|1111|Peter|****    |\n" +
                        "+----+-----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "Таблица test2 (id INTEGER,name text,password text) была успешно создана.\n" +
                        "Введите команду или help для помощи\n" +
                        "Удаляем данные с таблицы 'test2'. Y/N\n" +
                        "Таблица test2 была успешно очищена.\n" +
                        "Введите команду или help для помощи\n" +
                        "В таблице 'test2' успешно создана запись {id=2222, name=Ivan, password=++++}\n" +
                        "Введите команду или help для помощи\n" +
                        "+----+----+--------+\n" +
                        "|id  |name|password|\n" +
                        "+----+----+--------+\n" +
                        "|2222|Ivan|++++    |\n" +
                        "+----+----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "Удаляем данные из всех таблиц?. Y/N\n" +
                        "Все таблицы были успешно очищены.\n" +
                        "Введите команду или help для помощи\n" +
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "+--+----+--------+\n" +
                        "|id|name|password|\n" +
                        "+--+----+--------+\n" +
                        "Введите команду или help для помощи\n" +
                        "Удаляем все таблицы? Y/N\n" +
                        "Все таблицы были успешно удалены.\n" +
                        "Введите команду или help для помощи\n" +
                        "[]\n" +
                        "Введите команду или help для помощи\n" +
                        "Отключение успешно\n" +
                        "Введите команду или help для помощи\n" +
                        "До скорой встречи!\n", getData());
    }


    @Test
    public void testTruncateWithError() {
        // given
        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("truncateTable|sadfasd|fsf|fdsf");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'truncateTable|tableName', а ты ввел: truncateTable|sadfasd|fsf|fdsf\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateTableWithIllegalParameters() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|" + TABLE_NAME1);
        in.add("y");
        in.add("createTable|" + TABLE_NAME1 + "()|asfdasf|||");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе 'dbtest' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Удаляем таблицу 'test1'. Y/N?\n" +
                "Таблица test1 была успешно удалена.\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'createTable|tableName(column1,column2,..,columnN), а ты ввел: createTable|test1()|asfdasf|||\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateDatabaseWithIllegalParameters() {
        // given

        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("createDatabase|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'createDatabase|databaseName', а ты ввел: createDatabase|\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDropDatabaseWithoutParameters() {
        // given

        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropDatabase|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'dropDatabase|databaseName', а ты ввел: dropDatabase|\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testDropTableWithoutParameters() {
        // given

        in.add("connect|" + "" + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("dropTable|");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет, юзер\n" +
                "Введите команду или help для помощи\n" +
                "Подключение к базе '' прошло успешно!\n" +
                "Введите команду или help для помощи\n" +
                "Неудача по причине: Формат команды 'dropTable|tableName', а ты ввел: dropTable|\n" +
                "Повтори попытку\n" +
                "Введите команду или help для помощи\n" +
                "Отключение успешно\n" +
                "Введите команду или help для помощи\n" +
                "До скорой встречи!\n", getData());
    }
}
