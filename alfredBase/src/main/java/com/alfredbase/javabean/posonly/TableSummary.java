package com.alfredbase.javabean.posonly;

/**
 * Created by Alex on 2018/3/13.
 */

public class TableSummary {
    private int tableId;
    private String tableName;
    private String firstName;
    private String lastName;
    private String amount;
    private Long startTime;
    private String orderNo;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "TableSummary{" +
                "tableId=" + tableId +
                ", tableName='" + tableName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", amount='" + amount + '\'' +
                ", startTime=" + startTime +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
