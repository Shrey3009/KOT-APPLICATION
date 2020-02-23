package com.alfredbase.javabean.model;

import java.io.Serializable;

/**
 * Created by Alex on 2017/2/16.
 */

public class ReportSessionSales implements Serializable{
    private int id;
    private String sessionName;
    private String startDrawer;
    private String cash;
    private String cashTopup;
    private String expectedAmount;
    private String actualAmount;
    private String difference;
    private long businessDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getStartDrawer() {
        return startDrawer;
    }

    public void setStartDrawer(String startDrawer) {
        this.startDrawer = startDrawer;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCashTopup() {
        return cashTopup;
    }

    public void setCashTopup(String cashTopup) {
        this.cashTopup = cashTopup;
    }

    public String getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(String expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public long getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(long businessDate) {
        this.businessDate = businessDate;
    }

    @Override
    public String toString() {
        return "ReportSessionSales{" +
                "id=" + id +
                ", sessionName='" + sessionName + '\'' +
                ", startDrawer='" + startDrawer + '\'' +
                ", cash='" + cash + '\'' +
                ", cashTopup='" + cashTopup + '\'' +
                ", expectedAmount='" + expectedAmount + '\'' +
                ", actualAmount='" + actualAmount + '\'' +
                ", difference='" + difference + '\'' +
                ", businessDate=" + businessDate +
                '}';
    }
}
