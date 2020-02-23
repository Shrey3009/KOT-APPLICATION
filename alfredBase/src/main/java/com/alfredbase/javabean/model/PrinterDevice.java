package com.alfredbase.javabean.model;

import java.io.Serializable;

public class PrinterDevice   implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2956900804629957461L;
	int device_id;
	String name; //link to Printer Name set at backend
	String model;//printer model
	String mac;
	String IP;
	String type; //1 网络  2 蓝牙
	Integer isCahierPrinter=0;



	/* 0票据 1标签*/
	private Integer isLablePrinter;

	private int groupId;
	private String printerName;

	public Integer getIsLablePrinter() {
		return isLablePrinter;
	}

	public void setIsLablePrinter(Integer isLablePrinter) {
		this.isLablePrinter = isLablePrinter;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getIsCahierPrinter() {
		return isCahierPrinter;
	}
	public void setIsCahierPrinter(int isCahierPrinter) {
		this.isCahierPrinter = isCahierPrinter;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public void setIsCahierPrinter(Integer isCahierPrinter) {
		this.isCahierPrinter = isCahierPrinter;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PrinterDevice{" +
				"device_id=" + device_id +
				", name='" + name + '\'' +
				", model='" + model + '\'' +
				", mac='" + mac + '\'' +
				", IP='" + IP + '\'' +
				", type='" + type + '\'' +
				", isCahierPrinter=" + isCahierPrinter +
				", isLablePrinter=" + isLablePrinter +
				", groupId=" + groupId +
				", printerName='" + printerName + '\'' +
				'}';
	}
}
