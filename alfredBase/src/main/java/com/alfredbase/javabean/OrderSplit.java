package com.alfredbase.javabean;


public class OrderSplit {
	private Integer id;

	private Integer orderId;
	/**
	 * 订单来源 1 pos， 2 服务员， 3 桌子自助， 4 手机app
	 */
	private Integer orderOriginId;
	/**
	 * 当前操作员id
	 */
	private Integer userId;
	/**
	 * 就餐人数
	 */
	private Integer persons;
	/**
	 * 订单状态（10打开、11待支付、12已支付、30已完成）
	 */
	private Integer orderStatus;
	/**
	 * 订单总金额
	 */
	private String subTotal;
	/**
	 * 税收总金额
	 */
	private String taxAmount;
	/**
	 * 打折总金额
	 */
	private String discountAmount;
	/**
	 * 实收金额 = 订单总金额 + 税收总金额 - 打折总金额
	 */
	private String total;
	/**
	 * 早中午藏 （0全天、 1早上、 2午餐、 3晚餐）
	 */
	private Integer sessionStatus;
	/**
	 * 餐厅id
	 */
	private Integer restId;

	private Integer revenueId;

	private Integer tableId;

	private Long createTime;

	private Long updateTime;
	
	private String sysCreateTime;
	
	private String sysUpdateTime;
	
	private Integer groupId;
	
	private String inclusiveTaxName;
	
	private String inclusiveTaxPrice;
	
	private String inclusiveTaxPercentage;
	// 在已经结账的订单上面做修改的时候 用到 临时用 不存数据库
	private String oldTotal;

	private int splitByPax;

	// 只在副机传送数据到主机的时候使用，注意只存到 CPOrderSplit表中，默认的 OrderSplit表中 不存
	private int oldOrderSplitId = 0;

	public OrderSplit() {
	}

	public OrderSplit(Integer id, Integer orderId, Integer orderOriginId,
			Integer userId, Integer persons, Integer orderStatus,
			String subTotal, String taxAmount, String discountAmount,
			String total, Integer sessionStatus, Integer restId,
			Integer revenueId, Integer tableId, Long createTime,
			Long updateTime, Integer groupId) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.orderOriginId = orderOriginId;
		this.userId = userId;
		this.persons = persons;
		this.orderStatus = orderStatus;
		this.subTotal = subTotal;
		this.taxAmount = taxAmount;
		this.discountAmount = discountAmount;
		this.total = total;
		this.sessionStatus = sessionStatus;
		this.restId = restId;
		this.revenueId = revenueId;
		this.tableId = tableId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.groupId = groupId;
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

	public Integer getOrderOriginId() {
		return orderOriginId;
	}

	public void setOrderOriginId(Integer orderOriginId) {
		this.orderOriginId = orderOriginId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPersons() {
		return persons;
	}

	public void setPersons(Integer persons) {
		this.persons = persons;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Integer getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(Integer sessionStatus) {
		this.sessionStatus = sessionStatus;
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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getSysCreateTime() {
		return sysCreateTime;
	}

	public void setSysCreateTime(String sysCreateTime) {
		this.sysCreateTime = sysCreateTime;
	}

	public String getSysUpdateTime() {
		return sysUpdateTime;
	}

	public void setSysUpdateTime(String sysUpdateTime) {
		this.sysUpdateTime = sysUpdateTime;
	}

	public String getInclusiveTaxName() {
		return inclusiveTaxName;
	}

	public void setInclusiveTaxName(String inclusiveTaxName) {
		this.inclusiveTaxName = inclusiveTaxName;
	}

	public String getInclusiveTaxPrice() {
		return inclusiveTaxPrice;
	}

	public void setInclusiveTaxPrice(String inclusiveTaxPrice) {
		this.inclusiveTaxPrice = inclusiveTaxPrice;
	}

	public String getInclusiveTaxPercentage() {
		return inclusiveTaxPercentage;
	}

	public void setInclusiveTaxPercentage(String inclusiveTaxPercentage) {
		this.inclusiveTaxPercentage = inclusiveTaxPercentage;
	}

	public String getOldTotal() {
		return oldTotal;
	}

	public void setOldTotal(String oldTotal) {
		this.oldTotal = oldTotal;
	}

	public Integer getSplitByPax() {
		return splitByPax;
	}

	public void setSplitByPax(Integer splitByPax) {
		this.splitByPax = splitByPax;
	}

	public int getOldOrderSplitId() {
		return oldOrderSplitId;
	}

	public void setOldOrderSplitId(int oldOrderSplitId) {
		this.oldOrderSplitId = oldOrderSplitId;
	}

	@Override
	public String toString() {
		return "OrderSplit{" +
				"id=" + id +
				", orderId=" + orderId +
				", orderOriginId=" + orderOriginId +
				", userId=" + userId +
				", persons=" + persons +
				", orderStatus=" + orderStatus +
				", subTotal='" + subTotal + '\'' +
				", taxAmount='" + taxAmount + '\'' +
				", discountAmount='" + discountAmount + '\'' +
				", total='" + total + '\'' +
				", sessionStatus=" + sessionStatus +
				", restId=" + restId +
				", revenueId=" + revenueId +
				", tableId=" + tableId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", sysCreateTime='" + sysCreateTime + '\'' +
				", sysUpdateTime='" + sysUpdateTime + '\'' +
				", groupId=" + groupId +
				", inclusiveTaxName='" + inclusiveTaxName + '\'' +
				", inclusiveTaxPrice='" + inclusiveTaxPrice + '\'' +
				", inclusiveTaxPercentage='" + inclusiveTaxPercentage + '\'' +
				", oldTotal='" + oldTotal + '\'' +
				", splitByPax=" + splitByPax +
				", oldOrderSplitId=" + oldOrderSplitId +
				'}';
	}
}