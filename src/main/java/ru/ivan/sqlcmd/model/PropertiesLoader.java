package ru.ivan.sqlcmd.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {

    private static final String CONFIG_SQLCMD_PROPERTIES = "config/sqlcmd.properties";
    private Properties properties;

    public PropertiesLoader() {
        properties = new Properties();
        try ( InputStream in= PropertiesLoader.class.getClassLoader().getResourceAsStream(CONFIG_SQLCMD_PROPERTIES)) {
            properties.load(in);
        } catch (Exception e) {
            System.out.println("Error loading config "+CONFIG_SQLCMD_PROPERTIES);
            e.printStackTrace();
        }
    }

    public String getServerName() {
        return properties.getProperty("database.server.name");
    }

    public String getDatabaseName() {
        return properties.getProperty("database.name");
    }

    public String getDatabasePort() {
        return properties.getProperty("database.port");
    }

    public String getDriver() {
        return properties.getProperty("database.jdbc.driver");
    }

    public String getUserName() {
        return properties.getProperty("database.user.name");
    }

    public String getPassword() {
        return properties.getProperty("database.user.password");
    }


}
