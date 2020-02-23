package com.alfredbase.javabean;

import java.io.Serializable;

public class ReportDayPromotion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2676222687023136761L;
	private Integer id;
	private Integer restaurantId;
	private Integer revenueId;
	private String revenueName;
	private Long businessDate;

	private Integer promotionQty;
	private String promotionAmount;
	private String promotionName;
	private Integer promotionId;
	private int daySalesId;
	private long createTime;
	private long updateTime;
	private long sysCreateTime;
	private long sysUpdateTime;

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

	public Integer getPromotionQty() {
		return promotionQty;
	}

	public void setPromotionQty(Integer promotionQty) {
		this.promotionQty = promotionQty;
	}

	public String getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(String promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public int getDaySalesId() {
		return daySalesId;
	}

	public void setDaySalesId(int daySalesId) {
		this.daySalesId = daySalesId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getSysCreateTime() {
		return sysCreateTime;
	}

	public void setSysCreateTime(long sysCreateTime) {
		this.sysCreateTime = sysCreateTime;
	}

	public long getSysUpdateTime() {
		return sysUpdateTime;
	}

	public void setSysUpdateTime(long sysUpdateTime) {
		this.sysUpdateTime = sysUpdateTime;
	}

	@Override
	public String toString() {
		return "ReportDayPromotion{" +
				"id=" + id +
				", restaurantId=" + restaurantId +
				", revenueId=" + revenueId +
				", revenueName='" + revenueName + '\'' +
				", businessDate=" + businessDate +
				", promotionQty=" + promotionQty +
				", promotionAmount='" + promotionAmount + '\'' +
				", promotionName='" + promotionName + '\'' +
				", promotionId=" + promotionId +
				", daySalesId=" + daySalesId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", sysCreateTime=" + sysCreateTime +
				", sysUpdateTime=" + sysUpdateTime +
				'}';
	}
}
