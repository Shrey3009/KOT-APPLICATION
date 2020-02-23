package com.alfredbase.javabean;

public class NetsSettlement {
	private Integer id;
	private Integer referenceNo;
	private Integer paymentId;
	private Integer paymentSettId;
	private Integer billNo;
	private String cashAmount;
	private Long createTime;
	private Long updateTime;
	/**
	 * 是否可用(-1删除，0正常)
	 */
	private int isActive;
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(Integer referenceNo) {
		this.referenceNo = referenceNo;
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
	public String getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}

	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "NetsSettlement [id=" + id + ", referenceNo=" + referenceNo
				+ ", paymentId=" + paymentId + ", paymentSettId="
				+ paymentSettId + ", billNo=" + billNo + ", cashAmount="
				+ cashAmount + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", isActive=" + isActive + "]";
	}
	
	
}
