package ru.ivan.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ivan.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


/**
 * Created by Ivan on 21.09.2016.
 */
public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void setup() {
        out=new ByteArrayOutputStream();
        in=new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Before
    public void clearIn(){
        try {
            in.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHelp(){
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Существующие команды:\r\n" +
                "connect|database|user|password    -   подключение к базе данных\r\n" +
                "clear|table    -   очистка таблицы table от данных\r\n" +
                "create|table|column1|value1|column2|value2|...|columnN|valueN    -   создание записи в таблиц table\r\n"+
                "list    -   вывод имен всех таблиц базы\r\n" +
                "help    -   вывод списка всех команд\r\n" +
                "exit    -   выход из программы\r\n" +
                "find|table    -   вывод содержимого таблицы table \r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    public String getData() {
        try {
            String result= new String(out.toByteArray(),"UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    @Test
    public void testExit(){
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testListWithoutConnect(){
        //given
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Вы не можете пользоваться командой 'list' пока не подлючитесь с помощью команды connect|database|user|password\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }
    @Test
    public void testListAfterConnect(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "[users]\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }
    @Test
    public void testFindWithoutConnect(){
        //given
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Вы не можете пользоваться командой 'find|users' пока не подлючитесь с помощью команды connect|database|user|password\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testFindAfterConnect(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "id\t|name\t|password\t|\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testConnectAfterConnect(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testConnectWithError(){
        //given
        in.add("connect|sqlcmd|");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Неудача по причине: Количество параметров разделенных символом '|' - 2. Ожидается - 4\r\n" +
                "Повтори попытку\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testUnsopportedWithoutConnect(){
        //given
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Вы не можете пользоваться командой 'unsupported' пока не подлючитесь с помощью команды connect|database|user|password\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testUnsopportedAfterConnect(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "Такая команда отсутствует - unsupported\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testFindAfterConnectWithData(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|users");
        in.add("create|users|id|13|name|Stiven|password|*****");
        in.add("create|users|id|14|name|Eva|password|+++++");
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "Таблица users была успешно очищена\r\n"+
                "Введите команду или help для помощи\r\n" +
                "В таблице 'users' успешно создана запись DataSet{" +
                "names: [id, name, password] , " +
                "values: [13, Stiven, *****]" +
                "}\r\n"+
                "Введите команду или help для помощи\r\n" +
                "В таблице 'users' успешно создана запись DataSet{" +
                "names: [id, name, password] , " +
                "values: [14, Eva, +++++]" +
                "}\r\n"+
                "Введите команду или help для помощи\r\n" +
                "id\t|name\t|password\t|\r\n" +
                "13\t|Stiven\t|*****\t|\r\n" +
                "14\t|Eva\t|+++++\t|\r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }
    @Test
    public void testClearAfterConnectWithIllegalNumberOfArguments(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|users|asfaf");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "Неудача по причине: Формат команды clear|table, а ты ввел clear|users|asfaf\r\n"+
                "Повтори попытку\r\n"+
                     "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testClearAfterConnectWithIllegalTableName(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|user");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "Неудача по причине: Таблица user не существует\r\n"+
                "Повтори попытку\r\n"+
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    @Test
    public void testCreateAfterConnectWithIllegalData(){
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|users");
        in.add("create|users|id|name|Stiven|password|*****");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет, юзер\r\n" +
                "Введи имя базы данных, имя пользователя и пароль в формате database|user|password\r\n" +
                "Подключение успешно\r\n" +
                "Введите команду или help для помощи\r\n" +
                "Таблица users была успешно очищена\r\n"+
                "Введите команду или help для помощи\r\n" +
                "Неудача по причине: Должно быть четное количество параметров в формате create|table|column1|value1|column2|value2|...|columnN|valueN\r\n"+
                "Повтори попытку\r\n"+
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }
}
