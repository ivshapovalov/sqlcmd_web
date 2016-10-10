package ru.ivan.sqlcmd.model;

import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {

    private static final String CONFIG_SQLCMD_PROPERTIES = "config/sqlcmd.properties";

    private Properties appProperties;

    public PropertiesLoader() {
        appProperties = new Properties();
        try (InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(CONFIG_SQLCMD_PROPERTIES)) {
            appProperties.load(in);
        } catch (Exception e) {
            System.out.println("Error loading config " + CONFIG_SQLCMD_PROPERTIES);
            e.printStackTrace();
        }
//        Properties logProperties = new Properties();
//        try ( InputStream in= PropertiesLoader.class.getClassLoader().getResourceAsStream(CONFIG_LOG4J_PROPERTIES)) {
//            logProperties.load(in);
//        } catch (Exception e) {
//            System.out.println("Error loading config "+CONFIG_LOG4J_PROPERTIES);
//            e.printStackTrace();
//        }

    }

    public String getServerName() {
        return appProperties.getProperty("database.server.name");
    }

    public String getDatabasePort() {
        return appProperties.getProperty("database.port");
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
