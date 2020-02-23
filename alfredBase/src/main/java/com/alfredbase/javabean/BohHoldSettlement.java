package com.alfredbase.javabean;


public class BohHoldSettlement {
	private Integer id;
	private Integer restaurantId;
	private Integer revenueId;
	private Integer orderId;
	private Integer paymentId;
	private Integer paymentSettId;
	private Integer billNo;
	/**
	 * 赊账人姓名
	 */
	private String nameOfPerson;
	/**
	 * 赊账人电话
	 */
	private String phone;
	/**
	 * 原因
	 */
	private String remarks;
	/**
	 * 授权同意人id
	 */
	private Integer authorizedUserId;
	/**
	 * 赊账金额
	 */
	private String amount;
	/**
	 * 赊账状态
	 */
	private Integer status;
	/**
	 * 支付方式
	 */
	private Integer paymentType;
	/**
	 * 支付时间
	 */
	private Long paidDate;
	/**
	 * 赊账时间
	 */
	private Long daysDue;
	
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
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
	public Integer getBillNo() {
		return billNo;
	}
	public void setBillNo(Integer billNo) {
		this.billNo = billNo;
	}
	public String getNameOfPerson() {
		return nameOfPerson;
	}
	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Long getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(Long paidDate) {
		this.paidDate = paidDate;
	}
	public Long getDaysDue() {
		return daysDue;
	}
	public void setDaysDue(Long daysDue) {
		this.daysDue = daysDue;
	}

	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	@Override
	public String toString() {
		return "BohHoldSettlement [id=" + id + ", restaurantId=" + restaurantId
				+ ", revenueId=" + revenueId + ", orderId=" + orderId
				+ ", paymentId=" + paymentId + ", paymentSettId="
				+ paymentSettId + ", billNo=" + billNo + ", nameOfPerson="
				+ nameOfPerson + ", phone=" + phone + ", remarks=" + remarks
				+ ", authorizedUserId=" + authorizedUserId + ", amount="
				+ amount + ", status=" + status + ", paymentType="
				+ paymentType + ", paidDate=" + paidDate + ", daysDue="
				+ daysDue + ", isActive=" + isActive + "]";
	}
	
}
