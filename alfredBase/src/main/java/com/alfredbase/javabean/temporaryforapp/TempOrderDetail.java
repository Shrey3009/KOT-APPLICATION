package com.alfredbase.javabean.temporaryforapp;


public class TempOrderDetail {
	private Integer id;
	private Integer orderDetailId;
	private Integer appOrderId;
	private Integer itemId;
	private String specialInstractions;
	private int itemCount;
	private String itemPrice;
	private String itemName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public Integer getAppOrderId() {
		return appOrderId;
	}
	public void setAppOrderId(Integer appOrderId) {
		this.appOrderId = appOrderId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getSpecialInstractions() {
		return specialInstractions;
	}
	public void setSpecialInstractions(String specialInstractions) {
		this.specialInstractions = specialInstractions;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@Override
	public String toString() {
		return "TempOrderDetail [id=" + id + ", orderDetailId=" + orderDetailId
				+ ", appOrderId=" + appOrderId + ", itemId=" + itemId
				+ ", specialInstractions=" + specialInstractions
				+ ", itemCount=" + itemCount + ", itemPrice=" + itemPrice
				+ ", itemName=" + itemName + "]";
	}
}
