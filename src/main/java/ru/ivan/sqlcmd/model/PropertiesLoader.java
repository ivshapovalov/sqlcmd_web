package ru.ivan.sqlcmd.model;

import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {

    private static final String CONFIG_SQLCMD_PROPERTIES = "sqlcmd.properties";

    private Properties appProperties;

    public PropertiesLoader() {
        appProperties = new Properties();
        try (InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(CONFIG_SQLCMD_PROPERTIES)) {
            appProperties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getServerName() {
        return appProperties.getProperty("database.server.name");
    }

    public String getDatabasePort() {
        return appProperties.getProperty("database.port");
    }

    public String getDatabaseName() {
        return appProperties.getProperty("database.name");
    }

    public String getDriver() {
        return appProperties.getProperty("database.jdbc.driver");
    }

    public String getUserName() {
        return appProperties.getProperty("database.user.name");
    }

    public String getPassword() {
        return appProperties.getProperty("database.user.password");
    }



}
