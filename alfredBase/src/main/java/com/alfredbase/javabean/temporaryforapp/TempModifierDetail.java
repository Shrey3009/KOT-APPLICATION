package com.alfredbase.javabean.temporaryforapp;

public class TempModifierDetail {
	private Integer id;
	private Integer modifierId;
	private Integer orderDetailId;
	private Integer itemId;
	private String modifierName;
	private String modifierPrice;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getModifierId() {
		return modifierId;
	}
	public void setModifierId(Integer modifierId) {
		this.modifierId = modifierId;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public String getModifierPrice() {
		return modifierPrice;
	}
	public void setModifierPrice(String modifierPrice) {
		this.modifierPrice = modifierPrice;
	}
	@Override
	public String toString() {
		return "TempModifierDetail [id=" + id + ", modifierId=" + modifierId
				+ ", orderDetailId=" + orderDetailId + ", itemId=" + itemId
				+ ", modifierName=" + modifierName + ", modifierPrice="
				+ modifierPrice + "]";
	}
}
