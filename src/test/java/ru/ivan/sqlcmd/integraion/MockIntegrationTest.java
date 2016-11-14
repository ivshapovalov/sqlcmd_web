package ru.ivan.sqlcmd.integraion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import ru.ivan.sqlcmd.service.Service;
import ru.ivan.sqlcmd.model.DatabaseManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MockIntegrationTest {
    private WebDriver driver;
    private SpringMockerJettyRunner runner;
    private Service service;

    @Before
    public void startServer() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", "/sqlcmd");
        driver = new HtmlUnitDriver(true);
    }

    @After
    public void stop() throws Exception {
        runner.stop();
    }

    @Test
    public void test() throws Exception {
        // given
        runner.mockBean("service");
        runner.start();
        service = runner.getBean("service");

        when(service.getMainMenu()).thenReturn(Arrays.asList("listOfCommands","databases","tables","disconnect"));

        DatabaseManager manager = mock(DatabaseManager.class);
        when(service.connect(anyString(), anyString(), anyString()))
                .thenReturn(manager);

        List<String> table1=Arrays.asList("table1", "5");
        List<String> table2=Arrays.asList("table2", "10");
        when(service.tables(manager))
                .thenReturn(new LinkedList<>(Arrays.asList(table1,table2)));

        // when
        driver.get(runner.getUrl());

        driver.findElement(By.id("database")).sendKeys("sqlcmd");
        driver.findElement(By.id("username")).sendKeys("postgres");
        driver.findElement(By.id("password")).sendKeys("postgres");
        driver.findElement(By.id("connect")).click();

        // then
        assertLinksByTagName("[listOfCommands, databases, tables, disconnect]",
                "[/sqlcmd/listOfCommands, /sqlcmd/databases, /sqlcmd/tables, /sqlcmd/disconnect]");

        // when
        driver.get(getBase() + "/sqlcmd/tables");

        // then
        assertLinksByTagName("[table1, table2, menu]",
                "[/sqlcmd/rows?table=table1, " +
                "/sqlcmd/rows?table=table2, "+
                "/sqlcmd/menu]");
    }

    private void assertLinksByTagName(String expectedNames, String expectedUrls) {
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        List<String> names = new LinkedList<>();
        List<String> urls = new LinkedList<>();
        for (WebElement element : elements) {
            names.add(element.getText());
            urls.add(element.getAttribute("href"));
        }
        assertEquals(expectedNames, names.toString());
        String base = getBase();
        assertEquals(expectedUrls, urls.toString()
                .replaceAll(base, ""));
    }

    private String getBase() {
        return "http://localhost:" + runner.getPort();
    }


}