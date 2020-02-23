package com.alfredbase.javabean;

import java.io.Serializable;


public class ReportDayTax implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2458967580251925653L;
	private Integer id;
	private Integer daySalesId;
	private Integer restaurantId;
	private String restaurantName;
	private Integer revenueId;
	private String revenueName;
	private Long businessDate;
	private Integer taxId;
	private String taxName;
	private String taxPercentage;
	private Integer taxQty;
	private String taxAmount;

	/**
	 * 税收类型(0消费税，1服务税)
	 */
	private Integer taxType;

	public Integer getTaxType() {
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDaySalesId() {
		return daySalesId;
	}
	public void setDaySalesId(Integer daySalesId) {
		this.daySalesId = daySalesId;
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
	public Integer getTaxId() {
		return taxId;
	}
	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}
	public String getTaxName() {
		return taxName;
	}
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
	public String getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	public Integer getTaxQty() {
		return taxQty;
	}
	public void setTaxQty(Integer taxQty) {
		this.taxQty = taxQty;
	}
	public String getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	@Override
	public String toString() {
		return "ReportDayTax{" +
				"id=" + id +
				", daySalesId=" + daySalesId +
				", restaurantId=" + restaurantId +
				", restaurantName='" + restaurantName + '\'' +
				", revenueId=" + revenueId +
				", revenueName='" + revenueName + '\'' +
				", businessDate=" + businessDate +
				", taxId=" + taxId +
				", taxName='" + taxName + '\'' +
				", taxPercentage='" + taxPercentage + '\'' +
				", taxQty=" + taxQty +
				", taxAmount='" + taxAmount + '\'' +
				", taxType=" + taxType +
				'}';
	}
}
