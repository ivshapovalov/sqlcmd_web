package ru.ivan.sqlcmd.model;

import java.util.List;

/**
 * Created by Ivan on 20.09.2016.
 */
public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    List<String> getTablesNames();

    void connect(String database, String user, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet input);
}
