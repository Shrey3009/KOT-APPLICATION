package com.alfredbase.javabean.model;

import java.io.Serializable;

public class PrintOrderModifier implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1692308086045515264L;
	private String itemName;
	private String price;
	private int qty;
	private String amount;
	private int orderDetailId;
	private int isBill;
	
	public PrintOrderModifier(Integer orderDetailId, String itemName, String price, int qty, String amount){
		this.itemName = itemName;
		this.price =  price;
		this.qty = qty;
		this.amount = amount;
		this.orderDetailId = orderDetailId;
	}

	public PrintOrderModifier(Integer orderDetailId, String itemName, String price, int qty, String amount,int isBill){
		this.itemName = itemName;
		this.price =  price;
		this.qty = qty;
		this.amount = amount;
		this.orderDetailId = orderDetailId;
		this.isBill=isBill;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "PrintOrderModifier [itemName=" + itemName + ", price=" + price
				+ ", qty=" + qty + ", amount=" + amount + "]";
	}

	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getIsBill() {
		return isBill;
	}

	public void setIsBill(int isBill) {
		this.isBill = isBill;
	}
}
