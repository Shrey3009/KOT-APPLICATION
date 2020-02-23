package com.alfredbase.javabean;
/**
 * 后台配置信息对象
 * @author Alex
 *
 */
public class RestaurantConfig {
	
	private Integer id;
	private Integer restaurantId;
	private Integer paraId;
	/*
	 * 配置类型
	 */
	private Integer paraType;
	private String paraName;
	/*
	 * 配置值 是一个用“;;”分割的String
	 */
	private String paraValue1;
	private String paraValue2;
	private String paraValue3;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	public Integer getParaId() {
		return paraId;
	}
	public void setParaId(Integer paraId) {
		this.paraId = paraId;
	}
	public Integer getParaType() {
		return paraType;
	}
	public void setParaType(Integer paraType) {
		this.paraType = paraType;
	}
	public String getParaName() {
		return paraName;
	}
	public void setParaName(String paraName) {
		this.paraName = paraName;
	}
	public String getParaValue1() {
		return paraValue1;
	}
	public void setParaValue1(String paraValue1) {
		this.paraValue1 = paraValue1;
	}
	public String getParaValue2() {
		return paraValue2;
	}
	public void setParaValue2(String paraValue2) {
		this.paraValue2 = paraValue2;
	}

	public String getParaValue3() {
		return paraValue3;
	}

	public void setParaValue3(String paraValue3) {
		this.paraValue3 = paraValue3;
	}

	@Override
	public String toString() {
		return "RestaurantConfig{" +
				"id=" + id +
				", restaurantId=" + restaurantId +
				", paraId=" + paraId +
				", paraType=" + paraType +
				", paraName='" + paraName + '\'' +
				", paraValue1='" + paraValue1 + '\'' +
				", paraValue2='" + paraValue2 + '\'' +
				", paraValue3='" + paraValue3 + '\'' +
				'}';
	}
}
