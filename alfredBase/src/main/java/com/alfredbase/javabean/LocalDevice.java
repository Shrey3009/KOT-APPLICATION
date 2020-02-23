package com.alfredbase.javabean;

public class LocalDevice {
	private Integer id;
	private Integer deviceId;  // Printer ID: KDS and physical printer. Waiter ID: Waiter
	private String deviceName; // KDS or Printer name
	private String userName;   // User who is using the device
	private String deviceMode; // Printer hardware model
	/**
	 * Device Type:
	 */
	private Integer deviceType;
	private String ip;
	private String macAddress;
	private Integer connected; // 0 false; 1 true
    
	private Integer cashierPrinter;//0:false; 1 true;

	private String  printerName;
	/* 0票据 1标签*/
	private Integer isLablePrinter;
	public Integer getIsLablePrinter() {
		return isLablePrinter;
	}

	public void setIsLablePrinter(Integer isLablePrinter) {
		this.isLablePrinter = isLablePrinter;
	}


	
	public LocalDevice(){
		this.deviceMode = "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Integer getConnected() {
		return connected;
	}

	public void setConnected(Integer connected) {
		this.connected = connected;
	}
	
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getCashierPrinter() {
		return cashierPrinter;
	}
	public void setCashierPrinter(Integer cashierPrinter) {
		this.cashierPrinter = cashierPrinter;
	}	

	public String getDeviceMode() {
		return deviceMode;
	}
	public void setDeviceMode(String deviceMode) {
		this.deviceMode = deviceMode;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	@Override
	public String toString() {
		return "LocalDevice{" +
				"id=" + id +
				", deviceId=" + deviceId +
				", deviceName='" + deviceName + '\'' +
				", userName='" + userName + '\'' +
				", deviceMode='" + deviceMode + '\'' +
				", deviceType=" + deviceType +
				", ip='" + ip + '\'' +
				", macAddress='" + macAddress + '\'' +
				", connected=" + connected +
				", cashierPrinter=" + cashierPrinter +
				", printerName='" + printerName + '\'' +
				'}';
	}
}
