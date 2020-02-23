package com.alfredbase.javabean.temporaryforapp;

import java.io.Serializable;

/**
 * Created by Alex on 17/1/11.
 */

public class ReportUserOpenDrawer implements Serializable {
    private String userName;
    private int times;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "ReportUserOpenDrawer{" +
                "userName='" + userName + '\'' +
                ", times=" + times +
                '}';
    }
}
