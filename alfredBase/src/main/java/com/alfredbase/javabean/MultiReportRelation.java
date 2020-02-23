package com.alfredbase.javabean;

public class MultiReportRelation {
    private int id;
    private int mainReportId;
    private int subReportId;
    private int subPosBeanId;
    private long subReportCreateTime;
    private int syncStatus = 0; // 0未发送，1已发送

    public MultiReportRelation() {
    }

    public MultiReportRelation(int mainReportId, int subReportId, int subPosBeanId, long subReportCreateTime, int syncStatus) {
        this.mainReportId = mainReportId;
        this.subReportId = subReportId;
        this.subPosBeanId = subPosBeanId;
        this.subReportCreateTime = subReportCreateTime;
        this.syncStatus = syncStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMainReportId() {
        return mainReportId;
    }

    public void setMainReportId(int mainReportId) {
        this.mainReportId = mainReportId;
    }

    public int getSubReportId() {
        return subReportId;
    }

    public void setSubReportId(int subReportId) {
        this.subReportId = subReportId;
    }

    public int getSubPosBeanId() {
        return subPosBeanId;
    }

    public void setSubPosBeanId(int subPosBeanId) {
        this.subPosBeanId = subPosBeanId;
    }

    public long getSubReportCreateTime() {
        return subReportCreateTime;
    }

    public void setSubReportCreateTime(long subReportCreateTime) {
        this.subReportCreateTime = subReportCreateTime;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    @Override
    public String toString() {
        return "MultiReportRelation{" +
                "id=" + id +
                ", mainReportId=" + mainReportId +
                ", subReportId=" + subReportId +
                ", subPosBeanId=" + subPosBeanId +
                ", subReportCreateTime=" + subReportCreateTime +
                ", syncStatus=" + syncStatus +
                '}';
    }
}
