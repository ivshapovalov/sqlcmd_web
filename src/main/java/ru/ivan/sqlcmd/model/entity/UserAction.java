package ru.ivan.sqlcmd.model.entity;


import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
@Table(name = "user_actions", schema = "public")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "database_connection_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DatabaseConnection connection;

    @Column(name = "action")
    private String action;
    @Column(name = "date_time")
    private String dateTime;

    public UserAction() {
        // do nothing
    }

    public UserAction(DatabaseConnection connection, String action) {
        this.connection = connection;
        this.action = action;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateTime= sdf.format(calendar.getTime());
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public DatabaseConnection getConnection() {
        return connection;
    }

    public void setConnection(DatabaseConnection connection) {
        this.connection = connection;
    }
}
