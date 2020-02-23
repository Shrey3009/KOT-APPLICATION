package com.alfredbase.javabean.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.alfredbase.javabean.ReportPluDayComboModifier;

public class ReportEntItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 548843770905017796L;
	private String itemName;
	private int itemQty;
	private String price;
	private String amount;	
	private List<ReportPluDayComboModifier> comboModifiers;
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemQty() {
		return itemQty;
	}
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<ReportPluDayComboModifier> getComboModifiers() {
		if(comboModifiers == null)
			comboModifiers = Collections.emptyList();
		return comboModifiers;
	}
	public void setComboModifiers(List<ReportPluDayComboModifier> comboModifiers) {
		this.comboModifiers = comboModifiers;
	}
	@Override
	public String toString() {
		return "ReportEntItem [itemName=" + itemName + ", itemQty=" + itemQty
				+ ", price=" + price + ", amount=" + amount
				+ ", comboModifiers=" + comboModifiers + "]";
	}
}
