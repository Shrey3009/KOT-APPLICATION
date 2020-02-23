package com.alfredbase.javabean.model;

public class PrintBill {

	private int orderId;
	private String placeName;
	private String tableName;
	private int orderNo;
	private String total;
	private String userName;

	public PrintBill(int orderId, String placeName,
			String tableName, int orderNo, String total, String userName) {
		super();
		this.orderId = orderId;
		this.placeName = placeName;
		this.tableName = tableName;
		this.orderNo = orderNo;
		this.total = total;
		this.userName = userName;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
	public String toString() {
		return "PrintBill [orderId=" + orderId + ", placeName=" + placeName
				+ ", tableName=" + tableName + ", orderNo=" + orderNo
				+ ", total=" + total + ", userName=" + userName + "]";
	}
	
}
