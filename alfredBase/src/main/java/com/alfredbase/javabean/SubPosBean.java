package com.alfredbase.javabean;

import java.io.Serializable;

public class SubPosBean implements Serializable {
    private Integer id;
    private String userName;
    private String deviceId;
    private String numTag;
    private int subPosStatus;
    private long sessionStatusTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getNumTag() {
        return numTag;
    }

    public void setNumTag(String numTag) {
        this.numTag = numTag;
    }

    public int getSubPosStatus() {
        return subPosStatus;
    }

    public void setSubPosStatus(int subPosStatus) {
        this.subPosStatus = subPosStatus;
    }

    public long getSessionStatusTime() {
        return sessionStatusTime;
    }

    public void setSessionStatusTime(long sessionStatusTime) {
        this.sessionStatusTime = sessionStatusTime;
    }

    @Override
    public String toString() {
        return "SubPosBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", numTag='" + numTag + '\'' +
                ", subPosStatus=" + subPosStatus +
                ", sessionStatusTime=" + sessionStatusTime +
                '}';
    }

}
