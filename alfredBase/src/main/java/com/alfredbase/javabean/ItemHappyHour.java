package com.alfredbase.javabean;


public class ItemHappyHour {
	private Integer id;
	private Integer happyHourId;
	private Integer itemMainCategoryId;
	private Integer itemCategoryId;
	private Integer itemId;
	/**
	 * 关联类型(1关联主分类，2关联子分类，3关联菜单)
	 */
	private Integer type;

	private String discountPrice;
	private String discountRate;
	private Integer freeNum;

	private String itemMainCategoryName;
	private String itemCategoryName;
	private String itemName;

	private Integer freeItemId;
	private String freeItemName;

	public ItemHappyHour() {
	}

	public ItemHappyHour(Integer id, Integer happyHourId,
			Integer itemMainCategoryId, Integer itemCategoryId, Integer itemId,
			Integer type, String discountPrice, String discountRate,
			Integer freeNum, String itemMainCategoryName,
			String itemCategoryName, String itemName, Integer freeItemId,
			String freeItemName) {
		super();
		this.id = id;
		this.happyHourId = happyHourId;
		this.itemMainCategoryId = itemMainCategoryId;
		this.itemCategoryId = itemCategoryId;
		this.itemId = itemId;
		this.type = type;
		this.discountPrice = discountPrice;
		this.discountRate = discountRate;
		this.freeNum = freeNum;
		this.itemMainCategoryName = itemMainCategoryName;
		this.itemCategoryName = itemCategoryName;
		this.itemName = itemName;
		this.freeItemId = freeItemId;
		this.freeItemName = freeItemName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHappyHourId() {
		return happyHourId;
	}

	public void setHappyHourId(Integer happyHourId) {
		this.happyHourId = happyHourId;
	}

	public Integer getItemMainCategoryId() {
		return itemMainCategoryId;
	}

	public void setItemMainCategoryId(Integer itemMainCategoryId) {
		this.itemMainCategoryId = itemMainCategoryId;
	}

	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public Integer getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(Integer freeNum) {
		this.freeNum = freeNum;
	}

	public String getItemMainCategoryName() {
		return itemMainCategoryName;
	}

	public void setItemMainCategoryName(String itemMainCategoryName) {
		this.itemMainCategoryName = itemMainCategoryName;
	}

	public String getItemCategoryName() {
		return itemCategoryName;
	}

	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getFreeItemId() {
		return freeItemId;
	}

	public void setFreeItemId(Integer freeItemId) {
		this.freeItemId = freeItemId;
	}

	public String getFreeItemName() {
		return freeItemName;
	}

	public void setFreeItemName(String freeItemName) {
		this.freeItemName = freeItemName;
	}

	@Override
	public String toString() {
		return "ItemHappyHour [id=" + id + ", happyHourId=" + happyHourId
				+ ", itemMainCategoryId=" + itemMainCategoryId
				+ ", itemCategoryId=" + itemCategoryId + ", itemId=" + itemId
				+ ", type=" + type + ", discountPrice=" + discountPrice
				+ ", discountRate=" + discountRate + ", freeNum=" + freeNum
				+ ", itemMainCategoryName=" + itemMainCategoryName
				+ ", itemCategoryName=" + itemCategoryName + ", itemName="
				+ itemName + ", freeItemId=" + freeItemId + ", freeItemName="
				+ freeItemName + "]";
	}
}
