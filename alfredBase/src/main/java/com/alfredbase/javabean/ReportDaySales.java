package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

import java.io.Serializable;

public class ReportDaySales implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8531196981865820123L;
	private Integer id;
	private Integer restaurantId;
	/**
	 * 餐厅名称
	 */
	private String restaurantName;
	private Integer revenueId;
	/**
	 * 收银机名称
	 */
	private String revenueName;
	/**
	 * 餐厅营业时间
	 */
	private Long businessDate;
	/**
	 * 菜的金额
	 */
	private String itemSales;
	/**
	 * 菜的销售数量
	 */
	private Integer itemSalesQty;
	/**
	 * 百分比折扣率总金额
	 */
	private String discountPer;
	/**
	 * 百分比折扣率次数
	 */
	private Integer discountPerQty;
	/**
	 * 现金折扣总金额
	 */
	private String discount;
	/**
	 * 现金折扣次数
	 */
	private Integer discountQty;
	/**
	 * 总折扣金额
	 */
	private String discountAmt;
	private String focItem;
	private Integer focItemQty;
	/**
	 * 免费金额
	 */
	private String focBill;
	/**
	 * 免费数量
	 */
	private Integer focBillQty;
	private String totalSales;
	private String cash;
	private Integer cashQty;
	private String nets;
	private Integer netsQty;
	private String visa;
	private Integer visaQty;
	private String mc;
	private Integer mcQty;
	private String amex;
	private Integer amexQty;
	private String jbl;
	private Integer jblQty;
	private String unionPay;
	private Integer unionPayQty;
	private String diner;
	private Integer dinerQty;
	private String holdld;
	private Integer holdldQty;
	private String alipay;
	private Integer alipayQty;
	private String weixinpay;
	private Integer weixinpayQty;
	private String thirdParty;
	private Integer thirdPartyQty;
	private String totalCard;
	private Integer totalCardQty;
	private String totalCash;
	private Integer totalCashQty;
	private String deliveroo;
	private Integer deliverooQty;
	private String ubereats;
	private Integer ubereatsQty;
	private String foodpanda;
	private Integer foodpandaQty;
	private String voucher;
	private Integer voucherQty;
	/**
	 * voidBill结算后退单金额
	 */
	private String billVoid;
	/**
	 * voidBill结算后退单数量
	 */
	private Integer billVoidQty;

	/**
	 * voidItem结算前退单金额
	 */
	private String itemVoid;
	/**
	 * voidItem结算前退单数量
	 */
	private Integer itemVoidQty;
	private String nettSales;
	private Integer totalBills;
	private Integer openCount;
	private Integer firstReceipt;
	private Integer lastReceipt;
	/**
	 * 税的总计
	 */
	private String totalTax;
	
	private Integer orderQty;
	
	private Integer personQty;
	
	private String totalBalancePrice;
	
	private String cashInAmt;
	
	private String cashOutAmt;
	
	private String varianceAmt;
	
	private String inclusiveTaxAmt;// 包含税

	private String paypalpay;
	private Integer paypalpayQty;

	private String storedCard;
	private Integer storedCardQty;

	private String topUps;
	private Integer topUpsQty;

	private String billRefund;

	private Integer billRefundQty;

	private String refundTax;

	private String startDrawerAmount;

	private String expectedAmount;

	private String waiterAmount;

	private String difference;

	private String cashTopUp;

	private String takeawaySales;

	private String takeawayTax;

	private Integer takeawayQty;

	private Long createTime;

	private Long updateTime;

	private String totalHour;

	private String reportNoStr;
	private String promotionTotal;
	private String daySalesRound;

	private String payHalal;
	private Integer payHalalQty;
	private  Integer promotionQty;

	private String ipay88Wepay;
	private Integer ipay88WepayQty;

	private String ipay88Alipay;
	private Integer ipay88AlipayQty;

	private String ipay88Boost;
	private Integer ipay88BoostQty;

	private String ipay88Mcash;
	private Integer ipay88McashQty;

	private String ipay88TouchnGo;
	private Integer ipay88TouchnGoQty;

	private String ipay88Unionpay;
	private Integer ipay88UnionpayQty;

	private String ipay88Mbb;
	private Integer ipay88MbbQty;

	private String ipay88Cimb;
	private Integer ipay88CimbQty;

	private String ipay88Grabpay;
	private Integer ipay88GrabpayQty;

	private String ipay88Nets;
	private Integer ipay88NetsQty;


