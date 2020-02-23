package com.alfredbase.javabean.model;

import java.io.Serializable;

/**
 * <dl>
 * <dt><b>Title:</b></dt>
 * <dd>
 * 支付宝支付推送结果</dd>
 * <dt><b>Description:</b></dt>
 * <dd>
 * <p>
 * none</dd>
 * </dl>
 * 
 * @author
 * @version 1.0,
 */
public class AlipayPushMsgDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1543110282286435202L;

	private String sysTradeNo; // 系统订单号

	private Integer transStatus; // 支付状态

	private Integer posOrderId; // pos机订单ID

	private Integer posBillNo; // pos机bill no

	private String buyerEmail; // 支付宝帐号

	private String tradeNo; // 支付宝交易号

	private Long orderCreateTime; // 交易创建时间

	private String transAmount; // 交易金额

	public String getSysTradeNo() {
		return sysTradeNo;
	}

	public void setSysTradeNo(String sysTradeNo) {
		this.sysTradeNo = sysTradeNo;
	}

	public Integer getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(Integer transStatus) {
		this.transStatus = transStatus;
	}

	public Integer getPosOrderId() {
		return posOrderId;
	}

	public void setPosOrderId(Integer posOrderId) {
		this.posOrderId = posOrderId;
	}

	public Integer getPosBillNo() {
		return posBillNo;
	}

	public void setPosBillNo(Integer posBillNo) {
		this.posBillNo = posBillNo;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public Long getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Long orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	@Override
	public String toString() {
		return "AlipayPushMsgDto [sysTradeNo=" + sysTradeNo + ", transStatus="
				+ transStatus + ", posOrderId=" + posOrderId + ", posBillNo="
				+ posBillNo + ", buyerEmail=" + buyerEmail + ", tradeNo="
				+ tradeNo + ", orderCreateTime=" + orderCreateTime
				+ ", transAmount=" + transAmount + "]";
	}

}
