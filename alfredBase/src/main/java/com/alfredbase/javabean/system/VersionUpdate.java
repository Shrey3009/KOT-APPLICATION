package com.alfredbase.javabean.system;

/**
 * Created by Alex on 17/1/22.
 */

public class VersionUpdate {
    private int versionCode;
    private String versionName;
    private String posDownload;
    private String waiterDownload;
    private String kdsDownload;
    private int forceUpdate;
    private String description;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPosDownload() {
        return posDownload;
    }

    public void setPosDownload(String posDownload) {
        this.posDownload = posDownload;
    }

    public String getWaiterDownload() {
        return waiterDownload;
    }

    public void setWaiterDownload(String waiterDownload) {
        this.waiterDownload = waiterDownload;
    }

    public String getKdsDownload() {
        return kdsDownload;
    }

    public void setKdsDownload(String kdsDownload) {
        this.kdsDownload = kdsDownload;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "VersionUpdate{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", posDownload='" + posDownload + '\'' +
                ", waiterDownload='" + waiterDownload + '\'' +
                ", kdsDownload='" + kdsDownload + '\'' +
                ", forceUpdate=" + forceUpdate +
                ", description='" + description + '\'' +
                '}';
    }
}
