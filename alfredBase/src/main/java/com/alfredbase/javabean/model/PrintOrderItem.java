package com.alfredbase.javabean.model;

import java.io.Serializable;

public class PrintOrderItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4094175749697194688L;

	private String itemName;
	private String price;
	private String qty;
	private String amount;
	private int orderDetailId;
	private String weight;
	private Integer itemDetailId;
	private int isTakeAway;
	
	public PrintOrderItem(Integer orderDetailId, int isTakeAway, Integer itemDetailId, String itemName, String price, String qty, String amount, String weight){
		this.itemName = itemName;
		this.price =  price;
		this.qty = qty;
		this.amount = amount;
		this.orderDetailId = orderDetailId;
		this.weight = weight;
		this.itemDetailId = itemDetailId;
		this.isTakeAway = isTakeAway;
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
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Integer getItemDetailId() {
		return itemDetailId;
	}

	public void setItemDetailId(Integer itemDetailId) {
		this.itemDetailId = itemDetailId;
	}

	public int getIsTakeAway() {
		return isTakeAway;
	}

	public void setIsTakeAway(int isTakeAway) {
		this.isTakeAway = isTakeAway;
	}

	@Override
	public String toString() {
		return "PrintOrderItem{" +
				"itemName='" + itemName + '\'' +
				", price='" + price + '\'' +
				", qty='" + qty + '\'' +
				", amount='" + amount + '\'' +
				", orderDetailId=" + orderDetailId +
				", weight='" + weight + '\'' +
				", itemDetailId=" + itemDetailId +
				", isTakeAway=" + isTakeAway +
				'}';
	}

	public PrintOrderItem clone(){
		PrintOrderItem printOrderItem = new PrintOrderItem(orderDetailId, isTakeAway, itemDetailId, itemName, price, qty, amount, weight);
		return printOrderItem;

	}
}
