package com.alfredbase.store.sql;

import com.alfredbase.javabean.AlipaySettlement;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.javabean.CardsSettlement;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.NetsSettlement;
import com.alfredbase.javabean.NonChargableSettlement;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderPromotion;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.VoidSettlement;
import com.alfredbase.javabean.WeixinSettlement;
import com.alfredbase.store.sql.cpsql.CPOrderBillSQL;
import com.alfredbase.store.sql.cpsql.CPOrderDetailSQL;
import com.alfredbase.store.sql.cpsql.CPOrderDetailTaxSQL;
import com.alfredbase.store.sql.cpsql.CPOrderModifierSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSplitSQL;
import com.alfredbase.store.sql.cpsql.CPPaymentSQL;
import com.alfredbase.store.sql.cpsql.CPPaymentSettlementSQL;
import com.alfredbase.store.sql.cpsql.CPRoundAmountSQL;
import com.alfredbase.utils.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadSQL {

    public static Map<String, Object> getOrderInfo(Integer orderId) {
        Order order = OrderSQL.getOrder(orderId);
        if (order == null)
            return null;
        /**
         * 订单部分
         */
        List<OrderDetail> orderDetails = OrderDetailSQL.getAllOrderDetailsByOrder(order);

        List<OrderModifier> orderModifiers = OrderModifierSQL
                .getAllOrderModifier(order);

        List<OrderDetailTax> orderDetailTaxs = OrderDetailTaxSQL
                .getAllOrderDetailTax(order);

        List<OrderSplit> orderSplits = OrderSplitSQL.getOrderSplits(order);

        List<OrderBill> orderBills = OrderBillSQL.getAllOrderBillByOrder(order);

        List<RoundAmount> roundAmounts = RoundAmountSQL.getRoundAmountForSync(order);
        List<OrderPromotion> orderPromotions = PromotionDataSQL.getPromotionDataOrOrderid(order.getId());
        KotSummary kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());

        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByOrderId(order.getId());

        /**
         * 支付部分
         */
        List<Payment> payments = PaymentSQL.getPaymentByOrderIdForSyncData(orderId);
        ArrayList<PaymentSettlement> paymentSettlements = new ArrayList<PaymentSettlement>();
        ArrayList<CardsSettlement> cardsSettlements = new ArrayList<CardsSettlement>();
        ArrayList<NetsSettlement> netsSettlements = new ArrayList<NetsSettlement>();
        ArrayList<NonChargableSettlement> nonChargableSettlements = new ArrayList<NonChargableSettlement>();
        ArrayList<BohHoldSettlement> bohHoldSettlements = new ArrayList<BohHoldSettlement>();
        ArrayList<VoidSettlement> voidSettlements = new ArrayList<VoidSettlement>();
        List<AlipaySettlement> alipaySettlements = new ArrayList<AlipaySettlement>();
        List<WeixinSettlement> weixinSettlements = new ArrayList<WeixinSettlement>();

        for (Payment payment : payments) {
            if (payment != null) {
                paymentSettlements.addAll(PaymentSettlementSQL
                        .getPaymentSettlementsBypaymentId(payment.getId().intValue()));
                cardsSettlements.addAll(CardsSettlementSQL
                        .getCardsSettlementsByPamentId(payment.getId().intValue()));
                netsSettlements.addAll(NetsSettlementSQL
                        .getNetsSettlementsByPamentId(payment.getId().intValue()));
                nonChargableSettlements.addAll(NonChargableSettlementSQL
                        .getNonChargableSettlementsByPaymentId(payment.getId().intValue()));
                bohHoldSettlements.addAll(BohHoldSettlementSQL
                        .getBohHoldSettlementsByPamentId(payment.getId().intValue()));
                voidSettlements.addAll(VoidSettlementSQL
                        .getVoidSettlementsByPamentId(payment.getId().intValue()));
                alipaySettlements.addAll(AlipaySettlementSQL
                        .getAlipaySettlementByPamentId(payment.getId().intValue()));
                weixinSettlements.addAll(WeixinSettlementSQL
                        .getWeixinSettlementByPamentId(payment.getId().intValue()));
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", order);
        map.put("orderBills", orderBills);
        map.put("roundAmounts", roundAmounts);
        map.put("orderDetails", orderDetails);
        map.put("orderModifiers", orderModifiers);
        map.put("orderDetailTaxs", orderDetailTaxs);
        map.put("orderSplits", orderSplits);
        map.put("payments", payments);
        map.put("paymentSettlements", paymentSettlements);
        map.put("cardsSettlements", cardsSettlements);
        map.put("netsSettlements", netsSettlements);
        map.put("nonChargableSettlements", nonChargableSettlements);
        map.put("bohHoldSettlements", bohHoldSettlements);
        map.put("voidSettlements", voidSettlements);
        map.put("alipaySettlements", alipaySettlements);
        map.put("weixinSettlements", weixinSettlements);
        map.put("orderPromotions", orderPromotions);
        map.put("kotSummary", kotSummary);
        map.put("kotItemDetails", kotItemDetails);
        return map;
    }

    public static Map<String, Object> getOrderInfoWhenSubPosEditSettlement(Integer orderId) {
        Order order = OrderSQL.getOrder(orderId);
        if (order == null)
            return null;
        /**
         * 订单部分
         */
        List<RoundAmount> roundAmounts = RoundAmountSQL.getRoundAmountForSync(order);
        /**
         * 支付部分
         */
        List<Payment> payments = PaymentSQL.getPaymentByOrderIdForSyncData(orderId);
        ArrayList<PaymentSettlement> paymentSettlements = new ArrayList<PaymentSettlement>();
        for (Payment payment : payments) {
            if (payment != null) {
                paymentSettlements.addAll(PaymentSettlementSQL
                        .getPaymentSettlementsBypaymentId(payment.getId().intValue()));
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", order);
        map.put("roundAmounts", roundAmounts);
        map.put("payments", payments);
        map.put("paymentSettlements", paymentSettlements);
        return map;
    }

    public static Map<String, Object> getSubPosOrderInfo(Integer orderId) {
        Order order = CPOrderSQL.getOrder(orderId);
        if (order == null)
            return null;
        /**
         * 订单部分
         */
        List<OrderDetail> orderDetails = CPOrderDetailSQL.getAllOrderDetailsByOrder(order);

        List<OrderModifier> orderModifiers = CPOrderModifierSQL
                .getAllOrderModifier(order);

        List<OrderDetailTax> orderDetailTaxs = CPOrderDetailTaxSQL
                .getAllOrderDetailTax(order);

        List<OrderSplit> orderSplits = CPOrderSplitSQL.getOrderSplits(order);

        List<OrderBill> orderBills = CPOrderBillSQL.getAllOrderBillByOrder(order);

        List<RoundAmount> roundAmounts = CPRoundAmountSQL.getRoundAmountForSync(order);

        KotSummary kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());

        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByOrderId(order.getId());

        /**
         * 支付部分
         */
        List<Payment> payments = CPPaymentSQL.getPaymentByOrderIdForSyncData(orderId);
        ArrayList<PaymentSettlement> paymentSettlements = new ArrayList<PaymentSettlement>();
        ArrayList<CardsSettlement> cardsSettlements = new ArrayList<CardsSettlement>();
        ArrayList<NetsSettlement> netsSettlements = new ArrayList<NetsSettlement>();
        ArrayList<NonChargableSettlement> nonChargableSettlements = new ArrayList<NonChargableSettlement>();
        ArrayList<BohHoldSettlement> bohHoldSettlements = new ArrayList<BohHoldSettlement>();
        ArrayList<VoidSettlement> voidSettlements = new ArrayList<VoidSettlement>();
        List<AlipaySettlement> alipaySettlements = new ArrayList<AlipaySettlement>();
        List<WeixinSettlement> weixinSettlements = new ArrayList<WeixinSettlement>();
        for (Payment payment : payments) {
            if (payment != null) {
                paymentSettlements.addAll(CPPaymentSettlementSQL
                        .getPaymentSettlementsBypaymentId(payment.getId().intValue()));
//				cardsSettlements.addAll(CardsSettlementSQL
//						.getCardsSettlementsByPamentId(payment.getId().intValue()));
//				netsSettlements.addAll(NetsSettlementSQL
//						.getNetsSettlementsByPamentId(payment.getId().intValue()));
//				nonChargableSettlements.addAll(NonChargableSettlementSQL
//						.getNonChargableSettlementsByPaymentId(payment.getId().intValue()));
//				bohHoldSettlements.addAll(BohHoldSettlementSQL
//						.getBohHoldSettlementsByPamentId(payment.getId().intValue()));
//				voidSettlements.addAll(VoidSettlementSQL
//						.getVoidSettlementsByPamentId(payment.getId().intValue()));
//				alipaySettlements.addAll(AlipaySettlementSQL
//						.getAlipaySettlementByPamentId(payment.getId().intValue()));
//				weixinSettlements.addAll(WeixinSettlementSQL
//						.getWeixinSettlementByPamentId(payment.getId().intValue()));
            }
        }
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        LogUtil.i("bohHoldSettlements", gson.toJson(bohHoldSettlements));
        map.put("order", order);
        map.put("orderBills", orderBills);
        map.put("roundAmounts", roundAmounts);
        map.put("orderDetails", orderDetails);
        map.put("orderModifiers", orderModifiers);
        map.put("orderDetailTaxs", orderDetailTaxs);
        map.put("orderSplits", orderSplits);
        map.put("payments", payments);
        map.put("paymentSettlements", paymentSettlements);
        map.put("cardsSettlements", cardsSettlements);
        map.put("netsSettlements", netsSettlements);
        map.put("nonChargableSettlements", nonChargableSettlements);
        map.put("bohHoldSettlements", bohHoldSettlements);
        map.put("voidSettlements", voidSettlements);
        map.put("alipaySettlements", alipaySettlements);
        map.put("weixinSettlements", weixinSettlements);
        map.put("kotSummary", kotSummary);
        map.put("kotItemDetails", kotItemDetails);
        return map;
    }

//	public static Map<String,Object> getAllReportInfo(long businessDate){
//		Map<String, Object> map = new HashMap<String, Object>();
//		ReportDaySales reportDaySales = null;ReportDaySalesSQL.getReportDaySalesByTime(businessDate);
//		ArrayList<ReportDayTax> reportDayTaxs = new ArrayList<ReportDayTax>();
//		ArrayList<ReportPluDayItem> reportPluDayItems = new ArrayList<ReportPluDayItem>();
//		ArrayList<ReportPluDayModifier> reportPluDayModifiers = new ArrayList<ReportPluDayModifier>();
//		ArrayList<ReportHourly> reportHourlys = new ArrayList<ReportHourly>();
//		
//		reportDaySales = ReportDaySalesSQL.getReportDaySalesByTime(businessDate);
//		reportDayTaxs = ReportDayTaxSQL.getReportDayTaxsByNowTime(businessDate);
//		reportPluDayItems = ReportPluDayItemSQL.getReportPluDayItemsByTime(businessDate);
//		reportPluDayModifiers = ReportPluDayModifierSQL.getReportPluDayModifiersByTime(businessDate);
//		reportHourlys = ReportHourlySQL.getReportHourlysByTime(businessDate);
//		
//		map.put("reportDaySales", reportDaySales);
//		map.put("reportDayTaxs", reportDayTaxs);
//		map.put("reportPluDayItems", reportPluDayItems);
//		map.put("reportPluDayModifiers", reportPluDayModifiers);
//		map.put("reportHourlys", reportHourlys);
//		
//		return map;
//	}

}
