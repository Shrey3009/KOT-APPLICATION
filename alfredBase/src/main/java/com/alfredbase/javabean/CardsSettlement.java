package com.alfredbase.javabean;

public class CardsSettlement {
	
	private Integer id;
	private Integer paymentId;
	private Integer paymentSettId;
	private Integer billNo;
	private String cardNo;
	private Integer cardType;
	private Integer cvvNo;
	private String cardExpiryDate;
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
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public Integer getCvvNo() {
		return cvvNo;
	}
	public void setCvvNo(Integer cvvNo) {
		this.cvvNo = cvvNo;
	}
	public String getCardExpiryDate() {
		return cardExpiryDate;
	}
	public void setCardExpiryDate(String cardExpiryDate) {
		this.cardExpiryDate = cardExpiryDate;
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
		return "CardsSettlement [id=" + id + ", paymentId=" + paymentId
				+ ", paymentSettId=" + paymentSettId + ", billNo=" + billNo
				+ ", cardNo=" + cardNo + ", cardType=" + cardType + ", cvvNo="
				+ cvvNo + ", cardExpiryDate=" + cardExpiryDate
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", isActive=" + isActive + "]";
	}
	
	
}
