package ru.ivan.sqlcmd.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "database_connections", schema = "public")
public class DatabaseConnection {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "db_name")
    private String dbName;

    @JoinColumn(name = "database_connection_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<UserAction> userActions;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public DatabaseConnection() {
        // do nothing
    }

    public DatabaseConnection(String userName, String dbName) {
        this.userName = userName;
        this.dbName = dbName;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<UserAction> getUserActions() {
        return userActions;
    }

    public void setUserActions(List<UserAction> userActions) {
        this.userActions = userActions;
    }
}
