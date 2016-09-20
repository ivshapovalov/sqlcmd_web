package ru.ivan.sqlcmd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 19.09.2016.
 */
public class DataSet {

    public Object get(String name) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(name)) {
                return data.get(i).getValue();
            }
        }
        return null;
    }

    public void updateFrom(DataSet newValue) {

        for (int i = 0; i < newValue.getNames().size(); i++) {
            Data data = newValue.data.get(i);

            this.put(data.name, data.value);
        }

    }


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

    public void put(String name, Object value) {
        boolean isFound = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(name)) {
                data.get(i).value = value;
                return;
            }
        }
        data.add(new Data(name, value));

    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "names: " + getNames().toString() + "\n" +
                "values: " + getValues().toString() + "\n" +
                "}";
    }
}
