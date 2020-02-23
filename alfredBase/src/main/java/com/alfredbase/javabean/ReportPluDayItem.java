package com.alfredbase.javabean;

import java.io.Serializable;

public class ReportPluDayItem implements Serializable, Comparable<ReportPluDayItem>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 84404665931456128L;

	private Integer id;

    private Integer reportNo;

    private Integer restaurantId;

    private String restaurantName;

    private Integer revenueId;

    private String revenueName;

    private Long businessDate;

    private Integer itemMainCategoryId;

    private String itemMainCategoryName;

    private Integer itemCategoryId;

    private String itemCategoryName;

    private Integer itemDetailId;

    private String itemName;

    private String itemPrice;

    private Integer itemCount;
    
    private String itemAmount;
    
    private Integer itemVoidQty;

    private String itemVoidPrice;

    private Integer itemHoldQty;

    private String itemHoldPrice;

    private Integer itemFocQty;
    
    private String itemFocPrice;
    
    private Integer billVoidQty;
    
    private String billVoidPrice;
    
    private Integer billFocQty;
    
    private String billFocPrice;

	private int isOpenItem;

	private int daySalesId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReportNo() {
		return reportNo;
	}

	public void setReportNo(Integer reportNo) {
		this.reportNo = reportNo;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public String getRevenueName() {
		return revenueName;
	}

	public void setRevenueName(String revenueName) {
		this.revenueName = revenueName;
	}

	public Long getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
	}

	public Integer getItemMainCategoryId() {
		return itemMainCategoryId;
	}

	public void setItemMainCategoryId(Integer itemMainCategoryId) {
		this.itemMainCategoryId = itemMainCategoryId;
	}

	public String getItemMainCategoryName() {
		return itemMainCategoryName;
	}

	public void setItemMainCategoryName(String itemMainCategoryName) {
		this.itemMainCategoryName = itemMainCategoryName;
	}

	public Integer getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(Integer itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getItemCategoryName() {
		return itemCategoryName;
	}

	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}

	public Integer getItemDetailId() {
		return itemDetailId;
	}

	public void setItemDetailId(Integer itemDetailId) {
		this.itemDetailId = itemDetailId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public String getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(String itemAmount) {
		this.itemAmount = itemAmount;
	}

	public Integer getItemVoidQty() {
		return itemVoidQty;
	}

	public void setItemVoidQty(Integer itemVoidQty) {
		this.itemVoidQty = itemVoidQty;
	}

	public String getItemVoidPrice() {
		return itemVoidPrice;
	}

	public void setItemVoidPrice(String itemVoidPrice) {
		this.itemVoidPrice = itemVoidPrice;
	}

	public Integer getItemHoldQty() {
		return itemHoldQty;
	}

	public void setItemHoldQty(Integer itemHoldQty) {
		this.itemHoldQty = itemHoldQty;
	}

	public String getItemHoldPrice() {
		return itemHoldPrice;
	}

	public void setItemHoldPrice(String itemHoldPrice) {
		this.itemHoldPrice = itemHoldPrice;
	}

	public Integer getItemFocQty() {
		return itemFocQty;
	}

	public void setItemFocQty(Integer itemFocQty) {
		this.itemFocQty = itemFocQty;
	}

	public String getItemFocPrice() {
		return itemFocPrice;
	}

	public void setItemFocPrice(String itemFocPrice) {
		this.itemFocPrice = itemFocPrice;
	}

	public Integer getBillVoidQty() {
		return billVoidQty;
	}

	public void setBillVoidQty(Integer billVoidQty) {
		this.billVoidQty = billVoidQty;
	}

	public String getBillVoidPrice() {
		return billVoidPrice;
	}

	public void setBillVoidPrice(String billVoidPrice) {
		this.billVoidPrice = billVoidPrice;
	}

	public Integer getBillFocQty() {
		return billFocQty;
	}

	public void setBillFocQty(Integer billFocQty) {
		this.billFocQty = billFocQty;
	}

	public String getBillFocPrice() {
		return billFocPrice;
	}

	public void setBillFocPrice(String billFocPrice) {
		this.billFocPrice = billFocPrice;
	}

	public int getIsOpenItem() {
		return isOpenItem;
	}

	public void setIsOpenItem(int isOpenItem) {
		this.isOpenItem = isOpenItem;
	}

	public int getDaySalesId() {
		return daySalesId;
	}

	public void setDaySalesId(int daySalesId) {
		this.daySalesId = daySalesId;
	}

	@Override
	public String toString() {
		return "ReportPluDayItem{" +
				"id=" + id +
				", reportNo=" + reportNo +
				", restaurantId=" + restaurantId +
				", restaurantName='" + restaurantName + '\'' +
				", revenueId=" + revenueId +
				", revenueName='" + revenueName + '\'' +
				", businessDate=" + businessDate +
				", itemMainCategoryId=" + itemMainCategoryId +
				", itemMainCategoryName='" + itemMainCategoryName + '\'' +
				", itemCategoryId=" + itemCategoryId +
				", itemCategoryName='" + itemCategoryName + '\'' +
				", itemDetailId=" + itemDetailId +
				", itemName='" + itemName + '\'' +
				", itemPrice='" + itemPrice + '\'' +
				", itemCount=" + itemCount +
				", itemAmount='" + itemAmount + '\'' +
				", itemVoidQty=" + itemVoidQty +
				", itemVoidPrice='" + itemVoidPrice + '\'' +
				", itemHoldQty=" + itemHoldQty +
				", itemHoldPrice='" + itemHoldPrice + '\'' +
				", itemFocQty=" + itemFocQty +
				", itemFocPrice='" + itemFocPrice + '\'' +
				", billVoidQty=" + billVoidQty +
				", billVoidPrice='" + billVoidPrice + '\'' +
				", billFocQty=" + billFocQty +
				", billFocPrice='" + billFocPrice + '\'' +
				", isOpenItem=" + isOpenItem +
				", daySalesId=" + daySalesId +
				'}';
	}


	@Override
	public int compareTo(ReportPluDayItem another) {
		int i = this.getItemMainCategoryId() - another.getItemMainCategoryId();//先按照主分类排序
		if(i == 0){
			int a = this.getItemCategoryId() - another.getItemCategoryId();//如果主分类相等了再用子分类进行排序
			if(a < 0){
				return -1;
			}else if (a > 0){
				return 1;
			}else {
				return 0;
			}
		}else if (i < 0){
			return -1;
		}else {
			return 1;
		}
	}
}
