package ru.ivan.sqlcmd.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_actions", schema = "public")
public class UserAction {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "action")
    private String action;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public UserAction() {
        // do nothing
    }

    public UserAction(String userName, String dbName, String action) {
        this.userName = userName;
        this.dbName = dbName;
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(int id) {
        this.id = id;
    }
}
