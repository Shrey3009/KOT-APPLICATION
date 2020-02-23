package com.alfredbase.javabean;

public class RoundAmount {
	private Integer id;

	private Integer orderId;
	
	private int orderSplitId;

	private Integer billNo;

	private String roundBeforePrice;

	private String roundAlfterPrice;

	private Double roundBalancePrice;

	private Integer restId;

	private Integer revenueId;

	private Integer tableId;

	private Long businessDate;

	private Long createTime;

	private Long updateTime;
	
	public RoundAmount() {
	}

	public RoundAmount(Integer id, Integer orderId, Integer billNo,
			String roundBeforePrice, String roundAlfterPrice,
			Double roundBalancePrice, Integer restId, Integer revenueId,
			Integer tableId, Long businessDate, Long createTime, Long updateTime, int orderSplitId) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.billNo = billNo;
		this.roundBeforePrice = roundBeforePrice;
		this.roundAlfterPrice = roundAlfterPrice;
		this.roundBalancePrice = roundBalancePrice;
		this.restId = restId;
		this.revenueId = revenueId;
		this.tableId = tableId;
		this.businessDate = businessDate;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.orderSplitId = orderSplitId;
	}

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

	public String getRoundBeforePrice() {
		return roundBeforePrice;
	}

	public void setRoundBeforePrice(String roundBeforePrice) {
		this.roundBeforePrice = roundBeforePrice;
	}
	public String getRoundAlfterPrice() {
		return roundAlfterPrice;
	}

	public void setRoundAlfterPrice(String roundAlfterPrice) {
		this.roundAlfterPrice = roundAlfterPrice;
	}

	public Double getRoundBalancePrice() {
		return roundBalancePrice;
	}

	public void setRoundBalancePrice(Double roundBalancePrice) {
		this.roundBalancePrice = roundBalancePrice;
	}

	public Integer getRestId() {
		return restId;
	}

	public void setRestId(Integer restId) {
		this.restId = restId;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Long getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
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
	
	public int getOrderSplitId() {
		return orderSplitId;
	}

	public void setOrderSplitId(int orderSplitId) {
		this.orderSplitId = orderSplitId;
	}

	@Override
	public String toString() {
		return "RoundAmount [id=" + id + ", orderId=" + orderId
				+ ", orderSplitId=" + orderSplitId + ", billNo=" + billNo
				+ ", roundBeforePrice=" + roundBeforePrice
				+ ", roundAlfterPrice=" + roundAlfterPrice
				+ ", roundBalancePrice=" + roundBalancePrice + ", restId="
				+ restId + ", revenueId=" + revenueId + ", tableId=" + tableId
				+ ", businessDate=" + businessDate + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

	
}