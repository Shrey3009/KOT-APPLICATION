package com.alfredbase.javabean.model;

import java.io.Serializable;

public class MainPosInfo implements Serializable {

	private static final long serialVersionUID = 1809945969545930577L;

	private Integer restId; // 餐厅id
	private Integer revenueId; // 收银id
	private String name;		//名称
	private String IP; // pos的IP地址
	private String mac; // mac地址
	private int isKiosk; 

	public MainPosInfo() {

	}
	public MainPosInfo(Integer restId, Integer revenueId, String name,
			String iP, String mac, int isKiosk) {
		super();
		this.restId = restId;
		this.revenueId = revenueId;
		this.name = name;
		IP = iP;
		this.mac = mac;
		this.isKiosk = isKiosk;
	}

	/**
	 * @return the restId
	 */
	public Integer getRestId() {
		return restId;
	}

	/**
	 * @param restId the restId to set
	 */
	public void setRestId(Integer restId) {
		this.restId = restId;
	}

	/**
	 * @return the revenueId
	 */
	public Integer getRevenueId() {
		return revenueId;
	}

	/**
	 * @param revenueId the revenueId to set
	 */
	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the iP
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * @param iP the iP to set
	 */
	public void setIP(String iP) {
		IP = iP;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public int getIsKiosk() {
		return isKiosk;
	}
	public void setIsKiosk(int isKiosk) {
		this.isKiosk = isKiosk;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MainPosInfo [restId=" + restId + ", revenueId=" + revenueId
				+ ", name=" + name + ", IP=" + IP + ", mac=" + mac
				+ ", isKiosk=" + isKiosk + "]";
	}

}
