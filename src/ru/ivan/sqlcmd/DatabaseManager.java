package ru.ivan.sqlcmd;

/**
 * Created by Ivan on 19.09.2016.
 */
public interface DatabaseManager {
     void connect(String database, String user, String password);
    void clear(String tableName);

}
