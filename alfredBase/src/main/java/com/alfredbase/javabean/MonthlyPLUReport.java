package com.alfredbase.javabean;

import java.io.Serializable;

public class MonthlyPLUReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8587718862627455493L;
	
    private Long businessDate;
    private int itemId;
    private String itemName;
    private double itemPrice;
    private int sumItemNum;
    private double sumRealPrice;
    private int itemMainCategoryId;
    private String mainCategoryName;
    private int itemCategoryId;
    private String itemCategoryName;
    private int isModifier;
    private int modifierItemId;
    
	public Long getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public int getSumItemNum() {
		return sumItemNum;
	}
	public void setSumItemNum(int sumItemNum) {
		this.sumItemNum = sumItemNum;
	}
	public double getSumRealPrice() {
		return sumRealPrice;
	}
	public void setSumRealPrice(double sumRealPrice) {
		this.sumRealPrice = sumRealPrice;
	}
	public int getItemMainCategoryId() {
		return itemMainCategoryId;
	}
	public void setItemMainCategoryId(int itemMainCategoryId) {
		this.itemMainCategoryId = itemMainCategoryId;
	}
	public String getMainCategoryName() {
		return mainCategoryName;
	}
	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName;
	}
	public int getItemCategoryId() {
		return itemCategoryId;
	}
	public void setItemCategoryId(int itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}
	public String getItemCategoryName() {
		return itemCategoryName;
	}
	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}
	public int getIsModifier() {
		return isModifier;
	}
	public void setIsModifier(int isModifier) {
		this.isModifier = isModifier;
	}
	public int getModifierItemId() {
		return modifierItemId;
	}
	public void setModifierItemId(int modifierItemId) {
		this.modifierItemId = modifierItemId;
	}
	
	@Override
	public String toString() {
		return "MonthlyPLUReport [businessDate=" + businessDate + ", itemId="
				+ itemId + ", itemName=" + itemName + ", itemPrice="
				+ itemPrice + ", sumItemNum=" + sumItemNum + ", sumRealPrice="
				+ sumRealPrice + ", itemMainCategoryId=" + itemMainCategoryId
				+ ", mainCategoryName=" + mainCategoryName
				+ ", itemCategoryId=" + itemCategoryId + ", itemCategoryName="
				+ itemCategoryName + ", isModifier=" + isModifier
				+ ", modifierItemId=" + modifierItemId + "]";
	}
    
    
}
