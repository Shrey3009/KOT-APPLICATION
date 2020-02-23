package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportDaySalesSQL {
    public static void addReportDaySales(ReportDaySales reportDaySales) {
        if (reportDaySales == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.ReportDaySales
                    + "(id, restaurantId, restaurantName, revenueId, revenueName, businessDate, itemSales, itemSalesQty, discountPer, discountPerQty,"
                    + " discount, discountQty, discountAmt, focItem, focItemQty, focBill, focBillQty, totalSales, cash, cashQty, nets, netsQty, visa,"
                    + " visaQty, mc, mcQty, amex, amexQty, jbl, jblQty, unionPay, unionPayQty, diner, dinerQty, holdld, holdldQty, totalCard, totalCardQty,"
                    + " totalCash, totalCashQty, billVoid, billVoidQty, itemVoid, itemVoidQty, nettSales, totalBills, openCount, firstReceipt, lastReceipt,"
                    + " totalTax, orderQty, personQty, totalBalancePrice, cashInAmt, cashOutAmt, varianceAmt, inclusiveTaxAmt, alipay, alipayQty, thirdParty,"
                    + " thirdPartyQty, weixinpay, weixinpayQty, paypalpay, paypalpayQty, storedCard, storedCardQty, topUps, topUpsQty, billRefund, billRefundQty,"
                    + " refundTax, startDrawerAmount, expectedAmount, waiterAmount, difference, cashTopUp, takeawaySales, takeawayTax, takeawayQty, createTime,"
                    + " updateTime, deliveroo, deliverooQty, ubereats, ubereatsQty, foodpanda, foodpandaQty, voucher, voucherQty, totalHour, reportNoStr,"
                    + " payHalal, payHalalQty, promotionTotal, ipay88Wepay, ipay88WepayQty, ipay88Alipay, ipay88AlipayQty, ipay88Boost, ipay88BoostQty, ipay88Mcash, ipay88McashQty, ipay88TouchnGo, ipay88TouchnGoQty, ipay88Unionpay, ipay88UnionpayQty, ipay88Mbb, ipay88MbbQty, ipay88Cimb, ipay88CimbQty, ipay88Grabpay, ipay88GrabpayQty, ipay88Nets, ipay88NetsQty,daySalesRound,promotionQty )"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{reportDaySales.getId(),
                            reportDaySales.getRestaurantId(),
                            reportDaySales.getRestaurantName(),
                            reportDaySales.getRevenueId(),
                            reportDaySales.getRevenueName(),
                            reportDaySales.getBusinessDate(),
                            reportDaySales.getItemSales() == null ? "0.00" : reportDaySales.getItemSales(),
                            reportDaySales.getItemSalesQty() == null ? 0 : reportDaySales.getItemSalesQty(),
                            reportDaySales.getDiscountPer() == null ? "0.00" : reportDaySales.getDiscountPer(),
                            reportDaySales.getDiscountPerQty() == null ? 0 : reportDaySales.getDiscountPerQty(),
                            reportDaySales.getDiscount() == null ? 0 : reportDaySales.getDiscount(),
                            reportDaySales.getDiscountQty() == null ? 0 : reportDaySales.getDiscountQty(),
                            reportDaySales.getDiscountAmt() == null ? "0.00" : reportDaySales.getDiscountAmt(),
                            reportDaySales.getFocItem() == null ? "0.00" : reportDaySales.getFocItem(),
                            reportDaySales.getFocItemQty() == null ? 0 : reportDaySales.getFocItemQty(),
                            reportDaySales.getFocBill() == null ? "0.00" : reportDaySales.getFocBill(),
                            reportDaySales.getFocBillQty() == null ? 0 : reportDaySales.getFocBillQty(),
                            reportDaySales.getTotalSales() == null ? "0.00" : reportDaySales.getTotalSales(),
                            reportDaySales.getCash() == null ? "0.00" : reportDaySales.getCash(),
                            reportDaySales.getCashQty() == null ? 0 : reportDaySales.getCashQty(),
                            reportDaySales.getNets() == null ? "0.00" : reportDaySales.getNets(),
                            reportDaySales.getNetsQty() == null ? 0 : reportDaySales.getNetsQty(),
                            reportDaySales.getVisa() == null ? "0.00" : reportDaySales.getVisa(),
                            reportDaySales.getVisaQty() == null ? 0 : reportDaySales.getVisaQty(),
                            reportDaySales.getMc() == null ? "0.00" : reportDaySales.getMc(),
                            reportDaySales.getMcQty() == null ? 0 : reportDaySales.getMcQty(),
                            reportDaySales.getAmex() == null ? "0.00" : reportDaySales.getAmex(),
                            reportDaySales.getAmexQty() == null ? 0 : reportDaySales.getAmexQty(),
                            reportDaySales.getJbl() == null ? "0.00" : reportDaySales.getJbl(),
                            reportDaySales.getJblQty() == null ? 0 : reportDaySales.getJblQty(),
                            reportDaySales.getUnionPay() == null ? "0.00" : reportDaySales.getUnionPay(),
                            reportDaySales.getUnionPayQty() == null ? 0 : reportDaySales.getUnionPayQty(),
                            reportDaySales.getDiner() == null ? "0.00" : reportDaySales.getDiner(),
                            reportDaySales.getDinerQty() == null ? 0 : reportDaySales.getDinerQty(),
                            reportDaySales.getHoldld() == null ? "0.00" : reportDaySales.getHoldld(),
                            reportDaySales.getHoldldQty() == null ? 0 : reportDaySales.getHoldldQty(),
                            reportDaySales.getTotalCard() == null ? "0.00" : reportDaySales.getTotalCard(),
                            reportDaySales.getTotalCardQty() == null ? 0 : reportDaySales.getTotalCardQty(),
                            reportDaySales.getTotalCash() == null ? "0.00" : reportDaySales.getTotalCash(),
                            reportDaySales.getTotalCashQty() == null ? 0 : reportDaySales.getTotalCashQty(),
                            reportDaySales.getBillVoid() == null ? "0.00" : reportDaySales.getBillVoid(),
                            reportDaySales.getBillVoidQty() == null ? 0 : reportDaySales.getBillVoidQty(),
                            reportDaySales.getItemVoid() == null ? "0.00" : reportDaySales.getItemVoid(),
                            reportDaySales.getItemVoidQty() == null ? 0 : reportDaySales.getItemVoidQty(),
                            reportDaySales.getNettSales() == null ? "0.00" : reportDaySales.getNettSales(),
                            reportDaySales.getTotalBills() == null ? "0.00" : reportDaySales.getTotalBills(),
                            reportDaySales.getOpenCount() == null ? 0 : reportDaySales.getOpenCount(),
                            reportDaySales.getFirstReceipt(),
                            reportDaySales.getLastReceipt(),
                            reportDaySales.getTotalTax() == null ? "0.00" : reportDaySales.getTotalTax(),
                            reportDaySales.getOrderQty() == null ? 0 : reportDaySales.getOrderQty(),
                            reportDaySales.getPersonQty() == null ? 0 : reportDaySales.getPersonQty(),
                            reportDaySales.getTotalBalancePrice() == null ? 0 : reportDaySales.getTotalBalancePrice(),
                            reportDaySales.getCashInAmt() == null ? "0.00" : reportDaySales.getCashInAmt(),
                            reportDaySales.getCashOutAmt() == null ? "0.00" : reportDaySales.getCashOutAmt(),
                            reportDaySales.getVarianceAmt() == null ? "0.00" : reportDaySales.getVarianceAmt(),
                            reportDaySales.getInclusiveTaxAmt() == null ? "0.00" : reportDaySales.getInclusiveTaxAmt(),
                            reportDaySales.getAlipay() == null ? "0.00" : reportDaySales.getAlipay(),
                            reportDaySales.getAlipayQty() == null ? 0 : reportDaySales.getAlipayQty(),
                            reportDaySales.getThirdParty() == null ? "0.00" : reportDaySales.getThirdParty(),
                            reportDaySales.getThirdPartyQty() == null ? 0 : reportDaySales.getThirdPartyQty(),
                            reportDaySales.getWeixinpay() == null ? "0.00" : reportDaySales.getWeixinpay(),
                            reportDaySales.getWeixinpayQty() == null ? 0 : reportDaySales.getWeixinpayQty(),
                            reportDaySales.getPaypalpay() == null ? "0.00" : reportDaySales.getPaypalpay(),
                            reportDaySales.getPaypalpayQty() == null ? 0 : reportDaySales.getPaypalpayQty(),
                            reportDaySales.getStoredCard() == null ? "0.00" : reportDaySales.getStoredCard(),
                            reportDaySales.getStoredCardQty() == null ? 0 : reportDaySales.getStoredCardQty(),
                            reportDaySales.getTopUps() == null ? "0.00" : reportDaySales.getTopUps(),
                            reportDaySales.getTopUpsQty() == null ? 0 : reportDaySales.getTopUpsQty(),
                            reportDaySales.getBillRefund() == null ? "0.00" : reportDaySales.getBillRefund(),
                            reportDaySales.getBillRefundQty() == null ? 0 : reportDaySales.getBillRefundQty(),
                            reportDaySales.getRefundTax() == null ? "0.00" : reportDaySales.getRefundTax(),
                            reportDaySales.getStartDrawerAmount() == null ? "0.00" : reportDaySales.getStartDrawerAmount(),
                            reportDaySales.getExpectedAmount() == null ? "0.00" : reportDaySales.getExpectedAmount(),
                            reportDaySales.getWaiterAmount() == null ? "0.00" : reportDaySales.getWaiterAmount(),
                            reportDaySales.getDifference() == null ? "0.00" : reportDaySales.getDifference(),
                            reportDaySales.getCashTopUp() == null ? "0.00" : reportDaySales.getCashTopUp(),
                            reportDaySales.getTakeawaySales() == null ? "0.00" : reportDaySales.getTakeawaySales(),
                            reportDaySales.getTakeawayTax() == null ? "0.00" : reportDaySales.getTakeawayTax(),
                            reportDaySales.getTakeawayQty() == null ? 0 : reportDaySales.getTakeawayQty(),
                            reportDaySales.getCreateTime(),
                            reportDaySales.getUpdateTime(),
                            reportDaySales.getDeliveroo() == null ? "0.00" : reportDaySales.getDeliveroo(),
                            reportDaySales.getDeliverooQty() == null ? 0 : reportDaySales.getDeliverooQty(),
                            reportDaySales.getUbereats() == null ? "0.00" : reportDaySales.getUbereats(),
                            reportDaySales.getUbereatsQty() == null ? 0 : reportDaySales.getUbereatsQty(),
                            reportDaySales.getFoodpanda() == null ? "0.00" : reportDaySales.getFoodpanda(),
                            reportDaySales.getFoodpandaQty() == null ? 0 : reportDaySales.getFoodpandaQty(),
                            reportDaySales.getVoucher() == null ? "0.00" : reportDaySales.getVoucher(),
                            reportDaySales.getVoucherQty() == null ? 0 : reportDaySales.getVoucherQty(),
                            reportDaySales.getTotalHour() == null ? "1.000" : reportDaySales.getTotalHour(),
                            reportDaySales.getReportNoStr(),
                            reportDaySales.getPayHalal() == null ? "0.00" : reportDaySales.getPayHalal(),
                            reportDaySales.getPayHalalQty() == null ? 0 : reportDaySales.getPayHalalQty(),
                            reportDaySales.getPromotionTotal() == null ? "0.00" : reportDaySales.getPromotionTotal(),
                            reportDaySales.getIpay88Wepay() == null ? "0.00" : reportDaySales.getIpay88Wepay(),
                            reportDaySales.getIpay88WepayQty() == null ? 0 : reportDaySales.getIpay88WepayQty(),
                            reportDaySales.getIpay88Alipay() == null ? "0.00" : reportDaySales.getIpay88Alipay(),
                            reportDaySales.getIpay88AlipayQty() == null ? 0 : reportDaySales.getIpay88AlipayQty(),
                            reportDaySales.getIpay88Boost() == null ? "0.00" : reportDaySales.getIpay88Boost(),
                            reportDaySales.getIpay88BoostQty() == null ? 0 : reportDaySales.getIpay88BoostQty(),
                            reportDaySales.getIpay88Mcash() == null ? "0.00" : reportDaySales.getIpay88Mcash(),
                            reportDaySales.getIpay88McashQty() == null ? 0 : reportDaySales.getIpay88McashQty(),
                            reportDaySales.getIpay88TouchnGo() == null ? "0.00" : reportDaySales.getIpay88TouchnGo(),
                            reportDaySales.getIpay88TouchnGoQty() == null ? 0 : reportDaySales.getIpay88TouchnGoQty(),
                            reportDaySales.getIpay88Unionpay() == null ? "0.00" : reportDaySales.getIpay88Unionpay(),
                            reportDaySales.getIpay88UnionpayQty() == null ? 0 : reportDaySales.getIpay88UnionpayQty(),
                            reportDaySales.getIpay88Mbb() == null ? "0.00" : reportDaySales.getIpay88Mbb(),
                            reportDaySales.getIpay88MbbQty() == null ? 0 : reportDaySales.getIpay88MbbQty(),
                            reportDaySales.getIpay88Cimb() == null ? "0.00" : reportDaySales.getIpay88Cimb(),
                            reportDaySales.getIpay88CimbQty() == null ? 0 : reportDaySales.getIpay88CimbQty(),
                            reportDaySales.getIpay88Grabpay() == null ? "0.00" : reportDaySales.getIpay88Grabpay(),
                            reportDaySales.getIpay88GrabpayQty() == null ? 0 : reportDaySales.getIpay88GrabpayQty(),
                            reportDaySales.getIpay88Nets() == null ? "0.00" : reportDaySales.getIpay88Nets(),
                            reportDaySales.getIpay88NetsQty() == null ? 0 : reportDaySales.getIpay88NetsQty(),
                            reportDaySales.getDaySalesRound(),
                            reportDaySales.getPromotionQty() == null ? "0.00" : reportDaySales.getPromotionQty()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addReportDaySales(SQLiteDatabase db, ReportDaySales reportDaySales) {
        if (reportDaySales == null)
            return;
        try {
            String sql = "replace into "
                    + TableNames.ReportDaySales
                    + "(id, restaurantId, restaurantName, revenueId, revenueName, businessDate, itemSales, itemSalesQty, discountPer, discountPerQty,"
                    + " discount, discountQty, discountAmt, focItem, focItemQty, focBill, focBillQty, totalSales, cash, cashQty, nets, netsQty, visa,"
                    + " visaQty, mc, mcQty, amex, amexQty, jbl, jblQty, unionPay, unionPayQty, diner, dinerQty, holdld, holdldQty, totalCard, totalCardQty,"
                    + " totalCash, totalCashQty, billVoid, billVoidQty, itemVoid, itemVoidQty, nettSales, totalBills, openCount, firstReceipt, lastReceipt,"
                    + " totalTax, orderQty, personQty, totalBalancePrice, cashInAmt, cashOutAmt, varianceAmt, inclusiveTaxAmt, alipay, alipayQty, thirdParty,"
                    + " thirdPartyQty, weixinpay, weixinpayQty, paypalpay, paypalpayQty, storedCard, storedCardQty, topUps, topUpsQty, billRefund, billRefundQty,"
                    + " refundTax, startDrawerAmount, expectedAmount, waiterAmount, difference, cashTopUp, takeawaySales, takeawayTax, takeawayQty, createTime,"
                    + " updateTime, deliveroo, deliverooQty, ubereats, ubereatsQty, foodpanda, foodpandaQty, voucher, voucherQty, totalHour, reportNoStr,"
                    + " payHalal,payHalalQty,promotionTotal, ipay88Wepay, ipay88WepayQty, ipay88Alipay, ipay88AlipayQty, ipay88Boost, ipay88BoostQty, ipay88Mcash, ipay88McashQty, ipay88TouchnGo, ipay88TouchnGoQty, ipay88Unionpay, ipay88UnionpayQty, ipay88Mbb, ipay88MbbQty, ipay88Cimb, ipay88CimbQty, ipay88Grabpay, ipay88GrabpayQty, ipay88Nets, ipay88NetsQty,daySalesRound,promotionQty)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(
                    sql,
                    new Object[]{reportDaySales.getId(),
                            reportDaySales.getRestaurantId(),
                            reportDaySales.getRestaurantName(),
                            reportDaySales.getRevenueId(),
                            reportDaySales.getRevenueName(),
                            reportDaySales.getBusinessDate(),
                            reportDaySales.getItemSales() == null ? "0.00" : reportDaySales.getItemSales(),
                            reportDaySales.getItemSalesQty() == null ? 0 : reportDaySales.getItemSalesQty(),
                            reportDaySales.getDiscountPer() == null ? "0.00" : reportDaySales.getDiscountPer(),
                            reportDaySales.getDiscountPerQty() == null ? 0 : reportDaySales.getDiscountPerQty(),
                            reportDaySales.getDiscount() == null ? 0 : reportDaySales.getDiscount(),
                            reportDaySales.getDiscountQty() == null ? 0 : reportDaySales.getDiscountQty(),
                            reportDaySales.getDiscountAmt() == null ? "0.00" : reportDaySales.getDiscountAmt(),
                            reportDaySales.getFocItem() == null ? "0.00" : reportDaySales.getFocItem(),
                            reportDaySales.getFocItemQty() == null ? 0 : reportDaySales.getFocItemQty(),
                            reportDaySales.getFocBill() == null ? "0.00" : reportDaySales.getFocBill(),
                            reportDaySales.getFocBillQty() == null ? 0 : reportDaySales.getFocBillQty(),
                            reportDaySales.getTotalSales() == null ? "0.00" : reportDaySales.getTotalSales(),
                            reportDaySales.getCash() == null ? "0.00" : reportDaySales.getCash(),
                            reportDaySales.getCashQty() == null ? 0 : reportDaySales.getCashQty(),
                            reportDaySales.getNets() == null ? "0.00" : reportDaySales.getNets(),
                            reportDaySales.getNetsQty() == null ? 0 : reportDaySales.getNetsQty(),
                            reportDaySales.getVisa() == null ? "0.00" : reportDaySales.getVisa(),
                            reportDaySales.getVisaQty() == null ? 0 : reportDaySales.getVisaQty(),
                            reportDaySales.getMc() == null ? "0.00" : reportDaySales.getMc(),
                            reportDaySales.getMcQty() == null ? 0 : reportDaySales.getMcQty(),
                            reportDaySales.getAmex() == null ? "0.00" : reportDaySales.getAmex(),
                            reportDaySales.getAmexQty() == null ? 0 : reportDaySales.getAmexQty(),
                            reportDaySales.getJbl() == null ? "0.00" : reportDaySales.getJbl(),
                            reportDaySales.getJblQty() == null ? 0 : reportDaySales.getJblQty(),
                            reportDaySales.getUnionPay() == null ? "0.00" : reportDaySales.getUnionPay(),
                            reportDaySales.getUnionPayQty() == null ? 0 : reportDaySales.getUnionPayQty(),
                            reportDaySales.getDiner() == null ? "0.00" : reportDaySales.getDiner(),
                            reportDaySales.getDinerQty() == null ? 0 : reportDaySales.getDinerQty(),
                            reportDaySales.getHoldld() == null ? "0.00" : reportDaySales.getHoldld(),
                            reportDaySales.getHoldldQty() == null ? 0 : reportDaySales.getHoldldQty(),
                            reportDaySales.getTotalCard() == null ? "0.00" : reportDaySales.getTotalCard(),
                            reportDaySales.getTotalCardQty() == null ? 0 : reportDaySales.getTotalCardQty(),
                            reportDaySales.getTotalCash() == null ? "0.00" : reportDaySales.getTotalCash(),
                            reportDaySales.getTotalCashQty() == null ? 0 : reportDaySales.getTotalCashQty(),
                            reportDaySales.getBillVoid() == null ? "0.00" : reportDaySales.getBillVoid(),
                            reportDaySales.getBillVoidQty() == null ? 0 : reportDaySales.getBillVoidQty(),
                            reportDaySales.getItemVoid() == null ? "0.00" : reportDaySales.getItemVoid(),
                            reportDaySales.getItemVoidQty() == null ? 0 : reportDaySales.getItemVoidQty(),
                            reportDaySales.getNettSales() == null ? "0.00" : reportDaySales.getNettSales(),
                            reportDaySales.getTotalBills() == null ? "0.00" : reportDaySales.getTotalBills(),
                            reportDaySales.getOpenCount() == null ? 0 : reportDaySales.getOpenCount(),
                            reportDaySales.getFirstReceipt(),
                            reportDaySales.getLastReceipt(),
                            reportDaySales.getTotalTax() == null ? "0.00" : reportDaySales.getTotalTax(),
                            reportDaySales.getOrderQty() == null ? 0 : reportDaySales.getOrderQty(),
                            reportDaySales.getPersonQty() == null ? 0 : reportDaySales.getPersonQty(),
                            reportDaySales.getTotalBalancePrice() == null ? 0 : reportDaySales.getTotalBalancePrice(),
                            reportDaySales.getCashInAmt() == null ? "0.00" : reportDaySales.getCashInAmt(),
                            reportDaySales.getCashOutAmt() == null ? "0.00" : reportDaySales.getCashOutAmt(),
                            reportDaySales.getVarianceAmt() == null ? "0.00" : reportDaySales.getVarianceAmt(),
                            reportDaySales.getInclusiveTaxAmt() == null ? "0.00" : reportDaySales.getInclusiveTaxAmt(),
                            reportDaySales.getAlipay() == null ? "0.00" : reportDaySales.getAlipay(),
                            reportDaySales.getAlipayQty() == null ? 0 : reportDaySales.getAlipayQty(),
                            reportDaySales.getThirdParty() == null ? "0.00" : reportDaySales.getThirdParty(),
                            reportDaySales.getThirdPartyQty() == null ? 0 : reportDaySales.getThirdPartyQty(),
                            reportDaySales.getWeixinpay() == null ? "0.00" : reportDaySales.getWeixinpay(),
                            reportDaySales.getWeixinpayQty() == null ? 0 : reportDaySales.getWeixinpayQty(),
                            reportDaySales.getPaypalpay() == null ? "0.00" : reportDaySales.getPaypalpay(),
                            reportDaySales.getPaypalpayQty() == null ? 0 : reportDaySales.getPaypalpayQty(),
                            reportDaySales.getStoredCard() == null ? "0.00" : reportDaySales.getStoredCard(),
                            reportDaySales.getStoredCardQty() == null ? 0 : reportDaySales.getStoredCardQty(),
                            reportDaySales.getTopUps() == null ? "0.00" : reportDaySales.getTopUps(),
                            reportDaySales.getTopUpsQty() == null ? 0 : reportDaySales.getTopUpsQty(),
                            reportDaySales.getBillRefund() == null ? "0.00" : reportDaySales.getBillRefund(),
                            reportDaySales.getBillRefundQty() == null ? 0 : reportDaySales.getBillRefundQty(),
                            reportDaySales.getRefundTax() == null ? "0.00" : reportDaySales.getRefundTax(),
                            reportDaySales.getStartDrawerAmount() == null ? "0.00" : reportDaySales.getStartDrawerAmount(),
                            reportDaySales.getExpectedAmount() == null ? "0.00" : reportDaySales.getExpectedAmount(),
                            reportDaySales.getWaiterAmount() == null ? "0.00" : reportDaySales.getWaiterAmount(),
                            reportDaySales.getDifference() == null ? "0.00" : reportDaySales.getDifference(),
                            reportDaySales.getCashTopUp() == null ? "0.00" : reportDaySales.getCashTopUp(),
                            reportDaySales.getTakeawaySales() == null ? "0.00" : reportDaySales.getTakeawaySales(),
                            reportDaySales.getTakeawayTax() == null ? "0.00" : reportDaySales.getTakeawayTax(),
                            reportDaySales.getTakeawayQty() == null ? 0 : reportDaySales.getTakeawayQty(),
                            reportDaySales.getCreateTime(),
                            reportDaySales.getUpdateTime(),
                            reportDaySales.getDeliveroo() == null ? "0.00" : reportDaySales.getDeliveroo(),
                            reportDaySales.getDeliverooQty() == null ? 0 : reportDaySales.getDeliverooQty(),
                            reportDaySales.getUbereats() == null ? "0.00" : reportDaySales.getUbereats(),
                            reportDaySales.getUbereatsQty() == null ? 0 : reportDaySales.getUbereatsQty(),
                            reportDaySales.getFoodpanda() == null ? "0.00" : reportDaySales.getFoodpanda(),
                            reportDaySales.getFoodpandaQty() == null ? 0 : reportDaySales.getFoodpandaQty(),
                            reportDaySales.getVoucher() == null ? "0.00" : reportDaySales.getVoucher(),
                            reportDaySales.getVoucherQty() == null ? 0 : reportDaySales.getVoucherQty(),
                            reportDaySales.getTotalHour() == null ? "1.000" : reportDaySales.getTotalHour(),
                            reportDaySales.getReportNoStr(),
                            reportDaySales.getPayHalal() == null ? "0.00" : reportDaySales.getPayHalal(),
                            reportDaySales.getPayHalalQty() == null ? 0 : reportDaySales.getPayHalalQty(),
                            reportDaySales.getPromotionTotal() == null ? "0.00" : reportDaySales.getPromotionTotal(),
                            reportDaySales.getIpay88Wepay() == null ? "0.00" : reportDaySales.getIpay88Wepay(),
                            reportDaySales.getIpay88WepayQty() == null ? 0 : reportDaySales.getIpay88WepayQty(),
                            reportDaySales.getIpay88Alipay() == null ? "0.00" : reportDaySales.getIpay88Alipay(),
                            reportDaySales.getIpay88AlipayQty() == null ? 0 : reportDaySales.getIpay88AlipayQty(),
                            reportDaySales.getIpay88Boost() == null ? "0.00" : reportDaySales.getIpay88Boost(),
                            reportDaySales.getIpay88BoostQty() == null ? 0 : reportDaySales.getIpay88BoostQty(),
                            reportDaySales.getIpay88Mcash() == null ? "0.00" : reportDaySales.getIpay88Mcash(),
                            reportDaySales.getIpay88McashQty() == null ? 0 : reportDaySales.getIpay88McashQty(),
                            reportDaySales.getIpay88TouchnGo() == null ? "0.00" : reportDaySales.getIpay88TouchnGo(),
                            reportDaySales.getIpay88TouchnGoQty() == null ? 0 : reportDaySales.getIpay88TouchnGoQty(),
                            reportDaySales.getIpay88Unionpay() == null ? "0.00" : reportDaySales.getIpay88Unionpay(),
                            reportDaySales.getIpay88UnionpayQty() == null ? 0 : reportDaySales.getIpay88UnionpayQty(),
                            reportDaySales.getIpay88Mbb() == null ? "0.00" : reportDaySales.getIpay88Mbb(),
                            reportDaySales.getIpay88MbbQty() == null ? 0 : reportDaySales.getIpay88MbbQty(),
                            reportDaySales.getIpay88Cimb() == null ? "0.00" : reportDaySales.getIpay88Cimb(),
                            reportDaySales.getIpay88CimbQty() == null ? 0 : reportDaySales.getIpay88CimbQty(),
                            reportDaySales.getIpay88Grabpay() == null ? "0.00" : reportDaySales.getIpay88Grabpay(),
                            reportDaySales.getIpay88GrabpayQty() == null ? 0 : reportDaySales.getIpay88GrabpayQty(),
                            reportDaySales.getIpay88Nets() == null ? "0.00" : reportDaySales.getIpay88Nets(),
                            reportDaySales.getIpay88NetsQty() == null ? 0 : reportDaySales.getIpay88NetsQty(),
                            reportDaySales.getDaySalesRound(),
                            reportDaySales.getPromotionQty() == null ? "0.00" : reportDaySales.getPromotionQty()

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    /*ONLY use to save report downloaded from cloud*/
    public static void addReportDaySalesFromCloud(ReportDaySales reportDaySales) {
        if (reportDaySales == null)
            return;
        try {
            /**
             * private String deliveroo;
             private Integer deliverooQty;
             private String ubereats;
             private Integer ubereatsQty;
             private String foodpanda;
             private Integer foodpandaQty;
             private String voucher;
             private Integer voucherQty;
             */
            String sql = "replace into "
                    + TableNames.ReportDaySales
                    + "(restaurantId, restaurantName, revenueId, revenueName, businessDate, itemSales, itemSalesQty, discountPer, discountPerQty,"
                    + " discount, discountQty, discountAmt, focItem, focItemQty, focBill, focBillQty, totalSales, cash, cashQty, nets, netsQty,"
                    + " visa, visaQty, mc, mcQty, amex, amexQty, jbl, jblQty, unionPay, unionPayQty, diner, dinerQty, holdld, holdldQty, totalCard,"
                    + " totalCardQty, totalCash, totalCashQty, billVoid, billVoidQty, itemVoid, itemVoidQty, nettSales, totalBills, openCount,"
                    + " firstReceipt, lastReceipt, totalTax, orderQty, personQty, totalBalancePrice, cashInAmt, cashOutAmt, varianceAmt, inclusiveTaxAmt,"
                    + " alipay, alipayQty, thirdParty, thirdPartyQty, weixinpay, weixinpayQty, paypalpay, paypalpayQty, storedCard, storedCardQty, topUps, topUpsQty,"
                    + " billRefund, billRefundQty, refundTax, startDrawerAmount, expectedAmount, waiterAmount, difference, cashTopUp, takeawaySales, takeawayTax, "
                    + " takeawayQty, createTime, updateTime, deliveroo, deliverooQty, ubereats, ubereatsQty, foodpanda, foodpandaQty, voucher, voucherQty,totalHour, reportNoStr,"
                    + " payHalal,payHalalQty,promotionTotal, ipay88Wepay, ipay88WepayQty, ipay88Alipay, ipay88AlipayQty, ipay88Boost, ipay88BoostQty, ipay88Mcash, ipay88McashQty, ipay88TouchnGo, ipay88TouchnGoQty, ipay88Unionpay, ipay88UnionpayQty, ipay88Mbb, ipay88MbbQty, ipay88Cimb, ipay88CimbQty, ipay88Grabpay, ipay88GrabpayQty, ipay88Nets, ipay88NetsQty,daySalesRound,promotionQty)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{reportDaySales.getRestaurantId(),
                            reportDaySales.getRestaurantName(),
                            reportDaySales.getRevenueId(),
                            reportDaySales.getRevenueName(),
                            reportDaySales.getBusinessDate(),
                            reportDaySales.getItemSales() == null ? "0.00" : reportDaySales.getItemSales(),
                            reportDaySales.getItemSalesQty() == null ? 0 : reportDaySales.getItemSalesQty(),
                            reportDaySales.getDiscountPer() == null ? "0.00" : reportDaySales.getDiscountPer(),
                            reportDaySales.getDiscountPerQty() == null ? 0 : reportDaySales.getDiscountPerQty(),
                            reportDaySales.getDiscount() == null ? 0 : reportDaySales.getDiscount(),
                            reportDaySales.getDiscountQty() == null ? 0 : reportDaySales.getDiscountQty(),
                            reportDaySales.getDiscountAmt() == null ? "0.00" : reportDaySales.getDiscountAmt(),
                            reportDaySales.getFocItem() == null ? "0.00" : reportDaySales.getFocItem(),
                            reportDaySales.getFocItemQty() == null ? 0 : reportDaySales.getFocItemQty(),
                            reportDaySales.getFocBill() == null ? "0.00" : reportDaySales.getFocBill(),
                            reportDaySales.getFocBillQty() == null ? 0 : reportDaySales.getFocBillQty(),
                            reportDaySales.getTotalSales() == null ? "0.00" : reportDaySales.getTotalSales(),
                            reportDaySales.getCash() == null ? "0.00" : reportDaySales.getCash(),
                            reportDaySales.getCashQty() == null ? 0 : reportDaySales.getCashQty(),
                            reportDaySales.getNets() == null ? "0.00" : reportDaySales.getNets(),
                            reportDaySales.getNetsQty() == null ? 0 : reportDaySales.getNetsQty(),
                            reportDaySales.getVisa() == null ? "0.00" : reportDaySales.getVisa(),
                            reportDaySales.getVisaQty() == null ? 0 : reportDaySales.getVisaQty(),
                            reportDaySales.getMc() == null ? "0.00" : reportDaySales.getMc(),
                            reportDaySales.getMcQty() == null ? 0 : reportDaySales.getMcQty(),
                            reportDaySales.getAmex() == null ? "0.00" : reportDaySales.getAmex(),
                            reportDaySales.getAmexQty() == null ? 0 : reportDaySales.getAmexQty(),
                            reportDaySales.getJbl() == null ? "0.00" : reportDaySales.getJbl(),
                            reportDaySales.getJblQty() == null ? 0 : reportDaySales.getJblQty(),
                            reportDaySales.getUnionPay() == null ? "0.00" : reportDaySales.getUnionPay(),
                            reportDaySales.getUnionPayQty() == null ? 0 : reportDaySales.getUnionPayQty(),
                            reportDaySales.getDiner() == null ? "0.00" : reportDaySales.getDiner(),
                            reportDaySales.getDinerQty() == null ? 0 : reportDaySales.getDinerQty(),
                            reportDaySales.getHoldld() == null ? "0.00" : reportDaySales.getHoldld(),
                            reportDaySales.getHoldldQty() == null ? 0 : reportDaySales.getHoldldQty(),
                            reportDaySales.getTotalCard() == null ? "0.00" : reportDaySales.getTotalCard(),
                            reportDaySales.getTotalCardQty() == null ? 0 : reportDaySales.getTotalCardQty(),
                            reportDaySales.getTotalCash() == null ? "0.00" : reportDaySales.getTotalCash(),
                            reportDaySales.getTotalCashQty() == null ? 0 : reportDaySales.getTotalCashQty(),
                            reportDaySales.getBillVoid() == null ? "0.00" : reportDaySales.getBillVoid(),
                            reportDaySales.getBillVoidQty() == null ? 0 : reportDaySales.getBillVoidQty(),
                            reportDaySales.getItemVoid() == null ? "0.00" : reportDaySales.getItemVoid(),
                            reportDaySales.getItemVoidQty() == null ? 0 : reportDaySales.getItemVoidQty(),
                            reportDaySales.getNettSales() == null ? "0.00" : reportDaySales.getNettSales(),
                            reportDaySales.getTotalBills() == null ? "0.00" : reportDaySales.getTotalBills(),
                            reportDaySales.getOpenCount() == null ? 0 : reportDaySales.getOpenCount(),
                            reportDaySales.getFirstReceipt(),
                            reportDaySales.getLastReceipt(),
                            reportDaySales.getTotalTax() == null ? "0.00" : reportDaySales.getTotalTax(),
                            reportDaySales.getOrderQty() == null ? 0 : reportDaySales.getOrderQty(),
                            reportDaySales.getPersonQty() == null ? 0 : reportDaySales.getPersonQty(),
                            reportDaySales.getTotalBalancePrice() == null ? 0 : reportDaySales.getTotalBalancePrice(),
                            reportDaySales.getCashInAmt() == null ? "0.00" : reportDaySales.getCashInAmt(),
                            reportDaySales.getCashOutAmt() == null ? "0.00" : reportDaySales.getCashOutAmt(),
                            reportDaySales.getVarianceAmt() == null ? "0.00" : reportDaySales.getVarianceAmt(),
                            reportDaySales.getInclusiveTaxAmt() == null ? "0.00" : reportDaySales.getInclusiveTaxAmt(),
                            reportDaySales.getAlipay() == null ? "0.00" : reportDaySales.getAlipay(),
                            reportDaySales.getAlipayQty() == null ? 0 : reportDaySales.getAlipayQty(),
                            reportDaySales.getThirdParty() == null ? "0.00" : reportDaySales.getThirdParty(),
                            reportDaySales.getThirdPartyQty() == null ? 0 : reportDaySales.getThirdPartyQty(),
                            reportDaySales.getWeixinpay() == null ? "0.00" : reportDaySales.getWeixinpay(),
                            reportDaySales.getWeixinpayQty() == null ? 0 : reportDaySales.getWeixinpayQty(),
                            reportDaySales.getPaypalpay() == null ? "0.00" : reportDaySales.getPaypalpay(),
                            reportDaySales.getPaypalpayQty() == null ? 0 : reportDaySales.getPaypalpayQty(),
                            reportDaySales.getStoredCard() == null ? "0.00" : reportDaySales.getStoredCard(),
                            reportDaySales.getStoredCardQty() == null ? 0 : reportDaySales.getStoredCardQty(),
                            reportDaySales.getTopUps() == null ? "0.00" : reportDaySales.getTopUps(),
                            reportDaySales.getTopUpsQty() == null ? 0 : reportDaySales.getTopUpsQty(),
                            reportDaySales.getBillRefund() == null ? "0.00" : reportDaySales.getBillRefund(),
                            reportDaySales.getBillRefundQty() == null ? 0 : reportDaySales.getBillRefundQty(),
                            reportDaySales.getRefundTax() == null ? "0.00" : reportDaySales.getRefundTax(),
                            reportDaySales.getStartDrawerAmount() == null ? "0.00" : reportDaySales.getStartDrawerAmount(),
                            reportDaySales.getExpectedAmount() == null ? "0.00" : reportDaySales.getExpectedAmount(),
                            reportDaySales.getWaiterAmount() == null ? "0.00" : reportDaySales.getWaiterAmount(),
                            reportDaySales.getDifference() == null ? "0.00" : reportDaySales.getDifference(),
                            reportDaySales.getCashTopUp() == null ? "0.00" : reportDaySales.getCashTopUp(),
                            reportDaySales.getTakeawaySales() == null ? "0.00" : reportDaySales.getTakeawaySales(),
                            reportDaySales.getTakeawayTax() == null ? "0.00" : reportDaySales.getTakeawayTax(),
                            reportDaySales.getTakeawayQty() == null ? 0 : reportDaySales.getTakeawayQty(),
                            reportDaySales.getCreateTime(),
                            reportDaySales.getUpdateTime(),
                            reportDaySales.getDeliveroo() == null ? "0.00" : reportDaySales.getDeliveroo(),
                            reportDaySales.getDeliverooQty() == null ? 0 : reportDaySales.getDeliverooQty(),
                            reportDaySales.getUbereats() == null ? "0.00" : reportDaySales.getUbereats(),
                            reportDaySales.getUbereatsQty() == null ? 0 : reportDaySales.getUbereatsQty(),
                            reportDaySales.getFoodpanda() == null ? "0.00" : reportDaySales.getFoodpanda(),
                            reportDaySales.getFoodpandaQty() == null ? 0 : reportDaySales.getFoodpandaQty(),
                            reportDaySales.getVoucher() == null ? "0.00" : reportDaySales.getVoucher(),
                            reportDaySales.getVoucherQty() == null ? 0 : reportDaySales.getVoucherQty(),
                            reportDaySales.getTotalHour() == null ? "1.000" : reportDaySales.getTotalHour(),
                            reportDaySales.getReportNoStr(),
                            reportDaySales.getPayHalal() == null ? "0.00" : reportDaySales.getPayHalal(),
                            reportDaySales.getPayHalalQty() == null ? 0 : reportDaySales.getPayHalalQty(),
                            reportDaySales.getPromotionTotal() == null ? "0.00" : reportDaySales.getPromotionTotal(),
                            reportDaySales.getIpay88Wepay() == null ? "0.00" : reportDaySales.getIpay88Wepay(),
                            reportDaySales.getIpay88WepayQty() == null ? 0 : reportDaySales.getIpay88WepayQty(),
                            reportDaySales.getIpay88Alipay() == null ? "0.00" : reportDaySales.getIpay88Alipay(),
                            reportDaySales.getIpay88AlipayQty() == null ? 0 : reportDaySales.getIpay88AlipayQty(),
                            reportDaySales.getIpay88Boost() == null ? "0.00" : reportDaySales.getIpay88Boost(),
                            reportDaySales.getIpay88BoostQty() == null ? 0 : reportDaySales.getIpay88BoostQty(),
                            reportDaySales.getIpay88Mcash() == null ? "0.00" : reportDaySales.getIpay88Mcash(),
                            reportDaySales.getIpay88McashQty() == null ? 0 : reportDaySales.getIpay88McashQty(),
                            reportDaySales.getIpay88TouchnGo() == null ? "0.00" : reportDaySales.getIpay88TouchnGo(),
                            reportDaySales.getIpay88TouchnGoQty() == null ? 0 : reportDaySales.getIpay88TouchnGoQty(),
                            reportDaySales.getIpay88Unionpay() == null ? "0.00" : reportDaySales.getIpay88Unionpay(),
                            reportDaySales.getIpay88UnionpayQty() == null ? 0 : reportDaySales.getIpay88UnionpayQty(),
                            reportDaySales.getIpay88Mbb() == null ? "0.00" : reportDaySales.getIpay88Mbb(),
                            reportDaySales.getIpay88MbbQty() == null ? 0 : reportDaySales.getIpay88MbbQty(),
                            reportDaySales.getIpay88Cimb() == null ? "0.00" : reportDaySales.getIpay88Cimb(),
                            reportDaySales.getIpay88CimbQty() == null ? 0 : reportDaySales.getIpay88CimbQty(),
                            reportDaySales.getIpay88Grabpay() == null ? "0.00" : reportDaySales.getIpay88Grabpay(),
                            reportDaySales.getIpay88GrabpayQty() == null ? 0 : reportDaySales.getIpay88GrabpayQty(),
                            reportDaySales.getIpay88Nets() == null ? "0.00" : reportDaySales.getIpay88Nets(),
                            reportDaySales.getIpay88NetsQty() == null ? 0 : reportDaySales.getIpay88NetsQty(),
                            reportDaySales.getDaySalesRound(),
                            reportDaySales.getPromotionQty() == null ? "0.00" : reportDaySales.getPromotionQty()

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ReportDaySales getReportDaySales(Integer reportDaySalesID) {
        ReportDaySales reportDaySales = null;
        String sql = "select * from " + TableNames.ReportDaySales
                + " where id = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{reportDaySalesID + ""});
            if (cursor.moveToFirst()) {
                reportDaySales = new ReportDaySales();
                reportDaySales.setId(cursor.getInt(0));
                reportDaySales.setRestaurantId(cursor.getInt(1));
                reportDaySales.setRestaurantName(cursor.getString(2));
                reportDaySales.setRevenueId(cursor.getInt(3));
                reportDaySales.setRevenueName(cursor.getString(4));
                reportDaySales.setBusinessDate(cursor.getLong(5));
                reportDaySales.setItemSales(cursor.getString(6));
                reportDaySales.setItemSalesQty(cursor.getInt(7));
                reportDaySales.setDiscountPer(cursor.getString(8));
                reportDaySales.setDiscountPerQty(cursor.getInt(9));
                reportDaySales.setDiscount(cursor.getString(10));
                reportDaySales.setDiscountQty(cursor.getInt(11));
                reportDaySales.setDiscountAmt(cursor.getString(12));
                reportDaySales.setFocItem(cursor.getString(13));
                reportDaySales.setFocItemQty(cursor.getInt(14));
                reportDaySales.setFocBill(cursor.getString(15));
                reportDaySales.setFocBillQty(cursor.getInt(16));
                reportDaySales.setTotalSales(cursor.getString(17));
                reportDaySales.setCash(cursor.getString(18));
                reportDaySales.setCashQty(cursor.getInt(19));
                reportDaySales.setNets(cursor.getString(20));
                reportDaySales.setNetsQty(cursor.getInt(21));
                reportDaySales.setVisa(cursor.getString(22));
                reportDaySales.setVisaQty(cursor.getInt(23));
                reportDaySales.setMc(cursor.getString(24));
                reportDaySales.setMcQty(cursor.getInt(25));
                reportDaySales.setAmex(cursor.getString(26));
                reportDaySales.setAmexQty(cursor.getInt(27));
                reportDaySales.setJbl(cursor.getString(28));
                reportDaySales.setJblQty(cursor.getInt(29));
                reportDaySales.setUnionPay(cursor.getString(30));
                reportDaySales.setUnionPayQty(cursor.getInt(31));
                reportDaySales.setDiner(cursor.getString(32));
                reportDaySales.setDinerQty(cursor.getInt(33));
                reportDaySales.setHoldld(cursor.getString(34));
                reportDaySales.setHoldldQty(cursor.getInt(35));
                reportDaySales.setTotalCard(cursor.getString(36));
                reportDaySales.setTotalCardQty(cursor.getInt(37));
                reportDaySales.setTotalCash(cursor.getString(38));
                reportDaySales.setTotalCashQty(cursor.getInt(39));
                reportDaySales.setBillVoid(cursor.getString(40));
                reportDaySales.setBillVoidQty(cursor.getInt(41));
                reportDaySales.setItemVoid(cursor.getString(42));
                reportDaySales.setItemVoidQty(cursor.getInt(43));
                reportDaySales.setNettSales(cursor.getString(44));
                reportDaySales.setTotalBills(cursor.getInt(45));
                reportDaySales.setOpenCount(cursor.getInt(46));
                reportDaySales.setFirstReceipt(cursor.getInt(47));
                reportDaySales.setLastReceipt(cursor.getInt(48));
                reportDaySales.setTotalTax(cursor.getString(49));
                reportDaySales.setOrderQty(cursor.getInt(50));
                reportDaySales.setPersonQty(cursor.getInt(51));
                reportDaySales.setTotalBalancePrice(cursor.getString(52));
                reportDaySales.setCashInAmt(cursor.getString(53));
                reportDaySales.setCashOutAmt(cursor.getString(54));
                reportDaySales.setVarianceAmt(cursor.getString(55));
                reportDaySales.setInclusiveTaxAmt(cursor.getString(56));
                reportDaySales.setAlipay(cursor.getString(57));
                reportDaySales.setAlipayQty(cursor.getInt(58));
                reportDaySales.setThirdParty(cursor.getString(59));
                reportDaySales.setThirdPartyQty(cursor.getInt(60));
                reportDaySales.setWeixinpay(cursor.getString(61));
                reportDaySales.setWeixinpayQty(cursor.getInt(62));
                reportDaySales.setPaypalpay(cursor.getString(63));
                reportDaySales.setPaypalpayQty(cursor.getInt(64));
                reportDaySales.setStoredCard(cursor.getString(65));
                reportDaySales.setStoredCardQty(cursor.getInt(66));
                reportDaySales.setTopUps(cursor.getString(67));
                reportDaySales.setTopUpsQty(cursor.getInt(68));
                reportDaySales.setBillRefund(cursor.getString(69));
                reportDaySales.setBillRefundQty(cursor.getInt(70));
                reportDaySales.setRefundTax(cursor.getString(71));
                reportDaySales.setStartDrawerAmount(cursor.getString(72));
                reportDaySales.setExpectedAmount(cursor.getString(73));
                reportDaySales.setWaiterAmount(cursor.getString(74));
                reportDaySales.setDifference(cursor.getString(75));
                reportDaySales.setCashTopUp(cursor.getString(76));
                reportDaySales.setTakeawaySales(cursor.getString(77));
                reportDaySales.setTakeawayTax(cursor.getString(78));
                reportDaySales.setTakeawayQty(cursor.getInt(79));
                reportDaySales.setCreateTime(cursor.getLong(80));
                reportDaySales.setUpdateTime(cursor.getLong(81));
                reportDaySales.setDeliveroo(cursor.getString(82));
                reportDaySales.setDeliverooQty(cursor.getInt(83));
                reportDaySales.setUbereats(cursor.getString(84));
                reportDaySales.setUbereatsQty(cursor.getInt(85));
                reportDaySales.setFoodpanda(cursor.getString(86));
                reportDaySales.setFoodpandaQty(cursor.getInt(87));
                reportDaySales.setVoucher(cursor.getString(88));
                reportDaySales.setVoucherQty(cursor.getInt(89));
                reportDaySales.setTotalHour(cursor.getString(90));
                reportDaySales.setReportNoStr(cursor.getString(91));
                reportDaySales.setPayHalal(cursor.getString(92));
                reportDaySales.setPayHalalQty(cursor.getInt(93));
                reportDaySales.setPromotionTotal(cursor.getString(94));
                reportDaySales.setIpay88Wepay(cursor.getString(95));
                reportDaySales.setIpay88WepayQty(cursor.getInt(96));
                reportDaySales.setIpay88Alipay(cursor.getString(97));
                reportDaySales.setIpay88AlipayQty(cursor.getInt(98));
                reportDaySales.setIpay88Boost(cursor.getString(99));
                reportDaySales.setIpay88BoostQty(cursor.getInt(100));
                reportDaySales.setIpay88Mcash(cursor.getString(101));
                reportDaySales.setIpay88McashQty(cursor.getInt(102));
                reportDaySales.setIpay88TouchnGo(cursor.getString(103));
                reportDaySales.setIpay88TouchnGoQty(cursor.getInt(104));
                reportDaySales.setIpay88Unionpay(cursor.getString(105));
                reportDaySales.setIpay88UnionpayQty(cursor.getInt(106));
                reportDaySales.setIpay88Mbb(cursor.getString(107));
                reportDaySales.setIpay88MbbQty(cursor.getInt(108));
                reportDaySales.setIpay88Cimb(cursor.getString(109));
                reportDaySales.setIpay88CimbQty(cursor.getInt(110));
                reportDaySales.setIpay88Grabpay(cursor.getString(111));
                reportDaySales.setIpay88GrabpayQty(cursor.getInt(112));
                reportDaySales.setIpay88Nets(cursor.getString(113));
                reportDaySales.setIpay88NetsQty(cursor.getInt(114));
                reportDaySales.setDaySalesRound(cursor.getString(115));
                reportDaySales.setPromotionQty(cursor.getInt(116));
				return reportDaySales;
			}
		} catch (Exception e) {
			e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return reportDaySales;
    }

    public static ReportDaySales getReportDaySalesByTime(long day) {
        ReportDaySales reportDaySales = null;
        String sql = "select * from " + TableNames.ReportDaySales
                + " where businessDate = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{String.valueOf(day)});
            if (cursor.moveToFirst()) {
                reportDaySales = new ReportDaySales();
                reportDaySales.setId(cursor.getInt(0));
                reportDaySales.setRestaurantId(cursor.getInt(1));
                reportDaySales.setRestaurantName(cursor.getString(2));
                reportDaySales.setRevenueId(cursor.getInt(3));
                reportDaySales.setRevenueName(cursor.getString(4));
                reportDaySales.setBusinessDate(cursor.getLong(5));
                reportDaySales.setItemSales(cursor.getString(6));
                reportDaySales.setItemSalesQty(cursor.getInt(7));
                reportDaySales.setDiscountPer(cursor.getString(8));
                reportDaySales.setDiscountPerQty(cursor.getInt(9));
                reportDaySales.setDiscount(cursor.getString(10));
                reportDaySales.setDiscountQty(cursor.getInt(11));
                reportDaySales.setDiscountAmt(cursor.getString(12));
                reportDaySales.setFocItem(cursor.getString(13));
                reportDaySales.setFocItemQty(cursor.getInt(14));
                reportDaySales.setFocBill(cursor.getString(15));
                reportDaySales.setFocBillQty(cursor.getInt(16));
                reportDaySales.setTotalSales(cursor.getString(17));
                reportDaySales.setCash(cursor.getString(18));
                reportDaySales.setCashQty(cursor.getInt(19));
                reportDaySales.setNets(cursor.getString(20));
                reportDaySales.setNetsQty(cursor.getInt(21));
                reportDaySales.setVisa(cursor.getString(22));
                reportDaySales.setVisaQty(cursor.getInt(23));
                reportDaySales.setMc(cursor.getString(24));
                reportDaySales.setMcQty(cursor.getInt(25));
                reportDaySales.setAmex(cursor.getString(26));
                reportDaySales.setAmexQty(cursor.getInt(27));
                reportDaySales.setJbl(cursor.getString(28));
                reportDaySales.setJblQty(cursor.getInt(29));
                reportDaySales.setUnionPay(cursor.getString(30));
                reportDaySales.setUnionPayQty(cursor.getInt(31));
                reportDaySales.setDiner(cursor.getString(32));
                reportDaySales.setDinerQty(cursor.getInt(33));
                reportDaySales.setHoldld(cursor.getString(34));
                reportDaySales.setHoldldQty(cursor.getInt(35));
                reportDaySales.setTotalCard(cursor.getString(36));
                reportDaySales.setTotalCardQty(cursor.getInt(37));
                reportDaySales.setTotalCash(cursor.getString(38));
                reportDaySales.setTotalCashQty(cursor.getInt(39));
                reportDaySales.setBillVoid(cursor.getString(40));
                reportDaySales.setBillVoidQty(cursor.getInt(41));
                reportDaySales.setItemVoid(cursor.getString(42));
                reportDaySales.setItemVoidQty(cursor.getInt(43));
                reportDaySales.setNettSales(cursor.getString(44));
                reportDaySales.setTotalBills(cursor.getInt(45));
                reportDaySales.setOpenCount(cursor.getInt(46));
                reportDaySales.setFirstReceipt(cursor.getInt(47));
                reportDaySales.setLastReceipt(cursor.getInt(48));
                reportDaySales.setTotalTax(cursor.getString(49));
                reportDaySales.setOrderQty(cursor.getInt(50));
                reportDaySales.setPersonQty(cursor.getInt(51));
                reportDaySales.setTotalBalancePrice(cursor.getString(52));
                reportDaySales.setCashInAmt(cursor.getString(53));
                reportDaySales.setCashOutAmt(cursor.getString(54));
                reportDaySales.setVarianceAmt(cursor.getString(55));
                reportDaySales.setInclusiveTaxAmt(cursor.getString(56));
                reportDaySales.setAlipay(cursor.getString(57));
                reportDaySales.setAlipayQty(cursor.getInt(58));
                reportDaySales.setThirdParty(cursor.getString(59));
                reportDaySales.setThirdPartyQty(cursor.getInt(60));
                reportDaySales.setWeixinpay(cursor.getString(61));
                reportDaySales.setWeixinpayQty(cursor.getInt(62));
                reportDaySales.setPaypalpay(cursor.getString(63));
                reportDaySales.setPaypalpayQty(cursor.getInt(64));
                reportDaySales.setStoredCard(cursor.getString(65));
                reportDaySales.setStoredCardQty(cursor.getInt(66));
                reportDaySales.setTopUps(cursor.getString(67));
                reportDaySales.setTopUpsQty(cursor.getInt(68));
                reportDaySales.setBillRefund(cursor.getString(69));
                reportDaySales.setBillRefundQty(cursor.getInt(70));
                reportDaySales.setRefundTax(cursor.getString(71));
                reportDaySales.setStartDrawerAmount(cursor.getString(72));
                reportDaySales.setExpectedAmount(cursor.getString(73));
                reportDaySales.setWaiterAmount(cursor.getString(74));
                reportDaySales.setDifference(cursor.getString(75));
                reportDaySales.setCashTopUp(cursor.getString(76));
                reportDaySales.setTakeawaySales(cursor.getString(77));
                reportDaySales.setTakeawayTax(cursor.getString(78));
                reportDaySales.setTakeawayQty(cursor.getInt(79));
                reportDaySales.setCreateTime(cursor.getLong(80));
                reportDaySales.setUpdateTime(cursor.getLong(81));
                reportDaySales.setDeliveroo(cursor.getString(82));
                reportDaySales.setDeliverooQty(cursor.getInt(83));
                reportDaySales.setUbereats(cursor.getString(84));
                reportDaySales.setUbereatsQty(cursor.getInt(85));
                reportDaySales.setFoodpanda(cursor.getString(86));
                reportDaySales.setFoodpandaQty(cursor.getInt(87));
                reportDaySales.setVoucher(cursor.getString(88));
                reportDaySales.setVoucherQty(cursor.getInt(89));
                reportDaySales.setTotalHour(cursor.getString(90));
                reportDaySales.setReportNoStr(cursor.getString(91));
                reportDaySales.setPayHalal(cursor.getString(92));
                reportDaySales.setPayHalalQty(cursor.getInt(93));
                reportDaySales.setPromotionTotal(cursor.getString(94));

                reportDaySales.setIpay88Wepay(cursor.getString(95));
                reportDaySales.setIpay88WepayQty(cursor.getInt(96));
                reportDaySales.setIpay88Alipay(cursor.getString(97));
                reportDaySales.setIpay88AlipayQty(cursor.getInt(98));
                reportDaySales.setIpay88Boost(cursor.getString(99));
                reportDaySales.setIpay88BoostQty(cursor.getInt(100));
                reportDaySales.setIpay88Mcash(cursor.getString(101));
                reportDaySales.setIpay88McashQty(cursor.getInt(102));
                reportDaySales.setIpay88TouchnGo(cursor.getString(103));
                reportDaySales.setIpay88TouchnGoQty(cursor.getInt(104));
                reportDaySales.setIpay88Unionpay(cursor.getString(105));
                reportDaySales.setIpay88UnionpayQty(cursor.getInt(106));
                reportDaySales.setIpay88Mbb(cursor.getString(107));
                reportDaySales.setIpay88MbbQty(cursor.getInt(108));
                reportDaySales.setIpay88Cimb(cursor.getString(109));
                reportDaySales.setIpay88CimbQty(cursor.getInt(110));
                reportDaySales.setIpay88Grabpay(cursor.getString(111));
                reportDaySales.setIpay88GrabpayQty(cursor.getInt(112));
                reportDaySales.setIpay88Nets(cursor.getString(113));
                reportDaySales.setIpay88NetsQty(cursor.getInt(114));
                reportDaySales.setDaySalesRound(cursor.getString(115));
                reportDaySales.setPromotionQty(cursor.getInt(116));

				return reportDaySales;
			}
		} catch (Exception e) {
			e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return reportDaySales;
    }

    //for X/Z Report Feature only
    /*
     *  bizDate: {'x':nettsales, "z": nettsales}
     *
     * */
    public static Map<Long, Object> getReportDaySalesBetweenTime(long currentTime, long oldTime) {
        ArrayList<ReportDaySales> result = new ArrayList<ReportDaySales>();
        String sql = "select nettSales, businessDate from " + TableNames.ReportDaySales
                + " where businessDate <= ? AND businessDate >=?";
        Cursor cursor = null;
        Map<Long, Object> zReportSummary = new HashMap<Long, Object>();
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{String.valueOf(currentTime), String.valueOf(oldTime)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> summyObj = new HashMap<String, String>();
                summyObj.put("z", cursor.getString(0)); // NETT Sales colomn
                summyObj.put("x", null);
                zReportSummary.put(cursor.getLong(1), summyObj); //Business Date
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return zReportSummary;
    }

    public static ArrayList<ReportDaySales> getAllReportDaySales() {
        ArrayList<ReportDaySales> result = new ArrayList<ReportDaySales>();
        String sql = "select * from " + TableNames.ReportDaySales;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                ReportDaySales reportDaySales = new ReportDaySales();
                reportDaySales.setId(cursor.getInt(0));
                reportDaySales.setRestaurantId(cursor.getInt(1));
                reportDaySales.setRestaurantName(cursor.getString(2));
                reportDaySales.setRevenueId(cursor.getInt(3));
                reportDaySales.setRevenueName(cursor.getString(4));
                reportDaySales.setBusinessDate(cursor.getLong(5));
                reportDaySales.setItemSales(cursor.getString(6));
                reportDaySales.setItemSalesQty(cursor.getInt(7));
                reportDaySales.setDiscountPer(cursor.getString(8));
                reportDaySales.setDiscountPerQty(cursor.getInt(9));
                reportDaySales.setDiscount(cursor.getString(10));
                reportDaySales.setDiscountQty(cursor.getInt(11));
                reportDaySales.setDiscountAmt(cursor.getString(12));
                reportDaySales.setFocItem(cursor.getString(13));
                reportDaySales.setFocItemQty(cursor.getInt(14));
                reportDaySales.setFocBill(cursor.getString(15));
                reportDaySales.setFocBillQty(cursor.getInt(16));
                reportDaySales.setTotalSales(cursor.getString(17));
                reportDaySales.setCash(cursor.getString(18));
                reportDaySales.setCashQty(cursor.getInt(19));
                reportDaySales.setNets(cursor.getString(20));
                reportDaySales.setNetsQty(cursor.getInt(21));
                reportDaySales.setVisa(cursor.getString(22));
                reportDaySales.setVisaQty(cursor.getInt(23));
                reportDaySales.setMc(cursor.getString(24));
                reportDaySales.setMcQty(cursor.getInt(25));
                reportDaySales.setAmex(cursor.getString(26));
                reportDaySales.setAmexQty(cursor.getInt(27));
                reportDaySales.setJbl(cursor.getString(28));
                reportDaySales.setJblQty(cursor.getInt(29));
                reportDaySales.setUnionPay(cursor.getString(30));
                reportDaySales.setUnionPayQty(cursor.getInt(31));
                reportDaySales.setDiner(cursor.getString(32));
                reportDaySales.setDinerQty(cursor.getInt(33));
                reportDaySales.setHoldld(cursor.getString(34));
                reportDaySales.setHoldldQty(cursor.getInt(35));
                reportDaySales.setTotalCard(cursor.getString(36));
                reportDaySales.setTotalCardQty(cursor.getInt(37));
                reportDaySales.setTotalCash(cursor.getString(38));
                reportDaySales.setTotalCashQty(cursor.getInt(39));
                reportDaySales.setBillVoid(cursor.getString(40));
                reportDaySales.setBillVoidQty(cursor.getInt(41));
                reportDaySales.setItemVoid(cursor.getString(42));
                reportDaySales.setItemVoidQty(cursor.getInt(43));
                reportDaySales.setNettSales(cursor.getString(44));
                reportDaySales.setTotalBills(cursor.getInt(45));
                reportDaySales.setOpenCount(cursor.getInt(46));
                reportDaySales.setFirstReceipt(cursor.getInt(47));
                reportDaySales.setLastReceipt(cursor.getInt(48));
                reportDaySales.setTotalTax(cursor.getString(49));
                reportDaySales.setOrderQty(cursor.getInt(50));
                reportDaySales.setPersonQty(cursor.getInt(51));
                reportDaySales.setTotalBalancePrice(cursor.getString(52));
                reportDaySales.setCashInAmt(cursor.getString(53));
                reportDaySales.setCashOutAmt(cursor.getString(54));
                reportDaySales.setVarianceAmt(cursor.getString(55));
                reportDaySales.setInclusiveTaxAmt(cursor.getString(56));
                reportDaySales.setAlipay(cursor.getString(57));
                reportDaySales.setAlipayQty(cursor.getInt(58));
                reportDaySales.setThirdParty(cursor.getString(59));
                reportDaySales.setThirdPartyQty(cursor.getInt(60));
                reportDaySales.setWeixinpay(cursor.getString(61));
                reportDaySales.setWeixinpayQty(cursor.getInt(62));
                reportDaySales.setPaypalpay(cursor.getString(63));
                reportDaySales.setPaypalpayQty(cursor.getInt(64));
                reportDaySales.setStoredCard(cursor.getString(65));
                reportDaySales.setStoredCardQty(cursor.getInt(66));
                reportDaySales.setTopUps(cursor.getString(67));
                reportDaySales.setTopUpsQty(cursor.getInt(68));
                reportDaySales.setBillRefund(cursor.getString(69));
                reportDaySales.setBillRefundQty(cursor.getInt(70));
                reportDaySales.setRefundTax(cursor.getString(71));
                reportDaySales.setStartDrawerAmount(cursor.getString(72));
                reportDaySales.setExpectedAmount(cursor.getString(73));
                reportDaySales.setWaiterAmount(cursor.getString(74));
                reportDaySales.setDifference(cursor.getString(75));
                reportDaySales.setCashTopUp(cursor.getString(76));
                reportDaySales.setTakeawaySales(cursor.getString(77));
                reportDaySales.setTakeawayTax(cursor.getString(78));
                reportDaySales.setTakeawayQty(cursor.getInt(79));
                reportDaySales.setCreateTime(cursor.getLong(80));
                reportDaySales.setUpdateTime(cursor.getLong(81));
                reportDaySales.setDeliveroo(cursor.getString(82));
                reportDaySales.setDeliverooQty(cursor.getInt(83));
                reportDaySales.setUbereats(cursor.getString(84));
                reportDaySales.setUbereatsQty(cursor.getInt(85));
                reportDaySales.setFoodpanda(cursor.getString(86));
                reportDaySales.setFoodpandaQty(cursor.getInt(87));
                reportDaySales.setVoucher(cursor.getString(88));
                reportDaySales.setVoucherQty(cursor.getInt(89));
                reportDaySales.setTotalHour(cursor.getString(90));
                reportDaySales.setReportNoStr(cursor.getString(91));
                reportDaySales.setPayHalal(cursor.getString(92));
                reportDaySales.setPayHalalQty(cursor.getInt(93));
                reportDaySales.setPromotionTotal(cursor.getString(94));

                reportDaySales.setIpay88Wepay(cursor.getString(95));
                reportDaySales.setIpay88WepayQty(cursor.getInt(96));
                reportDaySales.setIpay88Alipay(cursor.getString(97));
                reportDaySales.setIpay88AlipayQty(cursor.getInt(98));
                reportDaySales.setIpay88Boost(cursor.getString(99));
                reportDaySales.setIpay88BoostQty(cursor.getInt(100));
                reportDaySales.setIpay88Mcash(cursor.getString(101));
                reportDaySales.setIpay88McashQty(cursor.getInt(102));
                reportDaySales.setIpay88TouchnGo(cursor.getString(103));
                reportDaySales.setIpay88TouchnGoQty(cursor.getInt(104));
                reportDaySales.setIpay88Unionpay(cursor.getString(105));
                reportDaySales.setIpay88UnionpayQty(cursor.getInt(106));
                reportDaySales.setIpay88Mbb(cursor.getString(107));
                reportDaySales.setIpay88MbbQty(cursor.getInt(108));
                reportDaySales.setIpay88Cimb(cursor.getString(109));
                reportDaySales.setIpay88CimbQty(cursor.getInt(110));
                reportDaySales.setIpay88Grabpay(cursor.getString(111));
                reportDaySales.setIpay88GrabpayQty(cursor.getInt(112));
                reportDaySales.setIpay88Nets(cursor.getString(113));
                reportDaySales.setIpay88NetsQty(cursor.getInt(114));
                reportDaySales.setDaySalesRound(cursor.getString(115));
                reportDaySales.setPromotionQty(cursor.getInt(116));

				result.add(reportDaySales);
			}
		} catch (Exception e) {
			e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static ArrayList<ReportDaySales> getReportDaySalessByNowTime() {
        ArrayList<ReportDaySales> result = new ArrayList<ReportDaySales>();
        String sql = "select * from "
                + TableNames.ReportDaySales
                + " where createTime < datetime('now','localtime') and paidDate > date('now','start of day')";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            ReportDaySales reportDaySales = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                reportDaySales = new ReportDaySales();
                reportDaySales.setId(cursor.getInt(0));
                reportDaySales.setRestaurantId(cursor.getInt(1));
                reportDaySales.setRestaurantName(cursor.getString(2));
                reportDaySales.setRevenueId(cursor.getInt(3));
                reportDaySales.setRevenueName(cursor.getString(4));
                reportDaySales.setBusinessDate(cursor.getLong(5));
                reportDaySales.setItemSales(cursor.getString(6));
                reportDaySales.setItemSalesQty(cursor.getInt(7));
                reportDaySales.setDiscountPer(cursor.getString(8));
                reportDaySales.setDiscountPerQty(cursor.getInt(9));
                reportDaySales.setDiscount(cursor.getString(10));
                reportDaySales.setDiscountQty(cursor.getInt(11));
                reportDaySales.setDiscountAmt(cursor.getString(12));
                reportDaySales.setFocItem(cursor.getString(13));
                reportDaySales.setFocItemQty(cursor.getInt(14));
                reportDaySales.setFocBill(cursor.getString(15));
                reportDaySales.setFocBillQty(cursor.getInt(16));
                reportDaySales.setTotalSales(cursor.getString(17));
                reportDaySales.setCash(cursor.getString(18));
                reportDaySales.setCashQty(cursor.getInt(19));
                reportDaySales.setNets(cursor.getString(20));
                reportDaySales.setNetsQty(cursor.getInt(21));
                reportDaySales.setVisa(cursor.getString(22));
                reportDaySales.setVisaQty(cursor.getInt(23));
                reportDaySales.setMc(cursor.getString(24));
                reportDaySales.setMcQty(cursor.getInt(25));
                reportDaySales.setAmex(cursor.getString(26));
                reportDaySales.setAmexQty(cursor.getInt(27));
                reportDaySales.setJbl(cursor.getString(28));
                reportDaySales.setJblQty(cursor.getInt(29));
                reportDaySales.setUnionPay(cursor.getString(30));
                reportDaySales.setUnionPayQty(cursor.getInt(31));
                reportDaySales.setDiner(cursor.getString(32));
                reportDaySales.setDinerQty(cursor.getInt(33));
                reportDaySales.setHoldld(cursor.getString(34));
                reportDaySales.setHoldldQty(cursor.getInt(35));
                reportDaySales.setTotalCard(cursor.getString(36));
                reportDaySales.setTotalCardQty(cursor.getInt(37));
                reportDaySales.setTotalCash(cursor.getString(38));
                reportDaySales.setTotalCashQty(cursor.getInt(39));
                reportDaySales.setBillVoid(cursor.getString(40));
                reportDaySales.setBillVoidQty(cursor.getInt(41));
                reportDaySales.setItemVoid(cursor.getString(42));
                reportDaySales.setItemVoidQty(cursor.getInt(43));
                reportDaySales.setNettSales(cursor.getString(44));
                reportDaySales.setTotalBills(cursor.getInt(45));
                reportDaySales.setOpenCount(cursor.getInt(46));
                reportDaySales.setFirstReceipt(cursor.getInt(47));
                reportDaySales.setLastReceipt(cursor.getInt(48));
                reportDaySales.setTotalTax(cursor.getString(49));
                reportDaySales.setOrderQty(cursor.getInt(50));
                reportDaySales.setPersonQty(cursor.getInt(51));
                reportDaySales.setTotalBalancePrice(cursor.getString(52));
                reportDaySales.setCashInAmt(cursor.getString(53));
                reportDaySales.setCashOutAmt(cursor.getString(54));
                reportDaySales.setVarianceAmt(cursor.getString(55));
                reportDaySales.setInclusiveTaxAmt(cursor.getString(56));
                reportDaySales.setAlipay(cursor.getString(57));
                reportDaySales.setAlipayQty(cursor.getInt(58));
                reportDaySales.setThirdParty(cursor.getString(59));
                reportDaySales.setThirdPartyQty(cursor.getInt(60));
                reportDaySales.setWeixinpay(cursor.getString(61));
                reportDaySales.setWeixinpayQty(cursor.getInt(62));
                reportDaySales.setPaypalpay(cursor.getString(63));
                reportDaySales.setPaypalpayQty(cursor.getInt(64));
                reportDaySales.setStoredCard(cursor.getString(65));
                reportDaySales.setStoredCardQty(cursor.getInt(66));
                reportDaySales.setTopUps(cursor.getString(67));
                reportDaySales.setTopUpsQty(cursor.getInt(68));
                reportDaySales.setBillRefund(cursor.getString(69));
                reportDaySales.setBillRefundQty(cursor.getInt(70));
                reportDaySales.setRefundTax(cursor.getString(71));
                reportDaySales.setStartDrawerAmount(cursor.getString(72));
                reportDaySales.setExpectedAmount(cursor.getString(73));
                reportDaySales.setWaiterAmount(cursor.getString(74));
                reportDaySales.setDifference(cursor.getString(75));
                reportDaySales.setCashTopUp(cursor.getString(76));
                reportDaySales.setTakeawaySales(cursor.getString(77));
                reportDaySales.setTakeawayTax(cursor.getString(78));
                reportDaySales.setTakeawayQty(cursor.getInt(79));
                reportDaySales.setCreateTime(cursor.getLong(80));
                reportDaySales.setUpdateTime(cursor.getLong(81));
                reportDaySales.setDeliveroo(cursor.getString(82));
                reportDaySales.setDeliverooQty(cursor.getInt(83));
                reportDaySales.setUbereats(cursor.getString(84));
                reportDaySales.setUbereatsQty(cursor.getInt(85));
                reportDaySales.setFoodpanda(cursor.getString(86));
                reportDaySales.setFoodpandaQty(cursor.getInt(87));
                reportDaySales.setVoucher(cursor.getString(88));
                reportDaySales.setVoucherQty(cursor.getInt(89));
                reportDaySales.setTotalHour(cursor.getString(90));
                reportDaySales.setReportNoStr(cursor.getString(91));
                reportDaySales.setPayHalal(cursor.getString(92));
                reportDaySales.setPayHalalQty(cursor.getInt(93));
                reportDaySales.setPromotionTotal(cursor.getString(94));

                reportDaySales.setIpay88Wepay(cursor.getString(95));
                reportDaySales.setIpay88WepayQty(cursor.getInt(96));
                reportDaySales.setIpay88Alipay(cursor.getString(97));
                reportDaySales.setIpay88AlipayQty(cursor.getInt(98));
                reportDaySales.setIpay88Boost(cursor.getString(99));
                reportDaySales.setIpay88BoostQty(cursor.getInt(100));
                reportDaySales.setIpay88Mcash(cursor.getString(101));
                reportDaySales.setIpay88McashQty(cursor.getInt(102));
                reportDaySales.setIpay88TouchnGo(cursor.getString(103));
                reportDaySales.setIpay88TouchnGoQty(cursor.getInt(104));
                reportDaySales.setIpay88Unionpay(cursor.getString(105));
                reportDaySales.setIpay88UnionpayQty(cursor.getInt(106));
                reportDaySales.setIpay88Mbb(cursor.getString(107));
                reportDaySales.setIpay88MbbQty(cursor.getInt(108));
                reportDaySales.setIpay88Cimb(cursor.getString(109));
                reportDaySales.setIpay88CimbQty(cursor.getInt(110));
                reportDaySales.setIpay88Grabpay(cursor.getString(111));
                reportDaySales.setIpay88GrabpayQty(cursor.getInt(112));
                reportDaySales.setIpay88Nets(cursor.getString(113));
                reportDaySales.setIpay88NetsQty(cursor.getInt(114));

                reportDaySales.setDaySalesRound(cursor.getString(115));
                reportDaySales.setPromotionQty(cursor.getInt(116));

                result.add(reportDaySales);
			}
		} catch (Exception e) {
			e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }


    public static ReportDaySales getReportDaySalesForZReport(long business) {
        ReportDaySales reportDaySales = null;

        String sql = "select restaurantId, restaurantName, revenueId, revenueName, businessDate, sum(itemSales), sum(itemSalesQty), sum(discountPer), sum(discountPerQty), "
                + " sum(discount), sum(discountQty), sum(discountAmt), sum(focItem), sum(focItemQty), sum(focBill), sum(focBillQty), sum(totalSales), sum(cash), sum(cashQty),"
                + " sum(nets), sum(netsQty), sum(visa),  sum(visaQty), sum(mc), sum(mcQty), sum(amex), sum(amexQty), sum(jbl), sum(jblQty), sum(unionPay), sum(unionPayQty), "
                + " sum(diner), sum(dinerQty), sum(holdld), sum(holdldQty), sum(totalCard), sum(totalCardQty), sum(totalCash), sum(totalCashQty), sum(billVoid), sum(billVoidQty),"
                + " sum(itemVoid), sum(itemVoidQty), sum(nettSales), sum(totalBills), sum(openCount), sum(firstReceipt), sum(lastReceipt), sum(totalTax), sum(orderQty),"
                + " sum(personQty), sum(totalBalancePrice), sum(cashInAmt), sum(cashOutAmt), sum(varianceAmt), sum(inclusiveTaxAmt), sum(alipay), sum(alipayQty), sum(thirdParty), "
                + " sum(thirdPartyQty), sum(weixinpay), sum(weixinpayQty), sum(paypalpay), sum(paypalpayQty), sum(storedCard), sum(storedCardQty), sum(topUps), sum(topUpsQty),"
                + " sum(billRefund), sum(billRefundQty), sum(refundTax), sum(startDrawerAmount), sum(expectedAmount), sum(waiterAmount), sum(difference), sum(cashTopUp), "
                + " sum(takeawaySales), sum(takeawayTax), sum(takeawayQty), createTime, updateTime, sum(deliveroo), sum(deliverooQty), sum(ubereats), sum(ubereatsQty), "
                + " sum(foodpanda), sum(foodpandaQty), sum(voucher), sum(voucherQty), sum(totalHour), reportNoStr, sum(payHalal), sum(payHalalQty),sum(promotionTotal), sum(ipay88Wepay), sum(ipay88WepayQty), sum(ipay88Alipay), sum(ipay88AlipayQty), sum(ipay88Boost), sum(ipay88BoostQty), sum(ipay88Mcash), sum(ipay88McashQty), sum(ipay88TouchnGo), sum(ipay88TouchnGoQty), sum(ipay88Unionpay), sum(ipay88UnionpayQty), sum(ipay88Mbb), sum(ipay88MbbQty), sum(ipay88Cimb), sum(ipay88CimbQty), sum(ipay88Grabpay), sum(ipay88GrabpayQty), sum(ipay88Nets), sum(ipay88NetsQty),sum(daySalesRound),sum(promotionQty) from "
                + TableNames.ReportDaySales
                + " where businessDate = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{String.valueOf(business)});
            int count = cursor.getCount();
            if (count < 1) {
                return reportDaySales;
            }
            if (cursor.moveToFirst()) {
                reportDaySales = new ReportDaySales();
                reportDaySales.setRestaurantId(cursor.getInt(0));
                reportDaySales.setRestaurantName(cursor.getString(1));
                reportDaySales.setRevenueId(cursor.getInt(2));
                reportDaySales.setRevenueName(cursor.getString(3));
                reportDaySales.setBusinessDate(cursor.getLong(4));
                reportDaySales.setItemSales(BH.getBD(cursor.getString(5)).toString());
                reportDaySales.setItemSalesQty(cursor.getInt(6));
                reportDaySales.setDiscountPer(BH.getBD(cursor.getString(7)).toString());
                reportDaySales.setDiscountPerQty(cursor.getInt(8));
                reportDaySales.setDiscount(BH.getBD(cursor.getString(9)).toString());
                reportDaySales.setDiscountQty(cursor.getInt(10));
                reportDaySales.setDiscountAmt(BH.getBD(cursor.getString(11)).toString());
                reportDaySales.setFocItem(BH.getBD(cursor.getString(12)).toString());
                reportDaySales.setFocItemQty(cursor.getInt(13));
                reportDaySales.setFocBill(BH.getBD(cursor.getString(14)).toString());
                reportDaySales.setFocBillQty(cursor.getInt(15));
                reportDaySales.setTotalSales(BH.getBD(cursor.getString(16)).toString());
                reportDaySales.setCash(BH.getBD(cursor.getString(17)).toString());
                reportDaySales.setCashQty(cursor.getInt(18));
                reportDaySales.setNets(BH.getBD(cursor.getString(19)).toString());
                reportDaySales.setNetsQty(cursor.getInt(20));
                reportDaySales.setVisa(BH.getBD(cursor.getString(21)).toString());
                reportDaySales.setVisaQty(cursor.getInt(22));
                reportDaySales.setMc(BH.getBD(cursor.getString(23)).toString());
                reportDaySales.setMcQty(cursor.getInt(24));
                reportDaySales.setAmex(BH.getBD(cursor.getString(25)).toString());
                reportDaySales.setAmexQty(cursor.getInt(26));
                reportDaySales.setJbl(BH.getBD(cursor.getString(27)).toString());
                reportDaySales.setJblQty(cursor.getInt(28));
                reportDaySales.setUnionPay(BH.getBD(cursor.getString(29)).toString());
                reportDaySales.setUnionPayQty(cursor.getInt(30));
                reportDaySales.setDiner(BH.getBD(cursor.getString(31)).toString());
                reportDaySales.setDinerQty(cursor.getInt(32));
                reportDaySales.setHoldld(BH.getBD(cursor.getString(33)).toString());
                reportDaySales.setHoldldQty(cursor.getInt(34));
                reportDaySales.setTotalCard(BH.getBD(cursor.getString(35)).toString());
                reportDaySales.setTotalCardQty(cursor.getInt(36));
                reportDaySales.setTotalCash(BH.getBD(cursor.getString(37)).toString());
                reportDaySales.setTotalCashQty(cursor.getInt(38));
                reportDaySales.setBillVoid(BH.getBD(cursor.getString(39)).toString());
                reportDaySales.setBillVoidQty(cursor.getInt(40));
                reportDaySales.setItemVoid(BH.getBD(cursor.getString(41)).toString());
                reportDaySales.setItemVoidQty(cursor.getInt(42));
                reportDaySales.setNettSales(BH.getBD(cursor.getString(43)).toString());
                reportDaySales.setTotalBills(cursor.getInt(44));
                reportDaySales.setOpenCount(cursor.getInt(45));
                reportDaySales.setFirstReceipt(cursor.getInt(46));
                reportDaySales.setLastReceipt(cursor.getInt(47));
                reportDaySales.setTotalTax(BH.getBD(cursor.getString(48)).toString());
                reportDaySales.setOrderQty(cursor.getInt(49));
                reportDaySales.setPersonQty(cursor.getInt(50));
                reportDaySales.setTotalBalancePrice(BH.getBD(cursor.getString(51)).toString());
                reportDaySales.setCashInAmt(BH.getBD(cursor.getString(52)).toString());
                reportDaySales.setCashOutAmt(BH.getBD(cursor.getString(53)).toString());
                reportDaySales.setVarianceAmt(BH.getBD(cursor.getString(54)).toString());
                reportDaySales.setInclusiveTaxAmt(BH.getBD(cursor.getString(55)).toString());
                reportDaySales.setAlipay(BH.getBD(cursor.getString(56)).toString());
                reportDaySales.setAlipayQty(cursor.getInt(57));
                reportDaySales.setThirdParty(BH.getBD(cursor.getString(58)).toString());
                reportDaySales.setThirdPartyQty(cursor.getInt(59));
                reportDaySales.setWeixinpay(BH.getBD(cursor.getString(60)).toString());
                reportDaySales.setWeixinpayQty(cursor.getInt(61));
                reportDaySales.setPaypalpay(BH.getBD(cursor.getString(62)).toString());
                reportDaySales.setPaypalpayQty(cursor.getInt(63));
                reportDaySales.setStoredCard(BH.getBD(cursor.getString(64)).toString());
                reportDaySales.setStoredCardQty(cursor.getInt(65));
                reportDaySales.setTopUps(BH.getBD(cursor.getString(66)).toString());
                reportDaySales.setTopUpsQty(cursor.getInt(67));
                reportDaySales.setBillRefund(BH.getBD(cursor.getString(68)).toString());
                reportDaySales.setBillRefundQty(cursor.getInt(69));
                reportDaySales.setRefundTax(BH.getBD(cursor.getString(70)).toString());
                reportDaySales.setStartDrawerAmount(BH.getBD(cursor.getString(71)).toString());
                reportDaySales.setExpectedAmount(BH.getBD(cursor.getString(72)).toString());
                reportDaySales.setWaiterAmount(BH.getBD(cursor.getString(73)).toString());
                reportDaySales.setDifference(BH.getBD(cursor.getString(74)).toString());
                reportDaySales.setCashTopUp(BH.getBD(cursor.getString(75)).toString());
                reportDaySales.setTakeawaySales(BH.getBD(cursor.getString(76)).toString());
                reportDaySales.setTakeawayTax(BH.getBD(cursor.getString(77)).toString());
                reportDaySales.setTakeawayQty(cursor.getInt(78));
                reportDaySales.setCreateTime(cursor.getLong(79));
                reportDaySales.setUpdateTime(cursor.getLong(80));
                reportDaySales.setDeliveroo(BH.getBD(cursor.getString(81)).toString());
                reportDaySales.setDeliverooQty(cursor.getInt(82));
                reportDaySales.setUbereats(BH.getBD(cursor.getString(83)).toString());
                reportDaySales.setUbereatsQty(cursor.getInt(84));
                reportDaySales.setFoodpanda(BH.getBD(cursor.getString(85)).toString());
                reportDaySales.setFoodpandaQty(cursor.getInt(86));
                reportDaySales.setVoucher(BH.getBD(cursor.getString(87)).toString());
                reportDaySales.setVoucherQty(cursor.getInt(88));
                reportDaySales.setTotalHour(BH.getBDThirdFormat(cursor.getString(89)).toString());
                reportDaySales.setReportNoStr(cursor.getString(90));
                reportDaySales.setPayHalal(BH.getBD(cursor.getString(91)).toString());
                reportDaySales.setPayHalalQty(cursor.getInt(92));
                reportDaySales.setPromotionTotal(cursor.getString(93));


                reportDaySales.setIpay88Wepay(cursor.getString(94));
                reportDaySales.setIpay88WepayQty(cursor.getInt(95));
                reportDaySales.setIpay88Alipay(cursor.getString(96));
                reportDaySales.setIpay88AlipayQty(cursor.getInt(97));
                reportDaySales.setIpay88Boost(cursor.getString(98));
                reportDaySales.setIpay88BoostQty(cursor.getInt(99));
                reportDaySales.setIpay88Mcash(cursor.getString(100));
                reportDaySales.setIpay88McashQty(cursor.getInt(101));
                reportDaySales.setIpay88TouchnGo(cursor.getString(102));
                reportDaySales.setIpay88TouchnGoQty(cursor.getInt(103));
                reportDaySales.setIpay88Unionpay(cursor.getString(104));
                reportDaySales.setIpay88UnionpayQty(cursor.getInt(105));
                reportDaySales.setIpay88Mbb(cursor.getString(106));
                reportDaySales.setIpay88MbbQty(cursor.getInt(107));
                reportDaySales.setIpay88Cimb(cursor.getString(108));
                reportDaySales.setIpay88CimbQty(cursor.getInt(109));
                reportDaySales.setIpay88Grabpay(cursor.getString(110));
                reportDaySales.setIpay88GrabpayQty(cursor.getInt(111));
                reportDaySales.setIpay88Nets(cursor.getString(112));
                reportDaySales.setIpay88NetsQty(cursor.getInt(113));
                reportDaySales.setDaySalesRound(cursor.getString(114));
                reportDaySales.setPromotionQty(cursor.getInt(115));
			}
		} catch (Exception e) {
			e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return reportDaySales;
    }

    public static String getReportNoStrByBusiness(long business) {
        String reportNoStr = null;
        String sql = "select reportNoStr from "
                + TableNames.ReportDaySales
                + " where businessDate = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{String.valueOf(business)});
            int count = cursor.getCount();
            if (count < 1) {
                return reportNoStr;
            }
            if (cursor.moveToFirst()) {
                reportNoStr = cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return reportNoStr;
    }

    public static void deleteReportDaySales(ReportDaySales reportDaySales) {
        String sql = "delete from " + TableNames.ReportDaySales
                + " where id = ?";
        try {
            SQLExe.getDB()
                    .execSQL(sql, new Object[]{reportDaySales.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
