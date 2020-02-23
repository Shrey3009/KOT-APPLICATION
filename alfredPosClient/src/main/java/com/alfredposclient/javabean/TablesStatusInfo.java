package com.alfredposclient.javabean;

public class TablesStatusInfo {
	private int id;
	private int idleNum;
	private int diningNum;
	private int inCheckoutNum;
	public TablesStatusInfo(int id, int idleNum, int diningNum,int inCheckoutNum){
		this.id = id;
		this.idleNum = idleNum;
		this.diningNum = diningNum;
		this.inCheckoutNum = inCheckoutNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdleNum() {
		return idleNum;
	}
	public void setIdleNum(int idleNum) {
		this.idleNum = idleNum;
	}
	public int getDiningNum() {
		return diningNum;
	}
	public void setDiningNum(int diningNum) {
		this.diningNum = diningNum;
	}
	public int getInCheckoutNum() {
		return inCheckoutNum;
	}
	public void setInCheckoutNum(int inCheckoutNum) {
		this.inCheckoutNum = inCheckoutNum;
	}
	@Override
	public String toString() {
		return "TablesStatusInfo [id=" + id + ", idleNum=" + idleNum
				+ ", diningNum=" + diningNum + ", inCheckoutNum="
				+ inCheckoutNum + "]";
	}
	
}
