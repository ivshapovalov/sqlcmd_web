package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Ivan on 20.09.2016.
 */
public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    int getSize(String tableName);

    Set<String> getTableNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
