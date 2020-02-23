package com.alfredbase.javabean.temporaryforapp;

public class TempOrder {
	private Integer id;
	private Integer appOrderId;
	private String total;
	private int custId;
	private long placeOrderTime;
	// 状态 0为未下单， 1为已经下过单
	private int status;
	// 是否已经支付 0为 未支付， 1为已支付
	private int paied = 0;
	// 外卖这类的
	private Integer sourceType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAppOrderId() {
		return appOrderId;
	}
	public void setAppOrderId(Integer appOrderId) {
		this.appOrderId = appOrderId;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public long getPlaceOrderTime() {
		return placeOrderTime;
	}
	public void setPlaceOrderTime(long placeOrderTime) {
		this.placeOrderTime = placeOrderTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public int getPaied() {
		return paied;
	}
	public void setPaied(int paied) {
		this.paied = paied;
	}
	@Override
	public String toString() {
		return "TempOrder [id=" + id + ", appOrderId=" + appOrderId
				+ ", total=" + total + ", custId=" + custId
				+ ", placeOrderTime=" + placeOrderTime + ", status=" + status
				+ ", paied=" + paied + ", sourceType=" + sourceType + "]";
	}
}
