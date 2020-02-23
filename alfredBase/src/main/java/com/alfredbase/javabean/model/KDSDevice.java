package com.alfredbase.javabean.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KDSDevice implements Serializable {

    private static final long serialVersionUID = -5249415685356838590L;

    int device_id;
    String name;
    String mac;
    String IP;
    int kdsType;

    /**
     * 0 online, -1 offline
     */
    int kdsStatus;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKdsType() {
        return kdsType;
    }

    public void setKdsType(int kdsType) {
        this.kdsType = kdsType;
    }

    public int getKdsStatus() {
        return kdsStatus;
    }

    public void setKdsStatus(int kdsStatus) {
        this.kdsStatus = kdsStatus;
    }

    @Override
    public String toString() {
        return "KDSDevice{" +
                "device_id=" + device_id +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", IP='" + IP + '\'' +
                ", kdsType=" + kdsType +
                ", kdsStatus=" + kdsStatus +
                '}';
    }

    public KDSDevice clone() {
        KDSDevice newone = new KDSDevice();
        newone.setDevice_id(this.device_id);
        newone.setName(this.name);
        newone.setMac(this.mac);
        newone.setIP(this.IP);
        newone.setKdsType(this.kdsType);
        newone.setKdsStatus(this.kdsStatus);
        return newone;

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("device_id", this.device_id);
        map.put("mac", this.mac);
        map.put("ip", this.IP);
        map.put("name", this.name);
        map.put("kdsType", this.kdsType);
        map.put("kdsStatus", this.kdsStatus);
        return map;
    }
}
