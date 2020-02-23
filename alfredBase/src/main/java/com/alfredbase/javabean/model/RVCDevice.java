package com.alfredbase.javabean.model;

import com.moonearly.model.UdpMsg;

public class RVCDevice {
    String name;
    String ip;
    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(int type) {
        this.type = "" + type;
    }

    public RVCDevice() {
    }

    public RVCDevice(String name, String ip, int type) {
        this.name = name;
        this.ip = ip;
        this.type = "" + type;
    }

    public RVCDevice(UdpMsg device) {
        this.name = device.getName();
        this.ip = device.getIp();
        this.type = "" + device.getType();
    }

    //
}
