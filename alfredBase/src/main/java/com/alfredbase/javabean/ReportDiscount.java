package com.alfredbase.javabean;

import java.io.Serializable;

public class ReportDiscount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8056426274901459537L;
	
	private Integer id;
	
	private Integer restaurantId;
	
	private Integer revenueId;
	
	private Integer userId;
	
	private	Integer orderId;
	
	private String revenueName;	//收银机名称
	
	private Long businessDate;	//餐厅营业时间
	
	private Integer billNumber;	
	
	private Integer tableId;
	
	private String tableName;	//桌子名称
	
	private String actuallAmount;	//打折前
	
	private String discount;	//折后价格
	
	private String grandTotal;	//实收金额

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Integer getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(Integer billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getActuallAmount() {
		return actuallAmount;
	}

	public void setActuallAmount(String actuallAmount) {
		this.actuallAmount = actuallAmount;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}

	@Override
	public String toString() {
		return "ReportDiscount [id=" + id + ", restaurantId=" + restaurantId
				+ ", revenueId=" + revenueId + ", userId=" + userId
				+ ", orderId=" + orderId + ", revenueName=" + revenueName
				+ ", businessDate=" + businessDate + ", billNumber="
				+ billNumber + ", tableId=" + tableId + ", tableName="
				+ tableName + ", actuallAmount=" + actuallAmount
				+ ", discount=" + discount + ", grandTotal=" + grandTotal + "]";
	}

}
