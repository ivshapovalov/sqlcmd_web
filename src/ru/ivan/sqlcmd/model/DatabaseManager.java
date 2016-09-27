package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Ivan on 20.09.2016.
 */
public interface DatabaseManager {
    List<DataSet> getTableData(String tableName);

    Set<String> getTablesNames();

    void connect(String database, String user, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet input);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
