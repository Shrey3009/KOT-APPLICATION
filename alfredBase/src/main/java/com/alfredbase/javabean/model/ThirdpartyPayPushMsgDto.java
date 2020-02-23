package com.alfredbase.javabean.model;

import java.io.Serializable;

/**
 * <dl>
 * <dt><b>Title:</b></dt>
 * <dd>
 * 第三方应用支付推送结果</dd>
 * <dt><b>Description:</b></dt>
 * <dd>
 * <p>
 * none</dd>
 * </dl>
 * 
 * @author
 * @version 1.0,
 */
public class ThirdpartyPayPushMsgDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3849613440616658423L;
	
	private Integer id;

	private String sysOrderId; // 系统订单号

	private String payType; // 支付类型


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSysOrderId() {
		return sysOrderId;
	}

	public void setSysOrderId(String sysOrderId) {
		this.sysOrderId = sysOrderId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Override
	public String toString() {
		return "ThirdpartyPayPushMsgDto [id=" + id + ", sysOrderId="
				+ sysOrderId + ", payType=" + payType + "]";
	}

}
