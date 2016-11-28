package ru.ivan.sqlcmd.controller;

public class Connection {

    private String databaseName;
    private String userName;
    private String password;

    public Connection() {
        // do nothing
    }

    public Connection(String databaseName, String userName,String password ) {
        this.databaseName = databaseName;
        this.password = password;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
