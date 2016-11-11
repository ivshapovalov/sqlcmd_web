package ru.ivan.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.ivan.sqlcmd.model.DatabaseManager;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class ServiceImplTest {

    @InjectMocks
    private ServiceImpl service=new ServiceImpl() {
        @Override
        public DatabaseManager getManager() {
            return manager;
        }
    };

    @Mock
    private DatabaseManager manager;

    @Test
    public void test() {
        //given
        when(manager.getTableColumns("users")).thenReturn(new LinkedHashSet<String>(Arrays.asList("id","name",
                "password")));

        Map<String, Object> row = new HashMap<>();
        row.put("id", "1");
        row.put("name", "name1");
        row.put("password", "+++");

        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", "2");
        row2.put("name", "name2");
        row2.put("password", "****");
        when(manager.getTableRows("users")).thenReturn(Arrays.asList(row,row2));

        //when

        List<List<String>> users = service.rows(manager, "users");

        //then
        assertEquals("[[id, name, password], " +
                "[1, name1, +++]," +
                " [2, name2, ****]]", users.toString());
    }
}
