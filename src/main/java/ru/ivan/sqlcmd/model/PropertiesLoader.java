package ru.ivan.sqlcmd.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class PropertiesLoader {

    private static final String CONFIG_SQLCMD_PROPERTIES = "src/config/sqlcmd.properties";
    private final Properties properties;

    public PropertiesLoader() {
        properties = new Properties();
        File file = new File(CONFIG_SQLCMD_PROPERTIES);

        try (FileInputStream fileInput = new FileInputStream(file)) {
            properties.load(fileInput);
        } catch (Exception e) {
            System.out.println("Error loading config " + file.getAbsolutePath());
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
