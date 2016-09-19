package ru.ivan.sqlcmd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19.09.2016.
 */
public class DataSet {

    static class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    List<Data> data = new ArrayList<>();

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            values.add(data.get(i).getValue());
        }
        return values;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            names.add(data.get(i).getName());
        }
        return names;
    }

    public void put(String columnName, Object object) {
        data.add(new Data(columnName, object));
    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "names: " +getNames().toString()+"\n"+
                "values: "+getValues().toString()+"\n"+
                "}";
    }
}
