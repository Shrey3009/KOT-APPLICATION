package com.alfredbase.javabean.javabeanforhtml;

public class DashboardItemDetail {
	private int itemId;
	private String name;
	private int qty;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	@Override
	public String toString() {
		return "DashboardItemDetail [itemId=" + itemId + ", itemName="
				+ name + ", qty=" + qty + "]";
	}

}
