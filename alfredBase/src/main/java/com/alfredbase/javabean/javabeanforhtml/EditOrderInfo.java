package com.alfredbase.javabean.javabeanforhtml;

public class EditOrderInfo {
	
	private Integer orderNo;
	private Integer orderId;
	private Integer kotNo;
	private String  placeName;
	private String  tableName;
	private String  waiterName;
	private String  cashierName;
	
	public EditOrderInfo() {
		
	}
	public EditOrderInfo(Integer orderNo, Integer orderId, Integer kotNo,
			String placeName, String tableName, String waiterName) {

		this.orderNo = orderNo;
		this.orderId = orderId;
		this.kotNo = kotNo;
		this.placeName = placeName;
		this.tableName = tableName;
		this.waiterName = waiterName;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getKotNo() {
		return kotNo;
	}
	public void setKotNo(Integer kotNo) {
		this.kotNo = kotNo;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getWaiterName() {
		return waiterName;
	}
	public void setWaiterName(String waiterName) {
		this.waiterName = waiterName;
	}
	public String getCashierName() {
		return cashierName;
	}
	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}
	@Override
	public String toString() {
		return "EditOrderInfo [orderNo=" + orderNo + ", orderId=" + orderId
				+ ", kotNo=" + kotNo + ", placeName=" + placeName
				+ ", tableName=" + tableName + ", waiterName=" + waiterName
				+ ", cashierName=" + cashierName + "]";
	}
	
}
