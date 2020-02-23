package com.alfredposclient.javabean;

/**
 * Created by Alex on 16/11/15.
 */

public class SecondScreenTotal {
    private String name;
    private String value;

    public SecondScreenTotal(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SecondScreenTotal{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
