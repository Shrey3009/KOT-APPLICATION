package com.alfredbase.javabean;

public class SyncMsg {
	private String id;
	private Integer orderId;
	private Integer msgType;
	private String data;
	private Integer status;
	private Long createTime;
	private Integer revenueId;
	private Long businessDate;

	
	/*
	 *  只用做本地使用 默认为0 
	 *  当为1的时候指的是结账完成 给后台发送的log信息 
	 *  当为2的时候指的是修改支付方式的时候给后台发送的log信息
	 *  之后没修改一次 currCount + 1
	 */
	private int currCount;

	private Integer appOrderId;
	private Integer orderStatus;
	private Integer orderNum;
	private Integer billNo;
	private Integer reportNo;
	public SyncMsg() {
	}

	public SyncMsg(String id, Integer orderId, Integer msgType, String data, Integer status,
			Long createTime, Integer revenueId, Long businessDate,int currCount) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.msgType = msgType;
		this.data = data;
		this.status = status;
		this.createTime = createTime;
		this.revenueId = revenueId;
		this.businessDate = businessDate;
		this.currCount = currCount;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public Long getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
	}

	public int getCurrCount() {
		return currCount;
	}

	public void setCurrCount(int currCount) {
		this.currCount = currCount;
	}

	public Integer getAppOrderId() {
		return appOrderId;
	}

	public void setAppOrderId(Integer appOrderId) {
		this.appOrderId = appOrderId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getBillNo() {
		return billNo;
	}

	public void setBillNo(Integer billNo) {
		this.billNo = billNo;
	}

	public Integer getReportNo() {
		return reportNo;
	}

	public void setReportNo(Integer reportNo) {
		this.reportNo = reportNo;
	}

	@Override
	public String toString() {
		return "SyncMsg{" +
				"id='" + id + '\'' +
				", orderId=" + orderId +
				", msgType=" + msgType +
				", data='" + data + '\'' +
				", status=" + status +
				", createTime=" + createTime +
				", revenueId=" + revenueId +
				", businessDate=" + businessDate +
				", currCount=" + currCount +
				", appOrderId=" + appOrderId +
				", orderStatus=" + orderStatus +
				", orderNum=" + orderNum +
				", billNo=" + billNo +
				", reportNo=" + reportNo +
				'}';
	}

}