//	private String cashGstDiscount;
//	private String netsGstDiscount;
//	private String visaGstDiscount;
//	private String masterCardGstDiscount;
//	private String amexGstDiscount;
//	private String otherGstDiscount;

	public ReportDaySales() {

	}

	
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

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public String getRevenueName() {
		return revenueName;
	}

	public void setRevenueName(String revenueName) {
		this.revenueName = revenueName;
	}

	public Long getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
	}

	public String getItemSales() {
		return itemSales;
	}

	public void setItemSales(String itemSales) {
		this.itemSales = itemSales;
	}

	public Integer getItemSalesQty() {
		return itemSalesQty;
	}

	public void setItemSalesQty(Integer itemSalesQty) {
		this.itemSalesQty = itemSalesQty;
	}

	public String getDiscountPer() {
		if(CommonUtil.isNull(discountPer))
			return "0.00";
		return discountPer;
	}

	public void setDiscountPer(String discountPer) {
		this.discountPer = discountPer;
	}

	public Integer getDiscountPerQty() {
		return discountPerQty;
	}

	public void setDiscountPerQty(Integer discountPerQty) {
		this.discountPerQty = discountPerQty;
	}

	public String getDiscount() {
		if(CommonUtil.isNull(discount))
			return "0.00";
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Integer getDiscountQty() {
		return discountQty;
	}

	public void setDiscountQty(Integer discountQty) {
		this.discountQty = discountQty;
	}

	public String getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(String discountAmt) {
		this.discountAmt = discountAmt;
	}

	public String getFocItem() {
		return focItem;
	}


	public void setFocItem(String focItem) {
		this.focItem = focItem;
	}


	public Integer getFocItemQty() {
		return focItemQty;
	}


	public void setFocItemQty(Integer focItemQty) {
		this.focItemQty = focItemQty;
	}
	
	public String getFocBill() {
		if(CommonUtil.isNull(focBill))
			return "0.00";
		return focBill;
	}

	public void setFocBill(String focBill) {
		this.focBill = focBill;
	}

	public Integer getFocBillQty() {
		return focBillQty;
	}

	public void setFocBillQty(Integer focBillQty) {
		this.focBillQty = focBillQty;
	}

	public String getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public Integer getCashQty() {
		return cashQty;
	}

	public void setCashQty(Integer cashQty) {
		this.cashQty = cashQty;
	}

	public String getNets() {
		return nets;
	}

	public void setNets(String nets) {
		this.nets = nets;
	}

	public Integer getNetsQty() {
		return netsQty;
	}

	public void setNetsQty(Integer netsQty) {
		this.netsQty = netsQty;
	}

	public String getVisa() {
		return visa;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public Integer getVisaQty() {
		return visaQty;
	}

	public void setVisaQty(Integer visaQty) {
		this.visaQty = visaQty;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public Integer getMcQty() {
		return mcQty;
	}

	public void setMcQty(Integer mcQty) {
		this.mcQty = mcQty;
	}

	public String getAmex() {
		return amex;
	}

	public void setAmex(String amex) {
		this.amex = amex;
	}

	public Integer getAmexQty() {
		return amexQty;
	}

	public void setAmexQty(Integer amexQty) {
		this.amexQty = amexQty;
	}

	public String getJbl() {
		return jbl;
	}

	public void setJbl(String jbl) {
		this.jbl = jbl;
	}

	public Integer getJblQty() {
		return jblQty;
	}

	public void setJblQty(Integer jblQty) {
		this.jblQty = jblQty;
	}

	public String getUnionPay() {
		return unionPay;
	}

	public void setUnionPay(String unionPay) {
		this.unionPay = unionPay;
	}

	public Integer getUnionPayQty() {
		return unionPayQty;
	}

	public void setUnionPayQty(Integer unionPayQty) {
		this.unionPayQty = unionPayQty;
	}

	public String getDiner() {
		return diner;
	}

	public void setDiner(String diner) {
		this.diner = diner;
	}

	public Integer getDinerQty() {
		return dinerQty;
	}

	public void setDinerQty(Integer dinerQty) {
		this.dinerQty = dinerQty;
	}

	public String getHoldld() {
		return holdld;
	}

	public void setHoldld(String holdld) {
		this.holdld = holdld;
	}

	public Integer getHoldldQty() {
		return holdldQty;
	}

	public void setHoldldQty(Integer holdldQty) {
		this.holdldQty = holdldQty;
	}

	public String getTotalCard() {
		return totalCard;
	}

	public void setTotalCard(String totalCard) {
		this.totalCard = totalCard;
	}

	public Integer getTotalCardQty() {
		return totalCardQty;
	}

	public void setTotalCardQty(Integer totalCardQty) {
		this.totalCardQty = totalCardQty;
	}

	public String getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}

	public Integer getTotalCashQty() {
		return totalCashQty;
	}

	public void setTotalCashQty(Integer totalCashQty) {
		this.totalCashQty = totalCashQty;
	}
	
	public String getNettSales() {
		return nettSales;
	}

	public void setNettSales(String nettSales) {
		this.nettSales = nettSales;
	}

	public Integer getTotalBills() {
		return totalBills;
	}

	public void setTotalBills(Integer totalBills) {
		this.totalBills = totalBills;
	}

	public Integer getOpenCount() {
		return openCount;
	}

	public void setOpenCount(Integer openCount) {
		this.openCount = openCount;
	}

	public Integer getFirstReceipt() {
		return firstReceipt;
	}

	public void setFirstReceipt(Integer firstReceipt) {
		this.firstReceipt = firstReceipt;
	}

	public Integer getLastReceipt() {
		return lastReceipt;
	}

	public void setLastReceipt(Integer lastReceipt) {
		this.lastReceipt = lastReceipt;
	}
	
	public String getTotalTax() {
		return totalTax;
	}


	public void setTotalTax(String totalTax) {
		this.totalTax = totalTax;
	}

	public Integer getOrderQty() {
		return orderQty;
	}


	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}


	public Integer getPersonQty() {
		return personQty;
	}


	public void setPersonQty(Integer personQty) {
		this.personQty = personQty;
	}

	public String getTotalBalancePrice() {
		return totalBalancePrice;
	}


	public void setTotalBalancePrice(String totalBalancePrice) {
		this.totalBalancePrice = totalBalancePrice;
	}

	public String getCashInAmt() {
		return cashInAmt;
	}


	public void setCashInAmt(String cashInAmt) {
		this.cashInAmt = cashInAmt;
	}


	public String getCashOutAmt() {
		return cashOutAmt;
	}


	public void setCashOutAmt(String cashOutAmt) {
		this.cashOutAmt = cashOutAmt;
	}


	public String getVarianceAmt() {
		return varianceAmt;
	}


	public void setVarianceAmt(String varianceAmt) {
		this.varianceAmt = varianceAmt;
	}

	public String getBillVoid() {
		return billVoid;
	}


	public void setBillVoid(String billVoid) {
		this.billVoid = billVoid;
	}


	public Integer getBillVoidQty() {
		return billVoidQty;
	}


	public void setBillVoidQty(Integer billVoidQty) {
		this.billVoidQty = billVoidQty;
	}


	public String getItemVoid() {
		return itemVoid;
	}


	public void setItemVoid(String itemVoid) {
		this.itemVoid = itemVoid;
	}


	public Integer getItemVoidQty() {
		return itemVoidQty;
	}


	public void setItemVoidQty(Integer itemVoidQty) {
		this.itemVoidQty = itemVoidQty;
	}


	public String getInclusiveTaxAmt() {
		return inclusiveTaxAmt;
	}


	public void setInclusiveTaxAmt(String inclusiveTaxAmt) {
		this.inclusiveTaxAmt = inclusiveTaxAmt;
	}

	public String getAlipay() {
		return alipay;
	}


	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}


	public Integer getAlipayQty() {
		return alipayQty;
	}


	public void setAlipayQty(Integer alipayQty) {
		this.alipayQty = alipayQty;
	}
	
	public String getWeixinpay() {
		return weixinpay;
	}

	public void setWeixinpay(String weixinpay) {
		this.weixinpay = weixinpay;
	}


	public Integer getWeixinpayQty() {
		return weixinpayQty;
	}


	public void setWeixinpayQty(Integer weixinpayQty) {
		this.weixinpayQty = weixinpayQty;
	}

	public String getThirdParty() {
		return thirdParty;
	}


	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}


	public Integer getThirdPartyQty() {
		return thirdPartyQty;
	}


	public void setThirdPartyQty(Integer thirdPartyQty) {
		this.thirdPartyQty = thirdPartyQty;
	}

	public String getPaypalpay() {
		return paypalpay;
	}

	public void setPaypalpay(String paypalpay) {
		if(paypalpay == null){
			this.paypalpay = "0.00";
		}else {
			this.paypalpay = paypalpay;
		}
	}

	public Integer getPaypalpayQty() {
		return paypalpayQty;
	}

	public void setPaypalpayQty(Integer paypalpayQty) {
		if(paypalpayQty == null){
			this.paypalpayQty = 0;
		}else {
			this.paypalpayQty = paypalpayQty;
		}
	}

	public String getStoredCard() {
		return storedCard;
	}

	public void setStoredCard(String storedCard) {
		this.storedCard = storedCard;
	}

	public Integer getStoredCardQty() {
		return storedCardQty;
	}

	public void setStoredCardQty(Integer storedCardQty) {
		this.storedCardQty = storedCardQty;
	}

	public String getTopUps() {
		return topUps;
	}

	public void setTopUps(String topUps) {
		this.topUps = topUps;
	}

	public Integer getTopUpsQty() {
		return topUpsQty;
	}

	public void setTopUpsQty(Integer topUpsQty) {
		this.topUpsQty = topUpsQty;
	}

	public String getBillRefund() {
		return billRefund;
	}

	public void setBillRefund(String billRefund) {
		this.billRefund = billRefund;
	}

	public Integer getBillRefundQty() {
		return billRefundQty;
	}

	public void setBillRefundQty(Integer billRefundQty) {
		this.billRefundQty = billRefundQty;
	}

	public String getRefundTax() {
		return refundTax;
	}

	public void setRefundTax(String refundTax) {
		this.refundTax = refundTax;
	}

	public String getStartDrawerAmount() {
		return startDrawerAmount;
	}

	public void setStartDrawerAmount(String startDrawerAmount) {
		this.startDrawerAmount = startDrawerAmount;
	}

	public String getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(String expectedAmount) {
		this.expectedAmount = expectedAmount;
	}

	public String getWaiterAmount() {
		return waiterAmount;
	}

	public void setWaiterAmount(String waiterAmount) {
		this.waiterAmount = waiterAmount;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public String getCashTopUp() {
		return cashTopUp;
	}

	public void setCashTopUp(String cashTopUp) {
		this.cashTopUp = cashTopUp;
	}

	public String getTakeawaySales() {
		return takeawaySales;
	}

	public void setTakeawaySales(String takeawaySales) {
		this.takeawaySales = takeawaySales;
	}

	public String getTakeawayTax() {
		return takeawayTax;
	}

	public void setTakeawayTax(String takeawayTax) {
		this.takeawayTax = takeawayTax;
	}

	public Integer getTakeawayQty() {
		return takeawayQty;
	}

	public void setTakeawayQty(Integer takeawayQty) {
		this.takeawayQty = takeawayQty;
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

	public Integer getVoucherQty() {
		return voucherQty;
	}

	public void setVoucherQty(Integer voucherQty) {
		this.voucherQty = voucherQty;
	}

	public String getDeliveroo() {
		return deliveroo;
	}

	public void setDeliveroo(String deliveroo) {
		this.deliveroo = deliveroo;
	}

	public Integer getDeliverooQty() {
		return deliverooQty;
	}

	public void setDeliverooQty(Integer deliverooQty) {
		this.deliverooQty = deliverooQty;
	}

	public String getUbereats() {
		return ubereats;
	}

	public void setUbereats(String ubereats) {
		this.ubereats = ubereats;
	}

	public Integer getUbereatsQty() {
		return ubereatsQty;
	}

	public void setUbereatsQty(Integer ubereatsQty) {
		this.ubereatsQty = ubereatsQty;
	}

	public String getFoodpanda() {
		return foodpanda;
	}

	public void setFoodpanda(String foodpanda) {
		this.foodpanda = foodpanda;
	}

	public Integer getFoodpandaQty() {
		return foodpandaQty;
	}

	public void setFoodpandaQty(Integer foodpandaQty) {
		this.foodpandaQty = foodpandaQty;
	}

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	public String getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(String totalHour) {
		this.totalHour = totalHour;
	}

	public String getReportNoStr() {
		return reportNoStr;
	}

	public void setReportNoStr(String reportNoStr) {
		this.reportNoStr = reportNoStr;
	}

	public String getPromotionTotal() {
		return promotionTotal;
	}

	public void setPromotionTotal(String promotionTotal) {
		this.promotionTotal = promotionTotal;
	}

	public String getPayHalal() {
		return payHalal;
	}

	public void setPayHalal(String payHalal) {
		this.payHalal = payHalal;
	}

	public Integer getPayHalalQty() {
		return payHalalQty;
	}

	public void setPayHalalQty(Integer payHalalQty) {
		this.payHalalQty = payHalalQty;
	}

    public String getDaySalesRound() {
        return daySalesRound;
    }

    public void setDaySalesRound(String daySalesRound) {
        this.daySalesRound = daySalesRound;
    }

    public Integer getPromotionQty() {
        return promotionQty;
    }

    public void setPromotionQty(Integer promotionQty) {
        this.promotionQty = promotionQty;
    }


    public String getIpay88Wepay() {
		return ipay88Wepay;
	}

	public void setIpay88Wepay(String ipay88Wepay) {
		this.ipay88Wepay = ipay88Wepay;
	}

	public Integer getIpay88WepayQty() {
		return ipay88WepayQty;
	}

	public void setIpay88WepayQty(Integer ipay88WepayQty) {
		this.ipay88WepayQty = ipay88WepayQty;
	}

	public String getIpay88Alipay() {
		return ipay88Alipay;
	}

	public void setIpay88Alipay(String ipay88Alipay) {
		this.ipay88Alipay = ipay88Alipay;
	}

	public Integer getIpay88AlipayQty() {
		return ipay88AlipayQty;
	}

	public void setIpay88AlipayQty(Integer ipay88AlipayQty) {
		this.ipay88AlipayQty = ipay88AlipayQty;
	}

	public String getIpay88Boost() {
		return ipay88Boost;
	}

	public void setIpay88Boost(String ipay88Boost) {
		this.ipay88Boost = ipay88Boost;
	}

	public Integer getIpay88BoostQty() {
		return ipay88BoostQty;
	}

	public void setIpay88BoostQty(Integer ipay88BoostQty) {
		this.ipay88BoostQty = ipay88BoostQty;
	}

	public String getIpay88Mcash() {
		return ipay88Mcash;
	}

	public void setIpay88Mcash(String ipay88Mcash) {
		this.ipay88Mcash = ipay88Mcash;
	}

	public Integer getIpay88McashQty() {
		return ipay88McashQty;
	}

	public void setIpay88McashQty(Integer ipay88McashQty) {
		this.ipay88McashQty = ipay88McashQty;
	}

	public String getIpay88TouchnGo() {
		return ipay88TouchnGo;
	}

	public void setIpay88TouchnGo(String ipay88TouchnGo) {
		this.ipay88TouchnGo = ipay88TouchnGo;
	}

	public Integer getIpay88TouchnGoQty() {
		return ipay88TouchnGoQty;
	}

	public void setIpay88TouchnGoQty(Integer ipay88TouchnGoQty) {
		this.ipay88TouchnGoQty = ipay88TouchnGoQty;
	}

	public String getIpay88Unionpay() {
		return ipay88Unionpay;
	}

	public void setIpay88Unionpay(String ipay88Unionpay) {
		this.ipay88Unionpay = ipay88Unionpay;
	}

	public Integer getIpay88UnionpayQty() {
		return ipay88UnionpayQty;
	}

	public void setIpay88UnionpayQty(Integer ipay88UnionpayQty) {
		this.ipay88UnionpayQty = ipay88UnionpayQty;
	}

	public String getIpay88Mbb() {
		return ipay88Mbb;
	}

	public void setIpay88Mbb(String ipay88Mbb) {
		this.ipay88Mbb = ipay88Mbb;
	}

	public Integer getIpay88MbbQty() {
		return ipay88MbbQty;
	}

	public void setIpay88MbbQty(Integer ipay88MbbQty) {
		this.ipay88MbbQty = ipay88MbbQty;
	}

	public String getIpay88Cimb() {
		return ipay88Cimb;
	}

	public void setIpay88Cimb(String ipay88Cimb) {
		this.ipay88Cimb = ipay88Cimb;
	}

	public Integer getIpay88CimbQty() {
		return ipay88CimbQty;
	}

	public void setIpay88CimbQty(Integer ipay88CimbQty) {
		this.ipay88CimbQty = ipay88CimbQty;
	}

	public String getIpay88Grabpay() {
		return ipay88Grabpay;
	}

	public void setIpay88Grabpay(String ipay88Grabpay) {
		this.ipay88Grabpay = ipay88Grabpay;
	}

	public Integer getIpay88GrabpayQty() {
		return ipay88GrabpayQty;
	}

	public void setIpay88GrabpayQty(Integer ipay88GrabpayQty) {
		this.ipay88GrabpayQty = ipay88GrabpayQty;
	}

	public String getIpay88Nets() {
		return ipay88Nets;
	}

	public void setIpay88Nets(String ipay88Nets) {
		this.ipay88Nets = ipay88Nets;
	}

	public Integer getIpay88NetsQty() {
		return ipay88NetsQty;
	}

	public void setIpay88NetsQty(Integer ipay88NetsQty) {
		this.ipay88NetsQty = ipay88NetsQty;
	}

	@Override
	public String toString() {
		return "ReportDaySales{" +
				"id=" + id +
				", restaurantId=" + restaurantId +
				", restaurantName='" + restaurantName + '\'' +
				", revenueId=" + revenueId +
				", revenueName='" + revenueName + '\'' +
				", businessDate=" + businessDate +
				", itemSales='" + itemSales + '\'' +
				", itemSalesQty=" + itemSalesQty +
				", discountPer='" + discountPer + '\'' +
				", discountPerQty=" + discountPerQty +
				", discount='" + discount + '\'' +
				", discountQty=" + discountQty +
				", discountAmt='" + discountAmt + '\'' +
				", focItem='" + focItem + '\'' +
				", focItemQty=" + focItemQty +
				", focBill='" + focBill + '\'' +
				", focBillQty=" + focBillQty +
				", totalSales='" + totalSales + '\'' +
				", cash='" + cash + '\'' +
				", cashQty=" + cashQty +
				", nets='" + nets + '\'' +
				", netsQty=" + netsQty +
				", visa='" + visa + '\'' +
				", visaQty=" + visaQty +
				", mc='" + mc + '\'' +
				", mcQty=" + mcQty +
				", amex='" + amex + '\'' +
				", amexQty=" + amexQty +
				", jbl='" + jbl + '\'' +
				", jblQty=" + jblQty +
				", unionPay='" + unionPay + '\'' +
				", unionPayQty=" + unionPayQty +
				", diner='" + diner + '\'' +
				", dinerQty=" + dinerQty +
				", holdld='" + holdld + '\'' +
				", holdldQty=" + holdldQty +
				", alipay='" + alipay + '\'' +
				", alipayQty=" + alipayQty +
				", weixinpay='" + weixinpay + '\'' +
				", weixinpayQty=" + weixinpayQty +
				", thirdParty='" + thirdParty + '\'' +
				", thirdPartyQty=" + thirdPartyQty +
				", totalCard='" + totalCard + '\'' +
				", totalCardQty=" + totalCardQty +
				", totalCash='" + totalCash + '\'' +
				", totalCashQty=" + totalCashQty +
				", deliveroo='" + deliveroo + '\'' +
				", deliverooQty=" + deliverooQty +
				", ubereats='" + ubereats + '\'' +
				", ubereatsQty=" + ubereatsQty +
				", foodpanda='" + foodpanda + '\'' +
				", foodpandaQty=" + foodpandaQty +
				", voucher='" + voucher + '\'' +
				", voucherQty=" + voucherQty +
				", billVoid='" + billVoid + '\'' +
				", billVoidQty=" + billVoidQty +
				", itemVoid='" + itemVoid + '\'' +
				", itemVoidQty=" + itemVoidQty +
				", nettSales='" + nettSales + '\'' +
				", totalBills=" + totalBills +
				", openCount=" + openCount +
				", firstReceipt=" + firstReceipt +
				", lastReceipt=" + lastReceipt +
				", totalTax='" + totalTax + '\'' +
				", orderQty=" + orderQty +
				", personQty=" + personQty +
				", totalBalancePrice='" + totalBalancePrice + '\'' +
				", cashInAmt='" + cashInAmt + '\'' +
				", cashOutAmt='" + cashOutAmt + '\'' +
				", varianceAmt='" + varianceAmt + '\'' +
				", inclusiveTaxAmt='" + inclusiveTaxAmt + '\'' +
				", paypalpay='" + paypalpay + '\'' +
				", paypalpayQty=" + paypalpayQty +
				", storedCard='" + storedCard + '\'' +
				", storedCardQty=" + storedCardQty +
				", topUps='" + topUps + '\'' +
				", topUpsQty=" + topUpsQty +
				", billRefund='" + billRefund + '\'' +
				", billRefundQty=" + billRefundQty +
				", refundTax='" + refundTax + '\'' +
				", startDrawerAmount='" + startDrawerAmount + '\'' +
				", expectedAmount='" + expectedAmount + '\'' +
				", waiterAmount='" + waiterAmount + '\'' +
				", difference='" + difference + '\'' +
				", cashTopUp='" + cashTopUp + '\'' +
				", takeawaySales='" + takeawaySales + '\'' +
				", takeawayTax='" + takeawayTax + '\'' +
				", takeawayQty=" + takeawayQty +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", totalHour='" + totalHour + '\'' +
				", reportNoStr='" + reportNoStr + '\'' +
				", payHalal='" + payHalal + '\'' +
				", payHalalQty=" + payHalalQty +
				", ipay88Wepay='" + ipay88Wepay + '\'' +
				", ipay88WepayQty=" +ipay88WepayQty +
				", ipay88Alipay='" + ipay88Alipay + '\'' +
				", ipay88AlipayQty=" +ipay88AlipayQty +
				", ipay88Boost='" + ipay88Boost + '\'' +
				", ipay88BoostQty=" +ipay88BoostQty +
				", ipay88Mcash='" + ipay88Mcash + '\'' +
				", ipay88McashQty=" +ipay88McashQty +
				", ipay88TouchnGo='" + ipay88TouchnGo + '\'' +
				", ipay88TouchnGoQty=" +ipay88TouchnGoQty +
				", ipay88Unionpay='" + ipay88Unionpay + '\'' +
				", ipay88UnionpayQty=" +ipay88UnionpayQty +
				", ipay88Mbb='" + ipay88Mbb + '\'' +
				", ipay88MbbQty=" +ipay88MbbQty +
				", ipay88Cimb='" + ipay88Cimb + '\'' +
				", ipay88CimbQty=" +ipay88CimbQty +
				", ipay88Grabpay='" + ipay88Grabpay + '\'' +
				", ipay88GrabpayQty=" +ipay88GrabpayQty +
				", ipay88Nets='" + ipay88Nets + '\'' +
				", ipay88NetsQty=" +ipay88NetsQty +
				", promotionTotal='" + promotionTotal + '\'' +
				", daySalesRound='" + daySalesRound + '\'' +
				'}';
	}
}