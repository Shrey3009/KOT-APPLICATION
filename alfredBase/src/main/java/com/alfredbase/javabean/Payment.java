package com.alfredbase.javabean;

public class Payment {
	private Integer id;
	private Integer billNo;
	private Integer orderId;
	private Integer orderSplitId;

	/**
	 * 营业时间
	 */
	private Long businessDate;
	/**
	 * bill类型（0未拆单，1已拆单）
	 */
	private Integer type;
	private Integer restaurantId;
	private Integer revenueId;
	private Integer userId;
	/**
	 * 待支付总金额
	 */
	private String paymentAmount;
	/**
	 * 税的总金额（不参与计算）
	 */
	private String taxAmount;
	/**
	 * 折扣金额
	 */
	private String discountAmount;
	private Long createTime;
	private Long updateTime;

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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderSplitId() {
		return orderSplitId;
	}

	public void setOrderSplitId(Integer orderSplitId) {
		this.orderSplitId = orderSplitId;
	}

	public Long getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
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

	@Override
	public String toString() {
		return "Payment [id=" + id + ", billNo=" + billNo + ", orderId="
				+ orderId + ", orderSplitId=" + orderSplitId
				+ ", businessDate=" + businessDate + ", type=" + type
				+ ", restaurantId=" + restaurantId + ", revenueId=" + revenueId
				+ ", userId=" + userId + ", paymentAmount=" + paymentAmount
				+ ", taxAmount=" + taxAmount + ", discountAmount="
				+ discountAmount + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}
}
