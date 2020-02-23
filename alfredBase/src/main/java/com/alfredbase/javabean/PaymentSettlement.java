package com.alfredbase.javabean;


public class PaymentSettlement {
	private Integer id;
	private Integer billNo;
	private Integer paymentId;
	/**
	 * 支付方式id
	 */
	private Integer paymentTypeId;
	/**
	 * 实际支付金额
	 */
	private String paidAmount;
	/**
	 * 需支付总金额
	 */
	private String totalAmount;
	private Integer restaurantId;
	private Integer revenueId;
	private Integer userId;
	private Long createTime;
	private Long updateTime;
	/**
	 * 注意这个字段只是前段用的，暂存信息找零信息跟后台没有关系
	 */
	private String cashChange;


	private String partChange;
	/**
	 * 是否可用(-1删除，0正常) 用于本地
	 */
	/**
	 * partImg注意这个字段只是前端用的
	 */
	private String partImg;
	private int isActive;

	public String getPartChange() {
		return partChange;
	}

	public void setPartChange(String partChange) {
		this.partChange = partChange;
	}

	public String getPartImg() {
		return partImg;
	}

	public void setPartImg(String partImg) {
		this.partImg = partImg;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBillNo() {
		return billNo;
	}
	public void setBillNo(Integer billNo) {
		this.billNo = billNo;
	}
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
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
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public String getCashChange() {
		return cashChange;
	}
	public void setCashChange(String cashChange) {
		this.cashChange = cashChange;
	}

	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "PaymentSettlement [id=" + id + ", billNo=" + billNo
				+ ", paymentId=" + paymentId + ", paymentTypeId="
				+ paymentTypeId + ", paidAmount=" + paidAmount
				+ ", totalAmount=" + totalAmount + ", restaurantId="
				+ restaurantId + ", revenueId=" + revenueId + ", userId="
				+ userId + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", cashChange=" + cashChange + ", isActive="
				+ isActive + "]";
	}

}
