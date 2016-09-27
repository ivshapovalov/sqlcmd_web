package ru.ivan.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by ivan.shapovalov on 27.09.2016.
 */
public interface DataSet {
    Object get(String name);

    void updateFrom(DataSet newValue);

    List<Object> getValues();

    Set<String> getNames();

    void put(String name, Object value);
}
