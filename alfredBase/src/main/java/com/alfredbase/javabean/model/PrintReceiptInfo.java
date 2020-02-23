package com.alfredbase.javabean.model;

public class PrintReceiptInfo {
	private int paymentTypeId;
	private String paidAmount;
	private String cashChange;
	private String cardNo;
	private String paymentTypeName;
	public int getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getCashChange() {
		return cashChange;
	}
	public void setCashChange(String cashChange) {
		this.cashChange = cashChange;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	@Override
	public String toString() {
		return "PrintReceiptInfo{" +
				"paymentTypeId=" + paymentTypeId +
				", paidAmount='" + paidAmount + '\'' +
				", cashChange='" + cashChange + '\'' +
				", cardNo='" + cardNo + '\'' +
				", paymentTypeName='" + paymentTypeName + '\'' +
				'}';
	}
}
