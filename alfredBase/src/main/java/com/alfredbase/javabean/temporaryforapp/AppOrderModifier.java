package com.alfredbase.javabean.temporaryforapp;

public class AppOrderModifier {
	private Integer id; 	//'主键id',
	private Integer orderId; 	//'主订单id',
	private Integer orderDetailId; 	//'订单详情id',
	private Integer custId; 	//'顾客id',
	private Integer itemId; 	//'菜单id',
	private Integer modifierId; 	//'配料的id',
	private String modifierName; 	//'配料名称',
	private Integer modifierNum; 	//'配料的数量',
	private Integer status; 	//'配料订单状态(-1删除，0正常)',
	private String modifierPrice; 	//'配料金额',
	private long createTime; 	//'创建时间',
	private long updateTime; 	//'更新时间',

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

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getModifierId() {
		return modifierId;
	}

	public void setModifierId(Integer modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public Integer getModifierNum() {
		return modifierNum;
	}

	public void setModifierNum(Integer modifierNum) {
		this.modifierNum = modifierNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getModifierPrice() {
		return modifierPrice;
	}

	public void setModifierPrice(String modifierPrice) {
		this.modifierPrice = modifierPrice;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "AppOrderModifier{" +
				"id=" + id +
				", orderId=" + orderId +
				", orderDetailId=" + orderDetailId +
				", custId=" + custId +
				", itemId=" + itemId +
				", modifierId=" + modifierId +
				", modifierName='" + modifierName + '\'' +
				", modifierNum=" + modifierNum +
				", status=" + status +
				", modifierPrice='" + modifierPrice + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
