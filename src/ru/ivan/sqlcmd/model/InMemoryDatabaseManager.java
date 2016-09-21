package ru.ivan.sqlcmd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 20.09.2016.
 */
public class InMemoryDatabaseManager implements DatabaseManager {
    private List<DataSet> data=new ArrayList<>();

    @Override
    public List<DataSet> getTableData(String tableName) {
        if (validate(tableName));
        List<DataSet> b = new ArrayList<>();
        b.addAll(data);
        return b;
    }

    private boolean validate(String tableName) {
        if (!"users".equals(tableName)) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    @Override
    public List<String> getTablesNames() {
        List<String> list=new ArrayList<>();
        list.add("users");
        return list;
    }

    @Override
    public void connect(String database, String user, String password) {

    }

    @Override
    public void clear(String tableName) {
        if (validate(tableName));
        data.clear();

    }

    @Override
    public void create(String tableName, DataSet input) {
        if (validate(tableName));
        data.add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        if (validate(tableName));
        for (DataSet dataSet:data
             ) {
            if(dataSet.get("id").equals(id)){
                dataSet.updateFrom(newValue);
            }
        }
    }

    @Override
    public List<String> getTableColumns(String tableName) {
        List<String> columns=new ArrayList<>();
        columns.add("id");
        columns.add("name");
        columns.add("password");
        return columns ;
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
