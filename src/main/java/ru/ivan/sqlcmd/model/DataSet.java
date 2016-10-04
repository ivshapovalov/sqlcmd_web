package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DataSet {
    Object get(String name);

    void updateFrom(DataSet newValue);

    List<Object> getValues();

    Set<String> getNames();

    void put(String name, Object value);
}
