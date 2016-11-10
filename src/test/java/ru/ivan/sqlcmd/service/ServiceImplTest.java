package ru.ivan.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-application-context.xml")

public class ServiceImplTest {

    @Autowired
    private ConnectionService service;
    @Test
    public void test () {
        //given
        DatabaseManager manager=service.connect("database","user","password");
        manager.createTable("users999 (id int, name text)");
        Map<String,Object> row=new HashMap<>();
        row.put("id","1");
        row.put("name","name1");
        manager.insertRow("users999",row);
        row=new HashMap<>();
        row.put("id","2");
        row.put("name","name2");
        manager.insertRow("users999",row);

        //when
        List<List<String>> users=service.rows(manager,"users999");



        //then
        assertEquals("[[name, id], [name1, 1], [name2, 2]]",users.toString());
    }
}
