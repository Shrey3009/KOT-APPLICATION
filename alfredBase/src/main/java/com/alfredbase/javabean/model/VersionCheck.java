package com.alfredbase.javabean.model;

public class VersionCheck {
	private String appVersion;
	private int status;
	private String printerUrl;
	private String posUrl;
	private boolean force;
	private boolean updateData;
	private int down;
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPrinterUrl() {
		return printerUrl;
	}
	public void setPrinterUrl(String printerUrl) {
		this.printerUrl = printerUrl;
	}
	public String getPosUrl() {
		return posUrl;
	}
	public void setPosUrl(String posUrl) {
		this.posUrl = posUrl;
	}
	public boolean isForce() {
		return force;
	}
	public void setForce(boolean force) {
		this.force = force;
	}

	public boolean isUpdateData() {
		return updateData;
	}
	public void setUpdateData(boolean updateData) {
		this.updateData = updateData;
	}

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	@Override
	public String toString() {
		return "VersionCheck [appVersion=" + appVersion + ", status=" + status
				+ ", printerUrl=" + printerUrl + ", posUrl=" + posUrl
				+ ", force=" + force + ", updateData=" + updateData + "]";
	}
}
