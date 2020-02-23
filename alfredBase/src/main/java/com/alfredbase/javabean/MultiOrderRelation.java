package com.alfredbase.javabean;

public class MultiOrderRelation {
    private int id;
    private int mainOrderId;
    private int subOrderId;
    private int subPosBeanId;
    private long subOrderCreateTime;

    public MultiOrderRelation() {
    }

    public MultiOrderRelation(int mainOrderId, int subOrderId, int subPosBeanId, long subOrderCreateTime) {
        this.mainOrderId = mainOrderId;
        this.subOrderId = subOrderId;
        this.subPosBeanId = subPosBeanId;
        this.subOrderCreateTime = subOrderCreateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(int mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public int getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(int subOrderId) {
        this.subOrderId = subOrderId;
    }

    public int getSubPosBeanId() {
        return subPosBeanId;
    }

    public void setSubPosBeanId(int subPosBeanId) {
        this.subPosBeanId = subPosBeanId;
    }

    public long getSubOrderCreateTime() {
        return subOrderCreateTime;
    }

    public void setSubOrderCreateTime(long subOrderCreateTime) {
        this.subOrderCreateTime = subOrderCreateTime;
    }

    @Override
    public String toString() {
        return "MultiOrderRelation{" +
                "id=" + id +
                ", mainOrderId=" + mainOrderId +
                ", subOrderId=" + subOrderId +
                ", subPosBeanId=" + subPosBeanId +
                ", subOrderCreateTime=" + subOrderCreateTime +
                '}';
    }
}
