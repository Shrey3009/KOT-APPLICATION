package com.alfredbase.javabean;


public class OrderDetailTax {

	private Integer id;
	private Integer orderId;
	private Integer orderDetailId;
	private Integer taxId;
	/**
	 * 税收名称
	 */
	private String taxName;
	/**
	 * 税收比例
	 */
	private String taxPercentage;
	private String taxPrice;
	/**
	 * 税收类型（0消费税， 1服务税）
	 */
	private Integer taxType;
	private Long createTime;
	private Long updateTime;
	private int indexId;
	
	private Integer orderSplitId;
	
	private int isActive;

	public OrderDetailTax() {

	}

	public OrderDetailTax(Integer id, Integer orderId, Integer orderDetailId,
			Integer taxId, String taxName, String taxPercentage,
			String taxPrice, Integer taxType, Long createTime,
			Long updateTime) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.orderDetailId = orderDetailId;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercentage = taxPercentage;
		this.taxPrice = taxPrice;
		this.taxType = taxType;
		this.createTime = createTime;
		this.updateTime = updateTime;
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

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public String getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}

	public Integer getTaxType() {
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
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

	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public Integer getOrderSplitId() {
		return orderSplitId;
	}

	public void setOrderSplitId(Integer orderSplitId) {
		this.orderSplitId = orderSplitId;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "OrderDetailTax [id=" + id + ", orderId=" + orderId
				+ ", orderDetailId=" + orderDetailId + ", taxId=" + taxId
				+ ", taxName=" + taxName + ", taxPercentage=" + taxPercentage
				+ ", taxPrice=" + taxPrice + ", taxType=" + taxType
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", indexId=" + indexId + ", orderSplitId=" + orderSplitId
				+ ", isActive=" + isActive + "]";
	}
}
