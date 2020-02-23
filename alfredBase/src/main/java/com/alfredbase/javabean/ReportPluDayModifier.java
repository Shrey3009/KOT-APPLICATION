package com.alfredbase.javabean;

import java.io.Serializable;

public class ReportPluDayModifier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1816338910320359675L;
	private Integer id;
	private Integer reportNo;
	private Integer restaurantId;
	private String restaurantName;
	private Integer revenueId;
	private String revenueName;
	private Long businessDate;
	private Integer modifierCategoryId;
	private String modifierCategoryName;
	private Integer modifierId;
	private String modifierName;
	/**
	 * 配料总金额和数量
	 */
	private String modifierPrice;
	private Integer modifierCount;
	/**
	 * voidbill的配料金额和数量
	 */
	private String billVoidPrice;
	private Integer billVoidCount;
	/**
	 * voiditem的配料金额和数量
	 */
	private String voidModifierPrice;
	private Integer voidModifierCount;
	/**
	 * boh的配料金额和数量
	 */
	private String bohModifierPrice;
	private Integer bohModifierCount;
	/**
	 * 单个菜foc的配料金额和数量
	 */
	private String focModifierPrice;
	private Integer focModifierCount;
	
	/**
	 * 整单 foc的配料金额和数量
	 */
	
	private String billFocPrice;
	private Integer billFocCount;
	
	private int comboItemId = 0;
	/**
	 *  Modifier的单价
	 */
	private String modifierItemPrice;

	private String realPrice;
	private Integer realCount;
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
	public Integer getModifierCategoryId() {
		return modifierCategoryId;
	}
	public void setModifierCategoryId(Integer modifierCategoryId) {
		this.modifierCategoryId = modifierCategoryId;
	}
	public String getModifierCategoryName() {
		return modifierCategoryName;
	}
	public void setModifierCategoryName(String modifierCategoryName) {
		this.modifierCategoryName = modifierCategoryName;
	}
	public Integer getModifierId() {
		return modifierId;
	}
	public void setModifierId(Integer modifierId) {
		this.modifierId = modifierId;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public String getModifierPrice() {
		return modifierPrice;
	}
	public void setModifierPrice(String modifierPrice) {
		this.modifierPrice = modifierPrice;
	}
	public Integer getModifierCount() {
		return modifierCount;
	}
	public void setModifierCount(Integer modifierCount) {
		this.modifierCount = modifierCount;
	}
	public String getBillVoidPrice() {
		return billVoidPrice;
	}
	public void setBillVoidPrice(String billVoidPrice) {
		this.billVoidPrice = billVoidPrice;
	}
	public Integer getBillVoidCount() {
		return billVoidCount;
	}
	public void setBillVoidCount(Integer billVoidCount) {
		this.billVoidCount = billVoidCount;
	}
	public String getVoidModifierPrice() {
		return voidModifierPrice;
	}
	public void setVoidModifierPrice(String voidModifierPrice) {
		this.voidModifierPrice = voidModifierPrice;
	}
	public Integer getVoidModifierCount() {
		return voidModifierCount;
	}
	public void setVoidModifierCount(Integer voidModifierCount) {
		this.voidModifierCount = voidModifierCount;
	}
	public String getBohModifierPrice() {
		return bohModifierPrice;
	}
	public void setBohModifierPrice(String bohModifierPrice) {
		this.bohModifierPrice = bohModifierPrice;
	}
	public Integer getBohModifierCount() {
		return bohModifierCount;
	}
	public void setBohModifierCount(Integer bohModifierCount) {
		this.bohModifierCount = bohModifierCount;
	}
	public String getFocModifierPrice() {
		return focModifierPrice;
	}
	public void setFocModifierPrice(String focModifierPrice) {
		this.focModifierPrice = focModifierPrice;
	}
	public Integer getFocModifierCount() {
		return focModifierCount;
	}
	public void setFocModifierCount(Integer focModifierCount) {
		this.focModifierCount = focModifierCount;
	}
	public String getBillFocPrice() {
		return billFocPrice;
	}
	public void setBillFocPrice(String billFocPrice) {
		this.billFocPrice = billFocPrice;
	}
	public Integer getBillFocCount() {
		return billFocCount;
	}
	public void setBillFocCount(Integer billFocCount) {
		this.billFocCount = billFocCount;
	}
	
	public int getComboItemId() {
		return comboItemId;
	}
	public void setComboItemId(int comboItemId) {
		this.comboItemId = comboItemId;
	}
	
	public String getModifierItemPrice() {
		return modifierItemPrice;
	}
	public void setModifierItemPrice(String modifierItemPrice) {
		this.modifierItemPrice = modifierItemPrice;
	}

	public String getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getRealCount() {
		return realCount;
	}

	public void setRealCount(Integer realCount) {
		this.realCount = realCount;
	}

	public int getDaySalesId() {
		return daySalesId;
	}

	public void setDaySalesId(int daySalesId) {
		this.daySalesId = daySalesId;
	}

	@Override
	public String toString() {
		return "ReportPluDayModifier{" +
				"id=" + id +
				", reportNo=" + reportNo +
				", restaurantId=" + restaurantId +
				", restaurantName='" + restaurantName + '\'' +
				", revenueId=" + revenueId +
				", revenueName='" + revenueName + '\'' +
				", businessDate=" + businessDate +
				", modifierCategoryId=" + modifierCategoryId +
				", modifierCategoryName='" + modifierCategoryName + '\'' +
				", modifierId=" + modifierId +
				", modifierName='" + modifierName + '\'' +
				", modifierPrice='" + modifierPrice + '\'' +
				", modifierCount=" + modifierCount +
				", billVoidPrice='" + billVoidPrice + '\'' +
				", billVoidCount=" + billVoidCount +
				", voidModifierPrice='" + voidModifierPrice + '\'' +
				", voidModifierCount=" + voidModifierCount +
				", bohModifierPrice='" + bohModifierPrice + '\'' +
				", bohModifierCount=" + bohModifierCount +
				", focModifierPrice='" + focModifierPrice + '\'' +
				", focModifierCount=" + focModifierCount +
				", billFocPrice='" + billFocPrice + '\'' +
				", billFocCount=" + billFocCount +
				", comboItemId=" + comboItemId +
				", modifierItemPrice='" + modifierItemPrice + '\'' +
				", realPrice='" + realPrice + '\'' +
				", realCount=" + realCount +
				", daySalesId=" + daySalesId +
				'}';
	}

}
