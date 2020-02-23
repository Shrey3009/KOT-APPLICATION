package com.alfredbase.store.sql;

import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.MultiReportRelation;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.UserOpenDrawerRecord;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.ObjectFactory;

import java.util.List;

public class SubPosCommitSQL {
    public static boolean commitOrder(int subPosBeanId, Order subOrder, List<OrderSplit> orderSplits, List<OrderBill> orderBills,
                                      List<Payment> payments, List<OrderDetail> orderDetails, List<OrderModifier> orderModifiers,
                                      List<OrderDetailTax> orderDetailTaxs, List<PaymentSettlement> paymentSettlements, List<RoundAmount> roundAmounts){
        boolean isSuccessful;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            ObjectFactory.getInstance().cpOrderInfo(db, subPosBeanId, subOrder, orderSplits, orderBills, payments, orderDetails,
                    orderModifiers, orderDetailTaxs, paymentSettlements, roundAmounts);

            db.setTransactionSuccessful();
            isSuccessful = true;
        }catch (Exception e){
            isSuccessful = false;
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return isSuccessful;
    }
    public static int commitOrderForKPMG(Order subOrder, List<OrderSplit> orderSplits, List<OrderBill> orderBills,
                                      List<Payment> payments, List<OrderDetail> orderDetails, List<OrderModifier> orderModifiers,
                                      List<OrderDetailTax> orderDetailTaxs, List<PaymentSettlement> paymentSettlements,
                                        List<RoundAmount> roundAmounts, String cardNum, long business, int sessionStatus,
                                         int tableId){
        int orderId = 0;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            orderId = ObjectFactory.getInstance().cpOrderInfoForKPMG(subOrder, orderSplits, orderBills, payments, orderDetails,
                    orderModifiers, orderDetailTaxs, paymentSettlements, roundAmounts, cardNum, business, sessionStatus, tableId).getId();

            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return orderId;
    }
    public static boolean commitOrderLog(Order subOrder, List<OrderSplit> orderSplits,  List<Payment> payments,
                                         List<PaymentSettlement> paymentSettlements, List<RoundAmount> roundAmounts){
        boolean isSuccessful;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            ObjectFactory.getInstance().cpOrderInfoLog(db, subOrder, orderSplits, payments, paymentSettlements, roundAmounts);

            db.setTransactionSuccessful();
            isSuccessful = true;
        }catch (Exception e){
            isSuccessful = false;
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return isSuccessful;
    }

    public static boolean commitReport(int subPosBeanId, ReportDaySales reportDaySales, List<ReportDayTax> reportDayTaxs,
                                       List<ReportDayPayment> reportDayPayments, List<ReportPluDayItem> reportPluDayItems,
                                       List<ReportPluDayModifier> reportPluDayModifiers, List<ReportHourly> reportHourlys,
                                       List<ReportPluDayComboModifier> reportPluDayComboModifiers, List<UserOpenDrawerRecord> userOpenDrawerRecords){
        boolean isSuccessful;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            int oldReportDaySalesId = reportDaySales.getId();
            reportDaySales.setId(CommonSQL.getNextSeq(TableNames.ReportDaySales));
            ReportDaySalesSQL.addReportDaySales(db, reportDaySales);
            MultiReportRelation m = new MultiReportRelation(reportDaySales.getId(), oldReportDaySalesId, subPosBeanId, reportDaySales.getCreateTime(), 0);
            MultiReportRelationSQL.updateMultiReportRelation(db, m);
            for(ReportDayTax reportDayTax : reportDayTaxs){
                reportDayTax.setId(CommonSQL.getNextSeq(TableNames.ReportDayTax));
                reportDayTax.setDaySalesId(reportDaySales.getId());
                ReportDayTaxSQL.addReportDayTax(db, reportDayTax);
            }
            for(ReportDayPayment reportDayPayment : reportDayPayments){
                reportDayPayment.setId(CommonSQL.getNextSeq(TableNames.ReportDayPayment));
                reportDayPayment.setDaySalesId(reportDaySales.getId());
                ReportDayPaymentSQL.addReportDayPayment(db, reportDayPayment);
            }
            if(reportPluDayItems != null && reportPluDayItems.size() > 0){
                ReportPluDayItemSQL.addReportPluDayItems(reportDaySales.getId().intValue(), db, reportPluDayItems);
            }
            if(reportPluDayModifiers != null && reportDayPayments.size() > 0){
                ReportPluDayModifierSQL.addReportPluDayModifierList(reportDaySales.getId().intValue(), db, reportPluDayModifiers);
            }
            if(reportHourlys != null && reportHourlys.size() > 0){
                ReportHourlySQL.addReportHourly(reportDaySales.getId().intValue(), db, reportHourlys);
            }
//            if(reportHourlys != null && reportHourlys.size() > 0){
//               // ReportPromotionSQL.addReportPromotion(reportDaySales.getId().intValue(), db, reportHourlys);
//            }
            if(reportPluDayComboModifiers != null && reportPluDayComboModifiers.size() > 0) {
                ReportPluDayComboModifierSQL.addReportPluDayModifierList(reportDaySales.getId().intValue(), db, reportPluDayComboModifiers);
            }
            if(userOpenDrawerRecords != null && userOpenDrawerRecords.size() > 0){
                UserOpenDrawerRecordSQL.addUserOpenDrawerRecordList(reportDaySales.getId().intValue(), db, userOpenDrawerRecords);
            }
            db.setTransactionSuccessful();
            isSuccessful = true;
        }catch (Exception e){
            isSuccessful = false;
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return isSuccessful;
    }
}
