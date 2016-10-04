package ru.ivan.sqlcmd.model;

import java.util.*;

public class DataSetImpl implements DataSet {
    private Map<String,Object> data = new LinkedHashMap<>();

    @Override
    public Object get(String name) {
         return data.get(name);
    }

    @Override
    public void updateFrom(DataSet newValue) {
        Set<String> columns=newValue.getNames();
        for (String column:columns
             ) {
            Object data=newValue.get(column);
            this.put(column,data);
        }
    }

    @Override
    public List<Object> getValues() {
       return new LinkedList<>(data.values());
    }

    @Override
    public Set<String> getNames() {
        return data.keySet();
    }

    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "names: " + getNames().toString() +" , "+
                "values: " + getValues().toString()+
                "}";
    }
}
