package com.alfredbase.javabean;

import java.io.Serializable;

public class ReportHourly implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2676222687023136761L;
	private Integer id;
	private Integer restaurantId;
	private Integer revenueId;
	private String revenueName;
	private Long businessDate;
	private int hour;
	private Integer amountQty;
	private String amountPrice;
	private int daySalesId;
//	//Nett sales after discount & SVC, before GST
//	private String Gto;
//	private String gst;
//	private String Discount;
//	private String svc;
//	private Integer pax;
//	/**
//	 * 一下不包含gst
//	 * Exclude GST only
//	 */
//	private String cash;
//	private String nets;
//	private String visa;
//	private String masterCard;
//	private String amex;
//	private String voucher;
//	private String others;
//	private String
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
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public Integer getAmountQty() {
		return amountQty;
	}
	public void setAmountQty(Integer amountQty) {
		this.amountQty = amountQty;
	}
	public String getAmountPrice() {
		return amountPrice;
	}
	public void setAmountPrice(String amountPrice) {
		this.amountPrice = amountPrice;
	}

	public int getDaySalesId() {
		return daySalesId;
	}

	public void setDaySalesId(int daySalesId) {
		this.daySalesId = daySalesId;
	}

	@Override
	public String toString() {
		return "ReportHourly{" +
				"id=" + id +
				", restaurantId=" + restaurantId +
				", revenueId=" + revenueId +
				", revenueName='" + revenueName + '\'' +
				", businessDate=" + businessDate +
				", hour=" + hour +
				", amountQty=" + amountQty +
				", amountPrice='" + amountPrice + '\'' +
				", daySalesId=" + daySalesId +
				'}';
	}
}
