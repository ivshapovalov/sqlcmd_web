package ru.ivan.sqlcmd.model;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.junit.Test;
import ru.ivan.sqlcmd.controller.MainController;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TableConstructorTest {

    static {
        Logger.getRootLogger().addAppender(new NullAppender());
    }
    @Test
    public void testTableConstructorWithOneParameters() {

        TableConstructor table = initTableConstructor();

        assertEquals("+--+----------+--------+"+ MainController.LINE_SEPARATOR+"" +
                "|id|name      |password|"+MainController.LINE_SEPARATOR+"" +
                "+--+----------+--------+"+MainController.LINE_SEPARATOR+"" +
                "|1 |FirstUser |+++++   |"+MainController.LINE_SEPARATOR+"" +
                "+--+----------+--------+"+MainController.LINE_SEPARATOR+"" +
                "|2 |SecondUser|#####   |"+MainController.LINE_SEPARATOR+"" +
                "+--+----------+--------+", table.getTableString());
    }

    private TableConstructor initTableConstructor() {

        Set<String> columns = new LinkedHashSet<>(Arrays.asList("id", "name", "password"));

        Map<String, Object> firstEntry = new LinkedHashMap<>();
        firstEntry.put("id", 1);
        firstEntry.put("name", "FirstUser");
        firstEntry.put("password", "+++++");


        Map<String, Object> secondEntry = new LinkedHashMap<>();
        secondEntry.put("id", 2);
        secondEntry.put("name", "SecondUser");
        secondEntry.put("password", "#####");

        List<Map<String, Object>> tableData = new LinkedList<>(Arrays.asList(firstEntry, secondEntry));

        return new TableConstructor(columns, tableData);
    }
}
