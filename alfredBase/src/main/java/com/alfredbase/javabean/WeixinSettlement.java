package com.alfredbase.javabean;

public class WeixinSettlement {
	
	private Integer id;
	private Integer paymentId;
	private Integer paymentSettId;
	private Integer billNo;
	private String tradeNo;
	private String buyerEmail;
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
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getBuyerEmail() {
		return buyerEmail;
	}
	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
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
		return "AlipaySettlement [id=" + id + ", paymentId=" + paymentId
				+ ", paymentSettId=" + paymentSettId + ", billNo=" + billNo
				+ ", tradeNo=" + tradeNo + ", buyerEmail=" + buyerEmail
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", isActive=" + isActive + "]";
	}
	
}
