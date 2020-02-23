package com.alfredbase.javabean;


public class NonChargableSettlement {
	private Integer id;
	private Integer orderId;
	private Integer billNo;
	private Integer paymentId;
	private Integer paymentSettId;
	/**
	 * 客户姓名
	 */
	private String nameOfPerson;
	/**
	 * 原因
	 */
	private String remarks;
	/**
	 * 验证人用户id
	 */
	private Integer authorizedUserId;
	/**
	 * 修改金额
	 */
	private String amount;
	private Integer restaurantId;
	private Integer revenueId;
	/**
	 * 收银员用户id
	 */
	private Integer userId;
	private Long createTime;
	private Long updateTime;
	/**
	 * 是否可用(-1删除，0正常)
	 */
	private int isActive;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
	public Integer getPaymentSettId() {
		return paymentSettId;
	}
	public void setPaymentSettId(Integer paymentSettId) {
		this.paymentSettId = paymentSettId;
	}
	public String getNameOfPerson() {
		return nameOfPerson;
	}
	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getAuthorizedUserId() {
		return authorizedUserId;
	}
	public void setAuthorizedUserId(Integer authorizedUserId) {
		this.authorizedUserId = authorizedUserId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "NonChargableSettlement [id=" + id + ", orderId=" + orderId
				+ ", billNo=" + billNo + ", paymentId=" + paymentId
				+ ", paymentSettId=" + paymentSettId + ", nameOfPerson="
				+ nameOfPerson + ", remarks=" + remarks + ", authorizedUserId="
				+ authorizedUserId + ", amount=" + amount + ", restaurantId="
				+ restaurantId + ", revenueId=" + revenueId + ", userId="
				+ userId + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", isActive=" + isActive + "]";
	}
	
	
}
