package ru.ivan.sqlcmd.integration;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.ivan.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
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
                "list    -   вывод имен всех таблиц базы\r\n" +
                "help    -   вывод списка всех команд\r\n" +
                "exit    -   выход из программы\r\n" +
                "find|table    -   вывод содержимого таблицы table \r\n" +
                "Введите команду или help для помощи\r\n" +
                "До скорой встречи!\r\n",getData());


    }

    public String getData() {
        try {
            return new String(out.toByteArray(),"UTF-8");
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
    public void testUnsopportedWithConnect(){
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
}
