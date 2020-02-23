package com.alfredbase.utils;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.ParamHelper;
import com.alfredbase.R;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.AlipaySettlement;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.javabean.CardsSettlement;
import com.alfredbase.javabean.CashInOut;
import com.alfredbase.javabean.EventLog;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.ItemPromotion;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.LocalDevice;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.MultiOrderRelation;
import com.alfredbase.javabean.NetsSettlement;
import com.alfredbase.javabean.NonChargableSettlement;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.Promotion;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportDiscount;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.Restaurant;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.SubPosBean;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.UserOpenDrawerRecord;
import com.alfredbase.javabean.UserTimeSheet;
import com.alfredbase.javabean.VoidSettlement;
import com.alfredbase.javabean.WeixinSettlement;
import com.alfredbase.javabean.model.KotNotification;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrintReceiptInfo;
import com.alfredbase.javabean.model.ReportSessionSales;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.javabean.temporaryforapp.AppOrderDetail;
import com.alfredbase.javabean.temporaryforapp.AppOrderDetailTax;
import com.alfredbase.javabean.temporaryforapp.AppOrderModifier;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.AlipaySettlementSQL;
import com.alfredbase.store.sql.BohHoldSettlementSQL;
import com.alfredbase.store.sql.CardsSettlementSQL;
import com.alfredbase.store.sql.CashInOutSQL;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.EventLogSQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotNotificationSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.LocalDeviceSQL;
import com.alfredbase.store.sql.MultiOrderRelationSQL;
import com.alfredbase.store.sql.NetsSettlementSQL;
import com.alfredbase.store.sql.NonChargableSettlementSQL;
import com.alfredbase.store.sql.OrderBillSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.ReportDiscountSQL;
import com.alfredbase.store.sql.RestaurantSQL;
import com.alfredbase.store.sql.RevenueCenterSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.SettingDataSQL;
import com.alfredbase.store.sql.SubPosBeanSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.UserOpenDrawerRecordSQL;
import com.alfredbase.store.sql.VoidSettlementSQL;
import com.alfredbase.store.sql.WeixinSettlementSQL;
import com.alfredbase.store.sql.cpsql.CPOrderBillSQL;
import com.alfredbase.store.sql.cpsql.CPOrderDetailSQL;
import com.alfredbase.store.sql.cpsql.CPOrderDetailTaxSQL;
import com.alfredbase.store.sql.cpsql.CPOrderModifierSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSQL;
import com.alfredbase.store.sql.cpsql.CPOrderSplitSQL;
import com.alfredbase.store.sql.cpsql.CPPaymentSQL;
import com.alfredbase.store.sql.cpsql.CPPaymentSettlementSQL;
import com.alfredbase.store.sql.cpsql.CPRoundAmountSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderDetailTaxSQL;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ObjectFactory {
    private static ObjectFactory instance;

    private ObjectFactory() {
    }

    public static ObjectFactory getInstance() {
        if (instance == null)
            instance = new ObjectFactory();
        return instance;
    }

    Object lock_order = new Object();

    public Order getOrder(Integer orderOriginId, int subPosBeanId, TableInfo tables,
                          RevenueCenter revenueCenter, User user,
                          SessionStatus sessionStatus, long businessDate, int orderNOTitle,
                          int orderStatus, Tax inclusiveTax, String waiterName) {
        return getOrder(orderOriginId, subPosBeanId, tables, revenueCenter, user, sessionStatus, businessDate, orderNOTitle, orderStatus, inclusiveTax, 0, waiterName);
    }

    public Order getOrderWaitingList(Integer orderOriginId, int subPosBeanId, TableInfo tables,
                                     RevenueCenter revenueCenter, User user,
                                     SessionStatus sessionStatus, long businessDate, int orderNOTitle,
                                     int orderStatus, Tax inclusiveTax) {
        int appOrderId = 0;
        Order order = null;
        synchronized (lock_order) {
            order = OrderSQL.getWaitingListOrder(tables.getPosId(), businessDate, sessionStatus);
            if (order == null) {

                order = new Order();
                order.setId(CommonSQL.getNextSeq(TableNames.Order));
                order.setOrderOriginId(orderOriginId);
                order.setUserId(user.getId());
                order.setPersons(tables.getPacks());
                order.setOrderStatus(orderStatus);
                order.setDiscountRate(ParamConst.DOUBLE_ZERO);
                order.setSessionStatus(sessionStatus.getSession_status());
                order.setRestId(CoreData.getInstance().getRestaurant().getId());
                order.setRevenueId(revenueCenter.getId());
                order.setPlaceId(tables.getPlacesId());
                order.setTableId(tables.getPosId());
                long time = System.currentTimeMillis();
                order.setCreateTime(time);
                order.setUpdateTime(time);
                order.setBusinessDate(businessDate);
//					order.setOrderNo(order.getId());
                order.setOrderNo(OrderHelper.calculateOrderNo(businessDate));//流水号
                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_NULL);
                order.setAppOrderId(appOrderId);
                if (inclusiveTax != null) {
                    order.setInclusiveTaxName(inclusiveTax.getTaxName());
                    order.setInclusiveTaxPercentage(inclusiveTax.getTaxPercentage());
                }
                if (subPosBeanId > 0) {
                    SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanById(subPosBeanId);
                    order.setNumTag(subPosBean.getNumTag());
                }
                order.setSubPosBeanId(subPosBeanId);
                OrderSQL.addOrder(order);
            }
        }
        return order;
    }

    Object lock_cpOrderInfo = new Object();

    public Order cpOrderInfo(SQLiteDatabase db, int subPosBeanId, Order subOrder, List<OrderSplit> orderSplits, List<OrderBill> orderBills,
                             List<Payment> payments, List<OrderDetail> orderDetails, List<OrderModifier> orderModifiers,
                             List<OrderDetailTax> orderDetailTaxs, List<PaymentSettlement> paymentSettlements, List<RoundAmount> roundAmounts) throws Exception {

        synchronized (lock_cpOrderInfo) {
            if (subOrder != null) {
                int oldId = subOrder.getId().intValue();
                subOrder.setId(CommonSQL.getNextSeq(TableNames.CPOrder));
                CPOrderSQL.update(db, subOrder);
                MultiOrderRelation m = new MultiOrderRelation(subOrder.getId(), oldId, subPosBeanId, subOrder.getCreateTime());
                MultiOrderRelationSQL.updateMultiOrderRelation(db, m);
            }

            Map<Integer, Integer> orderSplitMap = new HashMap<>();
            for (OrderSplit orderSplit : orderSplits) {
                int oldId = orderSplit.getId();
                orderSplit.setId(CommonSQL.getNextSeq(TableNames.CPOrderSplit));
                orderSplit.setOrderId(subOrder.getId());
                CPOrderSplitSQL.update(db, orderSplit, oldId);
                orderSplitMap.put(oldId, orderSplit.getId());
            }

            Map<Integer, Integer> paymentMap = new HashMap<>();
            for (Payment payment : payments) {
                int oldId = payment.getId();
                payment.setId(CommonSQL.getNextSeq(TableNames.CPPayment));
                payment.setOrderId(subOrder.getId());
                Integer orderSplitId = payment.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    payment.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                CPPaymentSQL.addPayment(db, payment);
                paymentMap.put(oldId, payment.getId());
            }

            for (RoundAmount roundAmount : roundAmounts) {
                roundAmount.setId(CommonSQL.getNextSeq(TableNames.CPRoundAmount));
                roundAmount.setOrderId(subOrder.getId());
                Integer orderSplitId = roundAmount.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    roundAmount.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                CPRoundAmountSQL.update(db, roundAmount);
            }

            for (OrderBill orderBill : orderBills) {
                orderBill.setId(CommonSQL.getNextSeq(TableNames.CPOrderBill));
                orderBill.setOrderId(subOrder.getId());
                Integer orderSplitId = orderBill.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    orderBill.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                CPOrderBillSQL.add(db, orderBill);
            }

            Map<Integer, Integer> orderDetailMap = new HashMap<>();
            for (OrderDetail orderDetail : orderDetails) {
                int oldId = orderDetail.getId();
                orderDetail.setId(CommonSQL.getNextSeq(TableNames.CPOrderDetail));
                orderDetail.setOrderId(subOrder.getId());
                Integer orderSplitId = orderDetail.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    orderDetail.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                CPOrderDetailSQL.updateOrderDetail(db, orderDetail);
                orderDetailMap.put(oldId, orderDetail.getId());
            }

            for (OrderModifier orderModifier : orderModifiers) {
                orderModifier.setId(CommonSQL.getNextSeq(TableNames.CPOrderModifier));
                Integer orderDetailId = orderModifier.getOrderDetailId();
                if (orderDetailId != null && orderDetailMap.containsKey(orderDetailId.intValue())) {
                    orderModifier.setOrderDetailId(orderDetailMap.get(orderDetailId.intValue()));
                }
                orderModifier.setOrderId(subOrder.getId());
                CPOrderModifierSQL.updateOrderModifier(db, orderModifier);
            }
            for (OrderDetailTax orderDetailTax : orderDetailTaxs) {
                orderDetailTax.setId(CommonSQL.getNextSeq(TableNames.CPOrderDetailTax));
                Integer orderDetailId = orderDetailTax.getOrderDetailId();
                if (orderDetailId != null && orderDetailMap.containsKey(orderDetailId.intValue())) {
                    orderDetailTax.setOrderDetailId(orderDetailMap.get(orderDetailId.intValue()));
                }
                orderDetailTax.setOrderId(subOrder.getId());
                CPOrderDetailTaxSQL.updateOrderDetailTax(db, orderDetailTax);
            }

            for (PaymentSettlement paymentSettlement : paymentSettlements) {
                paymentSettlement.setId(CommonSQL.getNextSeq(TableNames.CPPaymentSettlement));
                Integer paymentId = paymentSettlement.getPaymentId();
                if (paymentId != null && paymentMap.containsKey(paymentId.intValue())) {
                    paymentSettlement.setPaymentId(paymentMap.get(paymentId.intValue()));
                }
                CPPaymentSettlementSQL.addPaymentSettlement(db, paymentSettlement);
            }
        }
        return subOrder;
    }

    public Order cpOrderInfoForKPMG(Order subOrder, List<OrderSplit> orderSplits, List<OrderBill> orderBills,
                                    List<Payment> payments, List<OrderDetail> orderDetails, List<OrderModifier> orderModifiers,
                                    List<OrderDetailTax> orderDetailTaxs, List<PaymentSettlement> paymentSettlements,
                                    List<RoundAmount> roundAmounts, String cardNum, long business, int sessionStatus,
                                    int tableId) throws Exception {

        synchronized (lock_order) {
            if (subOrder != null) {
                subOrder.setId(CommonSQL.getNextSeq(TableNames.Order));
                subOrder.setOrderStatus(ParamConst.ORDER_STATUS_FINISHED);
                subOrder.setOrderNo(OrderHelper.calculateOrderNo(subOrder.getBusinessDate()));
                subOrder.setCreateTime(System.currentTimeMillis());
                subOrder.setUpdateTime(System.currentTimeMillis());
                subOrder.setSessionStatus(sessionStatus);
                subOrder.setBusinessDate(business);
                subOrder.setTableId(tableId);
                OrderSQL.update(subOrder);
            }

            Map<Integer, Integer> orderSplitMap = new HashMap<>();
            for (OrderSplit orderSplit : orderSplits) {
                int oldId = orderSplit.getId();
                orderSplit.setId(CommonSQL.getNextSeq(TableNames.OrderSplit));
                orderSplit.setOrderId(subOrder.getId());
                orderSplit.setCreateTime(System.currentTimeMillis());
                orderSplit.setUpdateTime(System.currentTimeMillis());
                orderSplit.setTableId(tableId);
                OrderSplitSQL.update(orderSplit);
                orderSplitMap.put(oldId, orderSplit.getId());
            }

            Map<Integer, Integer> orderBillMap = new HashMap<>();
            for (OrderBill orderBill : orderBills) {
                int oldBilNo = orderBill.getBillNo();
                orderBill.setId(CommonSQL.getNextSeq(TableNames.OrderBill));
                orderBill.setOrderId(subOrder.getId());
                orderBill.setCreateTime(System.currentTimeMillis());
                orderBill.setUpdateTime(System.currentTimeMillis());
                orderBill.setBillNo(RevenueCenterSQL.getBillNoFromRevenueCenter(subOrder.getRevenueId()));
                Integer orderSplitId = orderBill.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    orderBill.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                OrderBillSQL.add(orderBill);
                orderBillMap.put(oldBilNo, orderBill.getBillNo());
            }

            Map<Integer, Integer> paymentMap = new HashMap<>();
            for (Payment payment : payments) {
                int oldId = payment.getId();
                payment.setId(CommonSQL.getNextSeq(TableNames.Payment));
                payment.setOrderId(subOrder.getId());
                payment.setCreateTime(System.currentTimeMillis());
                payment.setUpdateTime(System.currentTimeMillis());
                payment.setBusinessDate(business);
                if (orderBillMap.containsKey(payment.getBillNo())) {
                    payment.setBillNo(orderBillMap.get(payment.getBillNo()));
                }
                Integer orderSplitId = payment.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    payment.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                PaymentSQL.addPayment(payment);
                paymentMap.put(oldId, payment.getId());
            }

            for (RoundAmount roundAmount : roundAmounts) {
                roundAmount.setId(CommonSQL.getNextSeq(TableNames.RoundAmount));
                roundAmount.setOrderId(subOrder.getId());
                roundAmount.setCreateTime(System.currentTimeMillis());
                roundAmount.setUpdateTime(System.currentTimeMillis());
                roundAmount.setBusinessDate(business);
                Integer orderSplitId = roundAmount.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    roundAmount.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                RoundAmountSQL.update(roundAmount);
            }

            Map<Integer, Integer> orderDetailMap = new HashMap<>();
            for (OrderDetail orderDetail : orderDetails) {
                int oldId = orderDetail.getId();
                orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
                orderDetail.setOrderId(subOrder.getId());
                orderDetail.setCreateTime(System.currentTimeMillis());
                orderDetail.setUpdateTime(System.currentTimeMillis());
                Integer orderSplitId = orderDetail.getOrderSplitId();
                if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                    orderDetail.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                }
                OrderDetailSQL.updateOrderDetail(orderDetail);
                orderDetailMap.put(oldId, orderDetail.getId());
            }

            for (OrderModifier orderModifier : orderModifiers) {
                orderModifier.setId(CommonSQL.getNextSeq(TableNames.OrderModifier));
                orderModifier.setCreateTime(System.currentTimeMillis());
                orderModifier.setUpdateTime(System.currentTimeMillis());
                Integer orderDetailId = orderModifier.getOrderDetailId();
                if (orderDetailId != null && orderDetailMap.containsKey(orderDetailId.intValue())) {
                    orderModifier.setOrderDetailId(orderDetailMap.get(orderDetailId.intValue()));
                }
                orderModifier.setOrderId(subOrder.getId());
                OrderModifierSQL.updateOrderModifier(orderModifier);
            }
            for (OrderDetailTax orderDetailTax : orderDetailTaxs) {
                orderDetailTax.setId(CommonSQL.getNextSeq(TableNames.OrderDetailTax));
                orderDetailTax.setCreateTime(System.currentTimeMillis());
                orderDetailTax.setUpdateTime(System.currentTimeMillis());
                Integer orderDetailId = orderDetailTax.getOrderDetailId();
                if (orderDetailId != null && orderDetailMap.containsKey(orderDetailId.intValue())) {
                    orderDetailTax.setOrderDetailId(orderDetailMap.get(orderDetailId.intValue()));
                }
                orderDetailTax.setOrderId(subOrder.getId());
                OrderDetailTaxSQL.updateOrderDetailTax(orderDetailTax);
            }

            for (PaymentSettlement paymentSettlement : paymentSettlements) {
                paymentSettlement.setId(CommonSQL.getNextSeq(TableNames.PaymentSettlement));
                paymentSettlement.setCreateTime(System.currentTimeMillis());
                paymentSettlement.setUpdateTime(System.currentTimeMillis());
                if (orderBillMap.containsKey(paymentSettlement.getBillNo())) {
                    paymentSettlement.setBillNo(orderBillMap.get(paymentSettlement.getBillNo()));
                }
                Integer paymentId = paymentSettlement.getPaymentId();
                if (paymentId != null && paymentMap.containsKey(paymentId.intValue())) {
                    paymentSettlement.setPaymentId(paymentMap.get(paymentId.intValue()));
                    if (!TextUtils.isEmpty(cardNum)) {
                        switch (paymentSettlement.getPaymentTypeId()) {
                            case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
                            case ParamConst.SETTLEMENT_TYPE_UNIPAY:
                            case ParamConst.SETTLEMENT_TYPE_VISA:
                            case ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL:
                            case ParamConst.SETTLEMENT_TYPE_AMEX:
                            case ParamConst.SETTLEMENT_TYPE_JCB:
                                getCardsSettlementForKPMG(paymentId, paymentSettlement,
                                        paymentSettlement.getPaymentTypeId(), cardNum, 123);
                                break;
                            case ParamConst.SETTLEMENT_TYPE_NETS:

                                break;

                        }
                    }
                }
                PaymentSettlementSQL.addPaymentSettlement(paymentSettlement);
            }
        }
        return subOrder;
    }

    public Order cpOrderInfoLog(SQLiteDatabase db, Order subOrder, List<OrderSplit> orderSplits, List<Payment> payments,
                                List<PaymentSettlement> paymentSettlements, List<RoundAmount> roundAmounts) throws Exception {

        synchronized (lock_cpOrderInfo) {
            if (subOrder != null) {
                CPPaymentSettlementSQL.deletePaymentSettlementByOrderId(db, subOrder.getId().intValue());
                CPPaymentSQL.deletePayment(db, subOrder.getId().intValue());
                CPRoundAmountSQL.deleteRoundAmount(db, subOrder);
                Map<Integer, Integer> orderSplitMap = new HashMap<>();
                for (OrderSplit orderSplit : orderSplits) {
                    orderSplitMap.put(orderSplit.getOldOrderSplitId(), orderSplit.getId());
                }
                Map<Integer, Integer> paymentMap = new HashMap<>();
                for (Payment payment : payments) {
                    int oldId = payment.getId();
                    payment.setId(CommonSQL.getNextSeq(TableNames.CPPayment));
                    payment.setOrderId(subOrder.getId());
                    Integer orderSplitId = payment.getOrderSplitId();
                    if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                        payment.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                    }
                    CPPaymentSQL.addPayment(db, payment);
                    paymentMap.put(oldId, payment.getId());
                }

                for (RoundAmount roundAmount : roundAmounts) {
                    roundAmount.setId(CommonSQL.getNextSeq(TableNames.CPRoundAmount));
                    roundAmount.setOrderId(subOrder.getId());
                    Integer orderSplitId = roundAmount.getOrderSplitId();
                    if (orderSplitId != null && orderSplitMap.containsKey(orderSplitId.intValue())) {
                        roundAmount.setOrderSplitId(orderSplitMap.get(orderSplitId.intValue()));
                    }
                    CPRoundAmountSQL.update(db, roundAmount);
                }

                for (PaymentSettlement paymentSettlement : paymentSettlements) {
                    paymentSettlement.setId(CommonSQL.getNextSeq(TableNames.CPPaymentSettlement));
                    Integer paymentId = paymentSettlement.getPaymentId();
                    if (paymentId != null && paymentMap.containsKey(paymentId.intValue())) {
                        paymentSettlement.setPaymentId(paymentMap.get(paymentId.intValue()));
                    }
                    CPPaymentSettlementSQL.addPaymentSettlement(db, paymentSettlement);
                }
            }
        }
        return subOrder;
    }

    public Order getOrder(Integer orderOriginId, int subPosBeanId, TableInfo tables,
                          RevenueCenter revenueCenter, User user,
                          SessionStatus sessionStatus, long businessDate, int orderNOTitle,
                          int orderStatus, Tax inclusiveTax, int appOrderId, String waiterName) {

        Order order = null;
        int posId = 0;
        int placesId = 0;
        int pack = 4;
        if (tables != null) {
            if (!IntegerUtils.isEmptyOrZero(tables.getPosId())) {
                posId = tables.getPosId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPlacesId())) {
                placesId = tables.getPlacesId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPacks())) {
                pack = tables.getPacks();
            }
        }
        synchronized (lock_order) {
            order = OrderSQL.getUnfinishedOrderAtTable(posId, businessDate, sessionStatus);
            if (order == null) {

                order = new Order();
                order.setId(CommonSQL.getNextSeq(TableNames.Order));
                order.setOrderOriginId(orderOriginId);
                order.setUserId(user.getId());
                order.setPersons(pack);
                order.setOrderStatus(orderStatus);
                order.setDiscountRate(ParamConst.DOUBLE_ZERO);
                try {
                    order.setSessionStatus(sessionStatus.getSession_status());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                order.setRestId(CoreData.getInstance().getRestaurant().getId());
                order.setRevenueId(revenueCenter.getId());
                order.setPlaceId(placesId);
                order.setTableId(posId);
                long time = System.currentTimeMillis();
                order.setCreateTime(time);
                order.setUpdateTime(time);
                order.setBusinessDate(businessDate);
//					order.setOrderNo(order.getId());
                order.setOrderNo(OrderHelper.calculateOrderNo(businessDate));//流水号
                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_NULL);
                order.setAppOrderId(appOrderId);
                if (inclusiveTax != null) {
                    order.setInclusiveTaxName(inclusiveTax.getTaxName());
                    order.setInclusiveTaxPercentage(inclusiveTax.getTaxPercentage());
                }
                if (subPosBeanId > 0) {
                    SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanById(subPosBeanId);
                    order.setNumTag(subPosBean.getNumTag());
                }
                order.setSubPosBeanId(subPosBeanId);
                order.setWaiterInformation(waiterName);
                order.setIsWaiterPrint(0);
                OrderSQL.addOrder(order);
            } else if (order.getPersons().intValue() != pack) {
                order.setPersons(pack);
                OrderSQL.updateOrderPersions(pack, order.getId());
            }
        }
        return order;
    }

    public Order addOrderFromKioskDesktop(Integer orderOriginId, int subPosBeanId, TableInfo tables,
                                          RevenueCenter revenueCenter, User user,
                                          SessionStatus sessionStatus, long businessDate, Tax inclusiveTax) {

        Order order = null;
        int posId = 0;
        int placesId = 0;
        int pack = 4;
        if (tables != null) {
            if (!IntegerUtils.isEmptyOrZero(tables.getPosId())) {
                posId = tables.getPosId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPlacesId())) {
                placesId = tables.getPlacesId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPacks())) {
                pack = tables.getPacks();
            }
        }
        synchronized (lock_order) {
            if (order == null) {
                order = new Order();
                order.setId(CommonSQL.getNextSeq(TableNames.Order));
                order.setOrderOriginId(orderOriginId);
                order.setUserId(user.getId());
                order.setPersons(pack);
                order.setOrderStatus(ParamConst.ORDER_STATUS_KIOSK);
                order.setDiscountRate(ParamConst.DOUBLE_ZERO);
                try {
                    order.setSessionStatus(sessionStatus.getSession_status());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                order.setRestId(CoreData.getInstance().getRestaurant().getId());
                order.setRevenueId(revenueCenter.getId());
                order.setPlaceId(placesId);
                order.setTableId(posId);
                long time = System.currentTimeMillis();
                order.setCreateTime(time);
                order.setUpdateTime(time);
                order.setBusinessDate(businessDate);
                order.setOrderNo(OrderHelper.calculateOrderNo(businessDate));//流水号
                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_NULL);
                order.setAppOrderId(0);
                if (inclusiveTax != null) {
                    order.setInclusiveTaxName(inclusiveTax.getTaxName());
                    order.setInclusiveTaxPercentage(inclusiveTax.getTaxPercentage());
                }
                if (subPosBeanId > 0) {
                    SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanById(subPosBeanId);
                    order.setNumTag(subPosBean.getNumTag());
                }
                order.setSubPosBeanId(subPosBeanId);
                OrderSQL.addOrder(order);
            }
        }
        return order;
    }

    public Order getOrderFromAppOrder(int subPosBeanId, AppOrder appOrder, User user,
                                      SessionStatus sessionStatus, RevenueCenter revenueCenter,
                                      TableInfo tables, long businessDate, Restaurant restaurant,
                                      Tax inclusiveTax, boolean isKiosk) {
        Order order = null;
        int posId = 0;
        int placesId = 0;
        int pack = 4;
        if (tables != null) {
            if (!IntegerUtils.isEmptyOrZero(tables.getPosId())) {
                posId = tables.getPosId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPlacesId())) {
                placesId = tables.getPlacesId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPacks())) {
                pack = tables.getPacks();
            }
        }
        if (appOrder != null) {
            synchronized (lock_order) {
                try {
                    order = OrderSQL.getOrderByAppOrderId(appOrder
                            .getId().intValue());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (order == null) {
                    order = new Order();
                    order.setId(CommonSQL.getNextSeq(TableNames.Order));
                    order.setOrderOriginId(ParamConst.ORDER_ORIGIN_APP);
                    order.setUserId(user.getId());
                    order.setPersons(appOrder.getPerson() > 0 ? appOrder.getPerson() : 4);
                    if (isKiosk)
                        order.setOrderStatus(ParamConst.ORDER_STATUS_FINISHED);
                    else
                        order.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS);
                    order.setDiscountRate(ParamConst.DOUBLE_ZERO);
                    order.setTaxAmount(appOrder.getTaxAmount());
                    try {
                        order.setSessionStatus(sessionStatus.getSession_status());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    order.setRestId(restaurant.getId());
                    order.setRevenueId(revenueCenter.getId());
                    order.setPlaceId(placesId);
                    order.setTableId(posId);
                    long time = System.currentTimeMillis();
                    order.setCreateTime(time);
                    order.setUpdateTime(time);
                    order.setBusinessDate(businessDate);
                    order.setOrderNo(OrderHelper.calculateOrderNo(businessDate));// 流水号
                    order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_NULL);
                    order.setAppOrderId(appOrder.getId().intValue());
                    order.setTotal(appOrder.getTotal());
                    order.setSubTotal(appOrder.getSubTotal());
                    order.setOrderRemark(appOrder.getOrderRemark());

                    //   1 堂吃, 2 打包, 3 外卖
                    if (appOrder.getEatType() == ParamConst.TAKE_AWAY) {
                        order.setIsTakeAway(ParamConst.TAKE_AWAY);
                    } else if (appOrder.getEatType() == ParamConst.APP_DELIVERY) {
                        order.setIsTakeAway(ParamConst.APP_DELIVERY);
                    } else {
                        order.setIsTakeAway(ParamConst.DINE_IN);
                    }
                    if (inclusiveTax != null) {
                        order.setInclusiveTaxName(inclusiveTax.getTaxName());
                        order.setInclusiveTaxPercentage(inclusiveTax.getTaxPercentage());
                    }
                    order.setDiscountAmount("0.00");
                    if (subPosBeanId > 0) {
                        SubPosBean subPosBean = SubPosBeanSQL.getSubPosBeanById(subPosBeanId);
                        order.setNumTag(subPosBean.getNumTag());
                    }
                    order.setSubPosBeanId(subPosBeanId);
                    OrderHelper.setOrderInclusiveTaxPrice(order);
                    OrderSQL.update(order);
                }
            }
        }
        return order;
    }

    Object lock_orderDetail = new Object();

    public OrderDetail getOrderDetail(Order order, ItemDetail itemDetail,
                                      int groupId) {
        OrderDetail orderDetail = new OrderDetail();
        synchronized (lock_orderDetail) {
            long time = System.currentTimeMillis();
            orderDetail.setCreateTime(time);
            orderDetail.setUpdateTime(time);
            orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
            orderDetail.setOrderId(order.getId());
            orderDetail.setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
            orderDetail.setUserId(order.getUserId());
            orderDetail.setItemId(itemDetail.getId());
            orderDetail.setItemName(itemDetail.getItemName());
            orderDetail.setItemNum(1);
            orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
            orderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_GENERAL);
            orderDetail.setReason("");
            orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
            orderDetail.setItemPrice(itemDetail.getPrice());
            orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setFromOrderDetailId(0);
            orderDetail.setIsFree(ParamConst.NOT_FREE);
            orderDetail.setIsItemDiscount(itemDetail.getIsDiscount());
            orderDetail.setAppOrderDetailId(0);
            if (itemDetail.getItemType() == 2) {
                orderDetail.setIsOpenItem(1);
            }
            orderDetail.setGroupId(groupId);
            orderDetail.setOrderSplitId(0);
            orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
            orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
            if (itemDetail.getItemType() == 3)
                orderDetail.setIsSet(1);
        }
        return orderDetail;
    }

    public OrderDetail getOrderDetailAndPromotion(Order order, ItemDetail itemDetail,
                                                  int groupId, Promotion promotion) {
        OrderDetail orderDetail = new OrderDetail();
        synchronized (lock_orderDetail) {
            long time = System.currentTimeMillis();
            orderDetail.setCreateTime(time);
            orderDetail.setUpdateTime(time);
            orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
            orderDetail.setOrderId(order.getId());
            orderDetail.setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
            orderDetail.setUserId(order.getUserId());
            orderDetail.setItemId(itemDetail.getId());
            orderDetail.setItemName(itemDetail.getItemName());
            orderDetail.setItemNum(promotion.getItemNum());
            orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
            orderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_GENERAL);
            orderDetail.setReason("");
            orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
            orderDetail.setItemPrice(itemDetail.getPrice());
            orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setFromOrderDetailId(0);
            orderDetail.setIsFree(ParamConst.FREE);
            orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
            orderDetail.setIsItemDiscount(itemDetail.getIsDiscount());
            orderDetail.setAppOrderDetailId(0);
            if (itemDetail.getItemType() == 2) {
                orderDetail.setIsOpenItem(1);
            }
            orderDetail.setGroupId(groupId);
            orderDetail.setOrderSplitId(0);
            orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
            orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
            if (itemDetail.getItemType() == 3)
                orderDetail.setIsSet(1);
        }
        return orderDetail;
    }

    public OrderDetail getOrderDetailFromKiosk(Order order, OrderDetail orderDetail) {
        synchronized (lock_orderDetail) {
            long time = System.currentTimeMillis();
            orderDetail.setCreateTime(time);
            orderDetail.setUpdateTime(time);
            int orderDetailId = CommonSQL
                    .getNextSeq(TableNames.OrderDetail);
            orderDetail
                    .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
            orderDetail.setId(orderDetailId);
            orderDetail.setOrderId(order.getId().intValue());
            OrderDetailSQL.updateOrderDetail(orderDetail);
        }
        return orderDetail;
    }

    // use in  transfer item feature
    public OrderDetail cpOrderDetail(OrderDetail cpOrderDetail) {
        OrderDetail orderDetail = new OrderDetail();
        synchronized (lock_orderDetail) {
            orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
            orderDetail.setCreateTime(cpOrderDetail.getCreateTime());
            orderDetail.setUpdateTime(cpOrderDetail.getUpdateTime());
            orderDetail.setOrderId(cpOrderDetail.getOrderId());
            orderDetail.setOrderOriginId(cpOrderDetail.getOrderOriginId());
            orderDetail.setOrderSplitId(0);
            orderDetail.setUserId(cpOrderDetail.getUserId());
            orderDetail.setItemId(cpOrderDetail.getItemId());
            orderDetail.setItemName(cpOrderDetail.getItemName());
            orderDetail.setItemNum(cpOrderDetail.getItemNum());
            orderDetail.setOrderDetailStatus(cpOrderDetail.getOrderDetailStatus());
            orderDetail.setOrderDetailType(cpOrderDetail.getOrderDetailType());
            orderDetail.setReason(cpOrderDetail.getReason());
            orderDetail.setPrintStatus(cpOrderDetail.getPrintStatus());
            orderDetail.setItemPrice(cpOrderDetail.getItemPrice());
            orderDetail.setTaxPrice(cpOrderDetail.getTaxPrice());
            orderDetail.setFromOrderDetailId(cpOrderDetail.getFromOrderDetailId());
            orderDetail.setIsFree(cpOrderDetail.getIsFree());
            orderDetail.setIsItemDiscount(cpOrderDetail.getIsItemDiscount());
            orderDetail.setAppOrderDetailId(cpOrderDetail.getAppOrderDetailId());
            orderDetail.setIsOpenItem(cpOrderDetail.getIsOpenItem());
            orderDetail.setGroupId(cpOrderDetail.getGroupId());
            orderDetail.setIsTakeAway(cpOrderDetail.getIsTakeAway());
            orderDetail.setMainCategoryId(cpOrderDetail.getMainCategoryId());
            orderDetail.setIsSet(cpOrderDetail.getIsSet());
            orderDetail.setTransferFromDetailId(cpOrderDetail.getTransferFromDetailId());
            OrderDetailSQL.updateOrderDetail(orderDetail);
            int transferId = cpOrderDetail.getTransferFromDetailId();
            if (transferId > 0) {
                List<OrderModifier> orderModifiers = OrderModifierSQL.getOrderModifiersByOrderDetailId(transferId);
                for (OrderModifier orderModifier : orderModifiers) {
                    if (cpOrderDetail.getTransferFromDetailNum() > 0) {
                        int oldOrderModifierNum = orderModifier.getModifierNum();
                        BigDecimal oldOrderModifierPrice = BH.getBD(orderModifier.getModifierPrice());
                        int newNum = oldOrderModifierNum * orderDetail.getItemNum().intValue() / cpOrderDetail.getTransferFromDetailNum();
                        BigDecimal newPrice = BH.div(BH.mul(BH.getBD(orderDetail.getItemNum().intValue()), oldOrderModifierPrice, false), BH.getBD(cpOrderDetail.getTransferFromDetailNum()), true);

                        orderModifier.setModifierNum(oldOrderModifierNum - newNum);
                        orderModifier.setModifierPrice(BH.sub(oldOrderModifierPrice, newPrice, true).toString());
                        OrderModifierSQL.updateOrderModifier(orderModifier);
                        orderModifier.setModifierNum(newNum);
                        orderModifier.setModifierPrice(newPrice.toString());
                    }
                    orderModifier.setOrderDetailId(orderDetail.getId());
                    orderModifier.setOrderId(orderDetail.getOrderId());
                    OrderModifierSQL.addOrderModifier(orderModifier);
                }
                OrderDetailSQL.updateOrderDetailAndOrder(orderDetail);
                OrderDetail oldOrderDetail = OrderDetailSQL.getOrderDetail(transferId);
                if (oldOrderDetail != null) {
                    OrderDetailSQL.updateOrderDetailAndOrder(oldOrderDetail);
                }
            }
        }
        return orderDetail;
    }

    public OrderDetail getOrderDetailFromTempAppOrderDetail(Order order,
                                                            AppOrderDetail appOrderDetail) {
        OrderDetail orderDetail;
        synchronized (lock_orderDetail) {
            long time = System.currentTimeMillis();
            orderDetail = OrderDetailSQL.getOrderDetailByAppOrderDetailId(appOrderDetail.getId());
            if (orderDetail == null) {
                orderDetail = new OrderDetail();
                orderDetail.setCreateTime(time);
                orderDetail.setUpdateTime(time);
                orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
                orderDetail.setOrderId(order.getId());
                orderDetail.setOrderOriginId(ParamConst.ORDER_ORIGIN_APP);
                orderDetail.setUserId(order.getUserId());
                orderDetail.setItemId(appOrderDetail.getItemId().intValue());
                orderDetail.setItemName(appOrderDetail.getItemName());
                orderDetail.setItemNum(appOrderDetail.getItemNum().intValue());
                orderDetail
                        .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
                orderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_GENERAL);
                orderDetail.setReason("");
                orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
                orderDetail.setItemPrice(appOrderDetail.getItemPrice());
                String taxPrice = AppOrderDetailTaxSQL.getAppOrderDetailTaxSumByAppOrderDetailId(appOrderDetail.getId().intValue());
                orderDetail.setTaxPrice(taxPrice);
                orderDetail.setFromOrderDetailId(0);
                orderDetail.setIsFree(ParamConst.NOT_FREE);
                orderDetail.setIsItemDiscount(1);
                orderDetail.setRealPrice(appOrderDetail.getTotalItemPrice());
                orderDetail.setGroupId(0);
                orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(appOrderDetail.getItemId().intValue(), appOrderDetail.getItemName());
                orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
                orderDetail.setAppOrderDetailId(appOrderDetail.getId());
            }
        }
        return orderDetail;
    }

    public OrderDetail getOrderDetailForTransferTable(Order order,
                                                      OrderDetail oldOrderDetail) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
        orderDetail.setOrderId(order.getId());
        orderDetail.setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
        orderDetail.setUserId(oldOrderDetail.getUserId());
        orderDetail.setItemName(oldOrderDetail.getItemName());
        orderDetail.setItemId(oldOrderDetail.getItemId());
        orderDetail.setItemNum(oldOrderDetail.getItemNum());
        orderDetail.setOrderDetailStatus(oldOrderDetail.getOrderDetailStatus());
        orderDetail.setOrderDetailType(oldOrderDetail.getOrderDetailType());
        orderDetail.setReason("");
        orderDetail.setPrintStatus(oldOrderDetail.getPrintStatus());
        orderDetail.setItemPrice(oldOrderDetail.getItemPrice());
        orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
//		orderDetail.setDiscountPrice(oldOrderDetail.getDiscountPrice());
//		orderDetail.setModifierPrice(oldOrderDetail.getModifierPrice());
//		orderDetail.setRealPrice(oldOrderDetail.getRealPrice());
        long time = System.currentTimeMillis();
        orderDetail.setCreateTime(time);
        orderDetail.setUpdateTime(time);
        orderDetail.setFromOrderDetailId(0);
        orderDetail.setIsFree(ParamConst.NOT_FREE);
        orderDetail.setIsOpenItem(oldOrderDetail.getIsOpenItem());
        orderDetail.setGroupId(ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID);
        orderDetail.setIsTakeAway(oldOrderDetail.getIsTakeAway());
        orderDetail.setIsItemDiscount(oldOrderDetail.getIsItemDiscount());
        orderDetail.setAppOrderDetailId(oldOrderDetail.getAppOrderDetailId());
        orderDetail.setMainCategoryId(oldOrderDetail.getMainCategoryId());
        orderDetail.setWeight(oldOrderDetail.getWeight());

        return orderDetail;
    }

    public OrderDetail createOrderDetailForWaiter(Order order,
                                                  ItemDetail itemDetail, int groupId, User user) {
        long time = Math.abs(System.currentTimeMillis());
        int orderDetailId = CommonSQL.getNextSeq(TableNames.OrderDetail);
        if (orderDetailId < 1000000) {
            orderDetailId += 1000000;
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(orderDetailId);
        orderDetail.setOrderId(order.getId());
        orderDetail.setOrderOriginId(ParamConst.ORDER_ORIGIN_WAITER);
        orderDetail.setUserId(user.getId());
        orderDetail.setItemId(itemDetail.getId());
        orderDetail.setItemName(itemDetail.getItemName());
        orderDetail.setItemNum(1);
        orderDetail.setItemUrl(itemDetail.getImgUrl());
        orderDetail
                .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
        orderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_GENERAL);
        orderDetail.setReason("");
        orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
        orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
        orderDetail.setItemPrice(itemDetail.getPrice());
        orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
        orderDetail.setCreateTime(time);
        orderDetail.setUpdateTime(time);
        orderDetail.setFromOrderDetailId(0);
        orderDetail.setIsFree(ParamConst.NOT_FREE);
        orderDetail.setIsItemDiscount(itemDetail.getIsDiscount());
        if (itemDetail.getItemType() == 2) {
            orderDetail.setIsOpenItem(1);
        }
        orderDetail.setGroupId(groupId);
        orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
        orderDetail.setAppOrderDetailId(0);
        orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
        orderDetail.setBarCode(itemDetail.getBarcode());
        return orderDetail;
    }

    Object lock_table = new Object();

    public TableInfo addNewTable(String imageName, int restaurantId, int revenueId, int placeId, int width, int height) {
        synchronized (lock_table) {
            TableInfo newTable = new TableInfo();
            newTable.setPosId(CommonSQL.getNextSeq(TableNames.TableInfo));
            newTable.setImageName(imageName);
            newTable.setRestaurantId(restaurantId);
            newTable.setRevenueId(revenueId);
            newTable.setPlacesId(placeId);
            newTable.setStatus(ParamConst.TABLE_STATUS_IDLE);
            newTable.setShape(3);
            newTable.setIsDecorate(0);
            newTable.setUnionId(restaurantId + "_" + revenueId + "_" + newTable.getPosId());
            newTable.setIsActive(ParamConst.ACTIVE_NOMAL);
            newTable.setResolution(width);
            newTable.setResolutionWidth(width);
            newTable.setResolutionHeight(height);
            newTable.setName(BaseApplication.getTopActivity().getString(R.string.table) + " " + newTable.getPosId().intValue());
//			if(imageName.startsWith("table_1"))
//				newTable.setPacks(1);
//			else if(imageName.startsWith("table_2"))
//				newTable.setPacks(2);
//			else if(imageName.startsWith("table_4"))
//				newTable.setPacks(4);
//			else if(imageName.startsWith("table_6"))
//				newTable.setPacks(6);
//			else
//				newTable.setPacks(8);

            newTable.setRotate(0);
            long time = System.currentTimeMillis();
            newTable.setCreateTime(time);
            newTable.setUpdateTime(time);
            TableInfoSQL.addTables(newTable);
            return newTable;
        }
    }

    Object lock_place = new Object();

    public PlaceInfo addNewPlace(int restaurantId, int revenueId, String placeName) {
        synchronized (lock_place) {
            PlaceInfo placeInfo = new PlaceInfo();
            placeInfo.setId(CommonSQL.getNextSeq(TableNames.PlaceInfo));
            placeInfo.setIsActive(ParamConst.ACTIVE_NOMAL);
            placeInfo.setRestaurantId(restaurantId);
            placeInfo.setRevenueId(revenueId);
            placeInfo.setPlaceDescription("");
            placeInfo.setPlaceName(placeName);
            placeInfo.setUnionId(restaurantId + "_" + revenueId + "_" + placeInfo.getId());
            PlaceInfoSQL.addPlaceInfo(placeInfo);
            return placeInfo;
        }
    }

    public TableInfo addNewWaitingList(String name, int restaurantId, int revenueId, int placeId) {
        return new TableInfo(name, restaurantId, revenueId, placeId);
    }

    Object lock_getRoundAmount = new Object();

    public RoundAmount getRoundAmount(Order order, OrderBill orderBill, BigDecimal roundBeforePrice, String roundType) {
        RoundAmount roundAmount = null;
        synchronized (lock_getRoundAmount) {
            roundAmount = RoundAmountSQL.getRoundAmountByOrderAndBill(order, orderBill);
            long time = System.currentTimeMillis();
            if (roundAmount == null) {
                roundAmount = new RoundAmount();
                roundAmount.setId(CommonSQL.getNextSeq(TableNames.RoundAmount));
                roundAmount.setOrderId(order.getId());
                roundAmount.setBillNo(orderBill.getBillNo());
                BigDecimal roundAlfterPrice = RoundUtil.getPriceAfterRound(roundType, roundBeforePrice);
                BigDecimal roundBalancePrice = BH.sub(roundAlfterPrice, roundBeforePrice, false);
                roundAmount.setRoundBeforePrice(roundBeforePrice.toString());
                roundAmount.setRoundAlfterPrice(roundAlfterPrice.toString());
                roundAmount.setRoundBalancePrice(Double.valueOf(roundBalancePrice
                        .toString()));
                roundAmount.setRestId(order.getRestId());
                roundAmount.setRevenueId(order.getRevenueId());
                roundAmount.setTableId(order.getTableId());
                roundAmount.setBusinessDate(order.getBusinessDate());
                roundAmount.setCreateTime(time);
                roundAmount.setUpdateTime(time);
                roundAmount.setOrderSplitId(0);
                RoundAmountSQL.update(roundAmount);
            } else {
                BigDecimal roundAlfterPrice = RoundUtil.getPriceAfterRound(roundType, roundBeforePrice);
                BigDecimal roundBalancePrice = BH.sub(roundAlfterPrice, roundBeforePrice, false);
                roundAmount.setRoundBeforePrice(roundBeforePrice.toString());
                roundAmount.setRoundAlfterPrice(roundAlfterPrice.toString());
                roundAmount.setRoundBalancePrice(Double.valueOf(roundBalancePrice
                        .toString()));
                roundAmount.setUpdateTime(time);
                RoundAmountSQL.update(roundAmount);
            }
        }
        return roundAmount;
    }

    public RoundAmount getRoundAmountByOrderSplit(OrderSplit orderSplit, OrderBill orderBill, BigDecimal roundBeforePrice, String roundType, long businessDate) {
        RoundAmount roundAmount = null;
        synchronized (lock_getRoundAmount) {
            roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(orderSplit, orderBill);
            long time = System.currentTimeMillis();
            if (roundAmount == null) {
                roundAmount = new RoundAmount();
                roundAmount.setId(CommonSQL.getNextSeq(TableNames.RoundAmount));
                roundAmount.setOrderId(orderSplit.getOrderId());
                roundAmount.setOrderSplitId(orderSplit.getId());
                roundAmount.setBillNo(orderBill.getBillNo());
                BigDecimal roundAlfterPrice = RoundUtil.getPriceAfterRound(roundType, roundBeforePrice);
                BigDecimal roundBalancePrice = BH.sub(roundAlfterPrice, roundBeforePrice, false);
                roundAmount.setRoundBeforePrice(roundBeforePrice.toString());
                roundAmount.setRoundAlfterPrice(roundAlfterPrice.toString());
                roundAmount.setRoundBalancePrice(Double.valueOf(roundBalancePrice
                        .toString()));
                roundAmount.setRestId(orderSplit.getRestId());
                roundAmount.setRevenueId(orderSplit.getRevenueId());
                roundAmount.setTableId(orderSplit.getTableId());
                roundAmount.setBusinessDate(businessDate);
                roundAmount.setCreateTime(time);
                roundAmount.setUpdateTime(time);
                RoundAmountSQL.update(roundAmount);
            } else {
                BigDecimal roundAlfterPrice = RoundUtil.getPriceAfterRound(roundType, roundBeforePrice);
                BigDecimal roundBalancePrice = BH.sub(roundAlfterPrice, roundBeforePrice, false);
                roundAmount.setRoundBeforePrice(roundBeforePrice.toString());
                roundAmount.setRoundAlfterPrice(roundAlfterPrice.toString());
                roundAmount.setRoundBalancePrice(Double.valueOf(roundBalancePrice
                        .toString()));
                roundAmount.setUpdateTime(time);
                RoundAmountSQL.update(roundAmount);
            }
        }
        return roundAmount;
    }

    private static BigDecimal round(String roundType,
                                    BigDecimal roundBeforePrice) {
        if (roundType == null) {
            return roundBeforePrice;
        }
        if (roundType.equalsIgnoreCase(ParamConst.ROUND_10CENTS)) {
            DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
            BigDecimal bigDecimal = BH.div(roundBeforePrice, BH.getBD("0.1"),
                    false);
            return BH.mul(BH.getBD(doubleFormat.format(bigDecimal)),
                    BH.getBD("0.1"), true);
        } else if (roundType.equalsIgnoreCase(
                ParamConst.ROUND_1DOLLAR)) {
            DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
            BigDecimal bigDecimal = BH.div(roundBeforePrice, BH.getBD("1.0"),
                    false);
            return BH.mul(BH.getBD(doubleFormat.format(bigDecimal)),
                    BH.getBD("1.0"), true);
        } else if (roundType.equalsIgnoreCase(
                ParamConst.ROUND_5CENTS)) {
            DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
            BigDecimal bigDecimal = BH.div(roundBeforePrice, BH.getBD("0.05"),
                    false);
            return BH.mul(BH.getBD(doubleFormat.format(bigDecimal)),
                    BH.getBD("0.05"), true);
        } else if (roundType.equalsIgnoreCase(
                ParamConst.ROUND_10CENTS_DOWN)) {
//			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
            BigDecimal bigDecimal = BH.div(roundBeforePrice, BH.getBD("0.1"),
                    false);
            return BH.mul(BH.getBD(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).toString()),
                    BH.getBD("0.1"), true);
        } else if (roundType.equalsIgnoreCase(
                ParamConst.ROUND_5CENTS_DOWN)) {
            BigDecimal bigDecimal = BH.div(roundBeforePrice, BH.getBD("0.05"),
                    false);
            return BH.mul(BH.getBD(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).toString()),
                    BH.getBD("0.05"), true);
        } else if (roundType.equalsIgnoreCase(
                ParamConst.ROUND_1DOLLAR_DOWN)) {
            BigDecimal bigDecimal = BH.div(roundBeforePrice, BH.getBD("0.1"),
                    false);
            return BH.mul(BH.getBD(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).toString()),
                    BH.getBD("1.0"), true);
        } else {
            return roundBeforePrice;
        }

    }

    // add:thread safe
    Object lock_free_order_detail = new Object();

    public OrderDetail getFreeOrderDetail(Order order,
                                          OrderDetail fromOrderDetail, ItemDetail itemDetail,
                                          ItemHappyHour itemHappyHour) {

        OrderDetail orderDetail = null;
        synchronized (lock_free_order_detail) {
            orderDetail = OrderDetailSQL.getOrderDetail(order.getId(),
                    fromOrderDetail);
            if (orderDetail == null) {
                orderDetail = new OrderDetail();
                orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
                orderDetail.setOrderId(order.getId());
                orderDetail.setOrderOriginId(fromOrderDetail.getOrderOriginId());
                orderDetail.setUserId(order.getUserId());
                orderDetail.setItemId(itemDetail.getId());
                orderDetail.setItemName(itemDetail.getItemName());
                orderDetail.setItemNum(itemHappyHour.getFreeNum()
                        * fromOrderDetail.getItemNum());
                orderDetail.setOrderDetailStatus(fromOrderDetail
                        .getOrderDetailStatus());
                orderDetail
                        .setOrderDetailType(fromOrderDetail.getOrderDetailType());
                orderDetail.setReason("");
                orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
                orderDetail.setItemPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
                orderDetail
                        .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
                orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
                long time = System.currentTimeMillis();
                orderDetail.setCreateTime(time);
                orderDetail.setUpdateTime(time);
                orderDetail.setFromOrderDetailId(fromOrderDetail.getId());
                orderDetail.setIsFree(ParamConst.FREE);
                orderDetail.setGroupId(fromOrderDetail.getGroupId());

                orderDetail.setModifierPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setRealPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setOrderSplitId(fromOrderDetail.getOrderSplitId());
                orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                orderDetail.setAppOrderDetailId(0);
                orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
            } else {
                orderDetail.setItemNum(itemHappyHour.getFreeNum()
                        * fromOrderDetail.getItemNum());
            }
            OrderDetailSQL.updateOrderDetail(orderDetail);
        }
        return orderDetail;
    }


    public OrderDetail getItemFreeOrderDetail(Order order,
                                              OrderDetail fromOrderDetail, ItemDetail itemDetail,
                                              ItemPromotion itemPromotion) {

        OrderDetail orderDetail = null;
        synchronized (lock_free_order_detail) {
            orderDetail = OrderDetailSQL.getOrderDetail(order.getId(),
                    fromOrderDetail);
            if (orderDetail == null) {
                orderDetail = new OrderDetail();
                orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
                orderDetail.setOrderId(order.getId());
                orderDetail.setOrderOriginId(fromOrderDetail.getOrderOriginId());
                orderDetail.setUserId(order.getUserId());
                orderDetail.setItemId(itemDetail.getId());
                orderDetail.setItemName(itemDetail.getItemName());
                orderDetail.setItemNum(itemPromotion.getFreeNum()
                        * fromOrderDetail.getItemNum());
                orderDetail.setOrderDetailStatus(fromOrderDetail
                        .getOrderDetailStatus());
                orderDetail
                        .setOrderDetailType(fromOrderDetail.getOrderDetailType());
                orderDetail.setReason("");
                orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
                orderDetail.setItemPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
                orderDetail
                        .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
                orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
                long time = System.currentTimeMillis();
                orderDetail.setCreateTime(time);
                orderDetail.setUpdateTime(time);
                orderDetail.setFromOrderDetailId(fromOrderDetail.getId());
                orderDetail.setIsFree(ParamConst.FREE);
                orderDetail.setGroupId(fromOrderDetail.getGroupId());

                orderDetail.setModifierPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setRealPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setOrderSplitId(fromOrderDetail.getOrderSplitId());
                orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                orderDetail.setAppOrderDetailId(0);
                orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
            } else {
                orderDetail.setItemNum(itemPromotion.getFreeNum()
                        * fromOrderDetail.getItemNum());
            }
            OrderDetailSQL.updateOrderDetail(orderDetail);
        }
        return orderDetail;
    }

    public OrderDetail getItemFreeOrderDetailMin(Order order,
                                                 OrderDetail orderDetail, Boolean isFree) {


        synchronized (lock_free_order_detail) {

            BigDecimal price = BH.getBD(ParamConst.DOUBLE_ZERO);
            if (isFree) {
                price = BH.sub(BH.mul(BH.getBD(orderDetail.getItemPrice()), BH.getBD(orderDetail.getItemNum()), false), BH.getBD(orderDetail.getItemPrice()), false);

            } else {
                price = BH.mul(BH.getBD(orderDetail.getItemPrice()), BH.getBD(orderDetail.getItemNum()), false);

            }
            orderDetail.setRealPrice(price.toString());
//            orderDetail = OrderDetailSQL.getOrderDetail(order.getId(),
//                    fromOrderDetail);
//            if (orderDetail == null) {
//                orderDetail = new OrderDetail();
//                orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
//                orderDetail.setOrderId(order.getId());
//                orderDetail.setOrderOriginId(fromOrderDetail.getOrderOriginId());
//                orderDetail.setUserId(order.getUserId());
//                orderDetail.setItemId(itemDetail.getId());
//                orderDetail.setItemName(itemDetail.getItemName());
//                orderDetail.setItemNum(itemPromotion.getFreeNum()
//                        * fromOrderDetail.getItemNum());
//                orderDetail.setOrderDetailStatus(fromOrderDetail
//                        .getOrderDetailStatus());
//                orderDetail
//                        .setOrderDetailType(fromOrderDetail.getOrderDetailType());
//                orderDetail.setReason("");
//                orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
//                orderDetail.setItemPrice(ParamConst.DOUBLE_ZERO);
//                orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
//                orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
//                orderDetail
//                        .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
//                orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
//                long time = System.currentTimeMillis();
//                orderDetail.setCreateTime(time);
//                orderDetail.setUpdateTime(time);
//                orderDetail.setFromOrderDetailId(fromOrderDetail.getId());
//                orderDetail.setIsFree(ParamConst.FREE);
//                orderDetail.setGroupId(fromOrderDetail.getGroupId());
//
//                orderDetail.setModifierPrice(ParamConst.DOUBLE_ZERO);
//                orderDetail.setRealPrice(ParamConst.DOUBLE_ZERO);
//                orderDetail.setOrderSplitId(fromOrderDetail.getOrderSplitId());
//                orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
//                orderDetail.setAppOrderDetailId(0);
//                orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
//            } else {

//            }
            OrderDetailSQL.updateOrderDetail(orderDetail);
        }
        return orderDetail;
    }

    public OrderDetail getPromotionFreeOrderDetail(Order order,
                                                   ItemDetail itemDetail,
                                                   Promotion promotion) {

        OrderDetail orderDetail = null;
        //   synchronized (lock_free_order_detail) {
        orderDetail = OrderDetailSQL.getPromotionOrderDetail(order.getId(),
                order.getId());
        if (orderDetail == null) {
            orderDetail = new OrderDetail();
            orderDetail.setId(CommonSQL.getNextSeq(TableNames.OrderDetail));
            orderDetail.setOrderId(order.getId());
            //  orderDetail.setOrderOriginId(fromOrderDetail.getOrderOriginId());
            orderDetail.setUserId(order.getUserId());
            orderDetail.setItemId(itemDetail.getId());
            orderDetail.setItemName(itemDetail.getItemName());
            orderDetail.setItemNum(promotion.getFreeNum());
//                orderDetail.setOrderDetailStatus(fromOrderDetail
//                        .getOrderDetailStatus());
            orderDetail.setOrderDetailType(0);
            orderDetail.setReason("");
            orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
            orderDetail.setItemPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
            orderDetail
                    .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
            orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
            long time = System.currentTimeMillis();
            orderDetail.setCreateTime(time);
            orderDetail.setUpdateTime(time);
            orderDetail.setFromOrderDetailId(order.getId());
            orderDetail.setIsFree(ParamConst.FREE);
            // orderDetail.setGroupId(fromOrderDetail.getGroupId());

            orderDetail.setModifierPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setRealPrice(ParamConst.DOUBLE_ZERO);

            //  orderDetail.setOrderSplitId(fromOrderDetail.getOrderSplitId());
            orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
            orderDetail.setAppOrderDetailId(0);
            orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
        } else {
            orderDetail.setItemNum(promotion.getFreeNum());
        }
        OrderDetailSQL.updateOrderDetail(orderDetail);
        //  }
        return orderDetail;
    }


    // add:thread safe
    Object lock_free_order_detail_for_waiter = new Object();

    public OrderDetail getFreeOrderDetailForWaiter(Order order,
                                                   OrderDetail fromOrderDetail, ItemDetail itemDetail,
                                                   ItemHappyHour itemHappyHour) {

        OrderDetail orderDetail = null;
        synchronized (lock_free_order_detail_for_waiter) {
            orderDetail = OrderDetailSQL.getOrderDetail(order.getId(),
                    fromOrderDetail);
            if (orderDetail == null) {
                int orderDetailId = CommonSQL.getNextSeq(TableNames.OrderDetail);
                if (orderDetailId < 1000000) {
                    orderDetailId += 1000000;
                }
                orderDetail = new OrderDetail();
                orderDetail.setId(orderDetailId);
                orderDetail.setOrderId(order.getId());
                orderDetail.setOrderOriginId(ParamConst.ORDER_ORIGIN_WAITER);
                orderDetail.setUserId(order.getUserId());
                orderDetail.setItemName(itemDetail.getItemName());
                orderDetail.setItemId(itemDetail.getId());
                orderDetail.setItemNum(itemHappyHour.getFreeNum()
                        * fromOrderDetail.getItemNum());
                orderDetail
                        .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
                orderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_GENERAL);
                orderDetail.setReason("");
                orderDetail.setPrintStatus(ParamConst.PRINT_STATUS_UNDONE);
                orderDetail.setItemPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setTaxPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
                orderDetail
                        .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
                orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
                long time = System.currentTimeMillis();
                orderDetail.setCreateTime(time);
                orderDetail.setUpdateTime(time);
                orderDetail.setFromOrderDetailId(fromOrderDetail.getId());
                orderDetail.setIsFree(ParamConst.FREE);
                orderDetail.setGroupId(fromOrderDetail.getGroupId());

                orderDetail.setModifierPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setRealPrice(ParamConst.DOUBLE_ZERO);
                orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                orderDetail.setAppOrderDetailId(0);
                orderDetail.setMainCategoryId(itemDetail.getItemMainCategoryId().intValue());
            } else {
                orderDetail.setItemNum(itemHappyHour.getFreeNum()
                        * fromOrderDetail.getItemNum());
            }
            OrderDetailSQL.updateOrderDetail(orderDetail);
        }
        return orderDetail;
    }


    Object lock_order_modifier_check = new Object();

    //: only call from main POS. not need threadsafe
    public ModifierCheck getModifierCheck(Order order, OrderDetail orderDetail,
                                          Modifier modifier, ItemModifier itemModifier) {
        ModifierCheck modifierCheck = new ModifierCheck();
        synchronized (lock_order_modifier_check) {

            modifierCheck.setId(CommonSQL.getNextSeq(TableNames.ModifierCheck));
            modifierCheck.setOrderDetailId(orderDetail.getId());
            modifierCheck.setOrderId(order.getId());
            modifierCheck.setModifierCategoryId(itemModifier.getModifierCategoryId());
            modifierCheck.setItemName(orderDetail.getItemName());
            modifierCheck.setModifierCategoryName(modifier.getCategoryName());
            modifierCheck.setNum(modifier.getMinNumber());
            modifierCheck.setMinNum(modifier.getMinNumber());

        }
        return modifierCheck;
    }


    Object lock_order_modifier = new Object();

    //: only call from main POS. not need threadsafe
    public OrderModifier getOrderModifier(Order order, OrderDetail orderDetail,
                                          Modifier modifier, int printerId) {
        OrderModifier orderModifier = new OrderModifier();
        synchronized (lock_order_modifier) {
            orderModifier.setId(CommonSQL.getNextSeq(TableNames.OrderModifier));
            orderModifier.setOrderId(order.getId());
            orderModifier.setOrderDetailId(orderDetail.getId());
            orderModifier.setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
            orderModifier.setUserId(order.getUserId());
            orderModifier.setItemId(orderDetail.getItemId());
            orderModifier.setModifierId(modifier.getId());
            orderModifier.setModifierNum(modifier.getQty() * orderDetail.getItemNum().intValue());
            orderModifier.setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
            orderModifier.setModifierPrice(BH.mul(BH.getBD(modifier.getPrice()), BH.getBD(modifier.getQty() * orderDetail.getItemNum().intValue()), false).toString());
            long time = System.currentTimeMillis();
            orderModifier.setCreateTime(time);
            orderModifier.setUpdateTime(time);
            orderModifier.setPrinterId(printerId);
            orderModifier.setModifierItemPrice(modifier.getPrice());

        }
        return orderModifier;
    }


    public OrderModifier getOrderModifierFromTempAppOrderModifier(Order order,
                                                                  OrderDetail orderDetail, int printerId,
                                                                  AppOrderModifier appOrderModifier) {
        OrderModifier orderModifier = new OrderModifier();
        synchronized (lock_order_modifier) {
            orderModifier.setId(CommonSQL.getNextSeq(TableNames.OrderModifier));
            orderModifier.setOrderId(order.getId());
            orderModifier.setOrderDetailId(orderDetail.getId());
            orderModifier.setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
            orderModifier.setUserId(order.getUserId());
            orderModifier.setItemId(orderDetail.getItemId());
            orderModifier.setModifierId(appOrderModifier.getModifierId()
                    .intValue());
            orderModifier.setModifierNum(appOrderModifier.getModifierNum()
                    .intValue());
            orderModifier.setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
            orderModifier.setModifierPrice(appOrderModifier
                    .getModifierPrice());
            long time = System.currentTimeMillis();
            orderModifier.setCreateTime(time);
            orderModifier.setUpdateTime(time);
            orderModifier.setPrinterId(printerId);
            orderModifier.setModifierItemPrice(appOrderModifier
                    .getModifierPrice());
            OrderModifierSQL
                    .addOrderModifierForDiner(orderModifier);
        }
        return orderModifier;
    }

    // add:thread safe
    Object lock_get_order_bill = new Object();

    public OrderBill getOrderBill(Order order, RevenueCenter revenueCenter) {

        OrderBill orderBill = null;
        synchronized (lock_get_order_bill) {
            orderBill = OrderBillSQL.getOrderBillByOrder(order);
            if (orderBill == null) {
                orderBill = new OrderBill();
                orderBill.setId(CommonSQL.getNextSeq(TableNames.OrderBill));
                orderBill.setBillNo(RevenueCenterSQL.getBillNoFromRevenueCenter(revenueCenter.getId()));
                orderBill.setOrderId(order.getId());
                orderBill.setOrderSplitId(0);// TODO
                orderBill.setType(ParamConst.BILL_TYPE_UN_SPLIT);// TODO
                orderBill.setRestaurantId(CoreData.getInstance().getRestaurant()
                        .getId());
                orderBill.setRevenueId(revenueCenter.getId());
                orderBill.setUserId(order.getUserId());
                long time = System.currentTimeMillis();
                orderBill.setCreateTime(time);
                orderBill.setUpdateTime(time);
                OrderBillSQL.add(orderBill);
            }
        }
        return orderBill;
    }

    public OrderBill getOrderBillByOrderSplit(OrderSplit orderSplit, RevenueCenter revenueCenter) {

        OrderBill orderBill = null;
        synchronized (lock_get_order_bill) {
            orderBill = OrderBillSQL.getOrderBillByOrderSplit(orderSplit);
            if (orderBill == null) {
                orderBill = OrderBillSQL.getOrderBillByOnlyOrder(orderSplit.getOrderId().intValue());
                if (orderBill == null) {
                    orderBill = new OrderBill();
                    orderBill.setId(CommonSQL.getNextSeq(TableNames.OrderBill));
                    orderBill.setBillNo(RevenueCenterSQL.getBillNoFromRevenueCenter(revenueCenter.getId()));
                }
                orderBill.setOrderId(orderSplit.getOrderId());
                orderBill.setOrderSplitId(orderSplit.getId());
                orderBill.setType(ParamConst.BILL_TYPE_SPLIT);
                orderBill.setRestaurantId(CoreData.getInstance().getRestaurant()
                        .getId());
                orderBill.setRevenueId(revenueCenter.getId());
                orderBill.setUserId(orderSplit.getUserId());
                long time = System.currentTimeMillis();
                orderBill.setCreateTime(time);
                orderBill.setUpdateTime(time);
                OrderBillSQL.add(orderBill);
            }
        }
        return orderBill;
    }

    // add:thread safe
    Object lock_get_order_detail_tax = new Object();

    public OrderDetailTax getOrderDetailTax(Order order,
                                            OrderDetail orderDetail, Tax tax, int indexId) {
        OrderDetailTax orderDetailTax = null;
        synchronized (lock_get_order_detail_tax) {
            orderDetailTax = OrderDetailTaxSQL.getOrderDetailTax(
                    order, orderDetail, tax);
            if (orderDetailTax == null) {

                orderDetailTax = new OrderDetailTax();
                if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                    int orderDetailTaxId = CommonSQL
                            .getNextSeq(TableNames.OrderDetailTax);
                    if (orderDetailTaxId < 1000000) {
                        orderDetailTaxId += 1000000;
                    }
                    orderDetailTax.setId(orderDetailTaxId);
                } else {
                    orderDetailTax.setId(CommonSQL
                            .getNextSeq(TableNames.OrderDetailTax));
                }
                orderDetailTax.setOrderId(order.getId());
                orderDetailTax.setOrderDetailId(orderDetail.getId());
                orderDetailTax.setTaxId(tax.getId());
                orderDetailTax.setTaxName(tax.getTaxName());
                orderDetailTax.setTaxPercentage(tax.getTaxPercentage());
                orderDetailTax.setTaxType(tax.getTaxType());
                long time = System.currentTimeMillis();
                orderDetailTax.setCreateTime(time);
                orderDetailTax.setUpdateTime(time);
                orderDetailTax.setIndexId(indexId);
                orderDetailTax.setOrderSplitId(orderDetail.getOrderSplitId());
                orderDetailTax.setIsActive(ParamConst.ACTIVE_NOMAL);
                OrderDetailTaxSQL.updateOrderDetailTax(orderDetailTax);
            }
        }
        return orderDetailTax;
    }

    public OrderDetailTax getOrderDetailTaxByOnline(Order order,
                                                    OrderDetail orderDetail, AppOrderDetailTax appOrderDetailTax, int indexId) {
        OrderDetailTax orderDetailTax = null;
        synchronized (lock_get_order_detail_tax) {
            orderDetailTax = OrderDetailTaxSQL.getOrderDetailTaxId(
                    order.getId(), orderDetail.getId(), appOrderDetailTax.getTaxId());
            if (orderDetailTax == null) {
                orderDetailTax = new OrderDetailTax();
                orderDetailTax.setId(CommonSQL
                        .getNextSeq(TableNames.OrderDetailTax));
            }
            orderDetailTax.setOrderId(order.getId());
            orderDetailTax.setOrderDetailId(orderDetail.getId());
            orderDetailTax.setTaxId(appOrderDetailTax.getTaxId());
            orderDetailTax.setTaxName(appOrderDetailTax.getTaxName());
            orderDetailTax.setTaxPercentage(appOrderDetailTax.getTaxPercentage());
            orderDetailTax.setTaxType(appOrderDetailTax.getTaxType());
            long time = System.currentTimeMillis();
            orderDetailTax.setCreateTime(time);
            orderDetailTax.setUpdateTime(time);
            orderDetailTax.setIndexId(indexId);
            orderDetailTax.setOrderSplitId(orderDetail.getOrderSplitId());
            orderDetailTax.setIsActive(ParamConst.ACTIVE_NOMAL);
            orderDetailTax.setTaxPrice(appOrderDetailTax.getTaxPrice());
            OrderDetailTaxSQL.updateOrderDetailTax(orderDetailTax);
        }
        return orderDetailTax;
    }

    /**
     * 特殊的调用方式 update by Alex
     *
     * @param order, orderBill 给不拆单的使用
     * @return
     */
    // add:thread safe
    Object lock_get_payment = new Object();

    public Payment getPayment(Order order, OrderBill orderBill) {

        Payment payment = null;
        synchronized (lock_get_payment) {
            payment = PaymentSQL.getPaymentByOrderId(order.getId());

            long time = System.currentTimeMillis();
            if (payment == null) {
                payment = new Payment();
                payment.setId(CommonSQL.getNextSeq(TableNames.Payment));
                payment.setCreateTime(time);
                payment.setOrderSplitId(0); // 不拆单 默认的是0
                payment.setType(ParamConst.BILL_TYPE_UN_SPLIT); // 是否拆单
                payment.setOrderId(order.getId());
                payment.setBusinessDate(order.getBusinessDate());
                payment.setRestaurantId(CoreData.getInstance().getRestaurant().getId());
                payment.setRevenueId(order.getRevenueId());
                payment.setUserId(order.getUserId());
            }
            payment.setBillNo(orderBill.getBillNo());
            payment.setPaymentAmount(order.getTotal()); // 不拆单 填入 Order信息
            payment.setTaxAmount(order.getTaxAmount()); // 不拆单 填入 Order信息
            payment.setDiscountAmount(order.getDiscountAmount()); // 不拆单 填入 Order信息
            payment.setUpdateTime(time);
            PaymentSQL.addPayment(payment);
        }
        return payment;
    }


    /**
     * 特殊的调用方式 add by Alex
     *
     * @param businessDate, orderSplit, orderBill 给拆单的使用
     * @return
     */
    public Payment getPaymentByOrderSplit(long businessDate, OrderSplit orderSplit, OrderBill orderBill) {

        Payment payment = null;
        synchronized (lock_get_payment) {
            payment = PaymentSQL.getPaymentByOrderSplitId(orderSplit.getId());

            long time = System.currentTimeMillis();
            if (payment == null) {
                payment = PaymentSQL.getPaymentByOrderId(orderSplit.getOrderId().intValue());
                if (payment == null) {
                    payment = new Payment();
                    payment.setId(CommonSQL.getNextSeq(TableNames.Payment));
                    payment.setCreateTime(time);
                }
            }
            payment.setBillNo(orderBill.getBillNo());
            payment.setOrderId(orderSplit.getOrderId());
            payment.setOrderSplitId(orderSplit.getId());
            payment.setBusinessDate(businessDate);
            payment.setType(ParamConst.BILL_TYPE_SPLIT);
            payment.setRestaurantId(CoreData.getInstance().getRestaurant().getId());
            payment.setRevenueId(orderSplit.getRevenueId());
            payment.setUserId(orderSplit.getUserId());
            payment.setPaymentAmount(orderSplit.getTotal());
            payment.setTaxAmount(orderSplit.getTaxAmount());
            payment.setDiscountAmount(orderSplit.getDiscountAmount());
            payment.setUpdateTime(time);
            PaymentSQL.addPayment(payment);
        }
        return payment;
    }

    // add:thread safe
    Object lock_get_payment_settlement = new Object();

    public PaymentSettlement getPaymentSettlement(Payment payment,
                                                  int paymentTypeId, String paidAmount) {
        PaymentSettlement paymentSettlement = null;

        synchronized (lock_get_payment_settlement) {
            paymentSettlement = PaymentSettlementSQL
                    .getPaymentSettlementByPaymentIdAndTypeId(payment,
                            paymentTypeId);
            long time = System.currentTimeMillis();
            if (paymentSettlement == null) {
                paymentSettlement = new PaymentSettlement();
                paymentSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.PaymentSettlement));
                paymentSettlement.setBillNo(payment.getBillNo());
                paymentSettlement.setPaymentId(payment.getId());
                paymentSettlement.setPaymentTypeId(paymentTypeId);
                paymentSettlement.setPaidAmount(paidAmount);
                paymentSettlement.setTotalAmount(payment.getPaymentAmount());
                paymentSettlement.setRestaurantId(payment.getRestaurantId());
                paymentSettlement.setRevenueId(payment.getRevenueId());
                paymentSettlement.setUserId(payment.getUserId());
                paymentSettlement.setCreateTime(time);
                paymentSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
            } else {
                paymentSettlement.setPaymentTypeId(paymentTypeId);
                if (paymentTypeId == ParamConst.SETTLEMENT_TYPE_CASH) {
                    BigDecimal amount = BH.add(
                            BH.getBD(paymentSettlement.getPaidAmount()),
                            BH.getBD(paidAmount), true);
                    paymentSettlement.setPaidAmount(amount.toString());
                } else {
                    paymentSettlement.setPaidAmount(paidAmount);
                }
            }
            if (paymentTypeId > 10000) {


                paymentSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.PaymentSettlement));
                paymentSettlement.setBillNo(payment.getBillNo());
                paymentSettlement.setPaymentId(payment.getId());
                paymentSettlement.setPaymentTypeId(paymentTypeId);
                paymentSettlement.setPaidAmount(paidAmount);
                paymentSettlement.setTotalAmount(payment.getPaymentAmount());
                paymentSettlement.setRestaurantId(payment.getRestaurantId());
                paymentSettlement.setRevenueId(payment.getRevenueId());
                paymentSettlement.setUserId(payment.getUserId());
                paymentSettlement.setCreateTime(time);
                paymentSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
            }
            paymentSettlement.setUpdateTime(time);
            PaymentSettlementSQL.addPaymentSettlement(paymentSettlement);
        }
        return paymentSettlement;
    }

    public PaymentSettlement getPaymentSettlementForCard(Payment payment,
                                                         int paymentTypeId, String paidAmount) {
        PaymentSettlement paymentSettlement = null;

        synchronized (lock_get_payment_settlement) {
//			paymentSettlement =PaymentSettlementSQL
//					.getPaymentSettlementByPaymentIdAndTypeId(payment,
//							paymentTypeId);
            long time = System.currentTimeMillis();
            paymentSettlement = new PaymentSettlement();
            paymentSettlement.setId(CommonSQL
                    .getNextSeq(TableNames.PaymentSettlement));
            paymentSettlement.setBillNo(payment.getBillNo());
            paymentSettlement.setPaymentId(payment.getId());
            paymentSettlement.setPaymentTypeId(paymentTypeId);
            paymentSettlement.setPaidAmount(paidAmount);
            paymentSettlement.setTotalAmount(payment.getPaymentAmount());
            paymentSettlement.setRestaurantId(payment.getRestaurantId());
            paymentSettlement.setRevenueId(payment.getRevenueId());
            paymentSettlement.setUserId(payment.getUserId());
            paymentSettlement.setCreateTime(time);
            paymentSettlement.setUpdateTime(time);
            paymentSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
            PaymentSettlementSQL.addPaymentSettlement(paymentSettlement);
        }
        return paymentSettlement;
    }

    // add:thread safe
    Object lock_get_getBohHoldSettlementByPaymentSettlement = new Object();

    public BohHoldSettlement getBohHoldSettlementByPaymentSettlement(
            PaymentSettlement paymentSettlement, int orderId,
            BohHoldSettlement mBohHoldSettlement) {

        BohHoldSettlement bohHoldSettlement = null;
        synchronized (lock_get_getBohHoldSettlementByPaymentSettlement) {
            bohHoldSettlement = BohHoldSettlementSQL
                    .getBohHoldSettlementByPament(paymentSettlement.getPaymentId(),
                            paymentSettlement.getId());
            if (bohHoldSettlement == null) {
                bohHoldSettlement = new BohHoldSettlement();
                bohHoldSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.BohHoldSettlement));
                bohHoldSettlement.setRestaurantId(paymentSettlement
                        .getRestaurantId());
                bohHoldSettlement.setRevenueId(paymentSettlement.getRevenueId());
                bohHoldSettlement.setPaymentId(paymentSettlement.getPaymentId());
                bohHoldSettlement.setPaymentSettId(paymentSettlement.getId());
                bohHoldSettlement.setOrderId(orderId);
                bohHoldSettlement.setBillNo(paymentSettlement.getBillNo());
                bohHoldSettlement.setNameOfPerson(mBohHoldSettlement
                        .getNameOfPerson());
                bohHoldSettlement.setPhone(mBohHoldSettlement.getPhone());
                bohHoldSettlement.setRemarks(mBohHoldSettlement.getRemarks());
                bohHoldSettlement.setAuthorizedUserId(mBohHoldSettlement
                        .getAuthorizedUserId());
                bohHoldSettlement.setAmount(mBohHoldSettlement.getAmount());
                bohHoldSettlement.setStatus(ParamConst.BOH_HOLD_STATUS_UNPLAY);
                bohHoldSettlement.setPaidDate(null);
                long time = System.currentTimeMillis();
                bohHoldSettlement.setDaysDue(time);
                bohHoldSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                BohHoldSettlementSQL.addBohHoldSettlement(bohHoldSettlement);
            }
        }
        return bohHoldSettlement;
    }

    // add:thread safe
    Object lock_get_getNonChargableSettlementByPaymentSettlement = new Object();

    public NonChargableSettlement getNonChargableSettlementByPaymentSettlement(
            Payment payment, PaymentSettlement paymentSettlement,
            NonChargableSettlement mNonChargableSettlement) {

        NonChargableSettlement nonChargableSettlement = null;
        synchronized (lock_get_getNonChargableSettlementByPaymentSettlement) {
            nonChargableSettlement = NonChargableSettlementSQL
                    .getNonChargableSettlementByPaymentId(payment.getId(), paymentSettlement
                            .getId());
            if (nonChargableSettlement == null) {
                nonChargableSettlement = new NonChargableSettlement();
                nonChargableSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.NonChargableSettlement));
                nonChargableSettlement.setOrderId(payment.getOrderId());
                nonChargableSettlement.setBillNo(paymentSettlement.getBillNo());
                nonChargableSettlement.setPaymentId(paymentSettlement
                        .getPaymentId());
                nonChargableSettlement.setPaymentSettId(paymentSettlement.getId());
                nonChargableSettlement.setNameOfPerson(mNonChargableSettlement
                        .getNameOfPerson());
                nonChargableSettlement.setRemarks(mNonChargableSettlement
                        .getRemarks());
                nonChargableSettlement.setAuthorizedUserId(mNonChargableSettlement
                        .getAuthorizedUserId());
                nonChargableSettlement.setAmount(mNonChargableSettlement
                        .getAmount());
                nonChargableSettlement.setRestaurantId(mNonChargableSettlement
                        .getRestaurantId());
                nonChargableSettlement.setRevenueId(mNonChargableSettlement
                        .getRevenueId());
                nonChargableSettlement.setUserId(mNonChargableSettlement
                        .getUserId());
                long time = System.currentTimeMillis();
                nonChargableSettlement.setCreateTime(time);
                nonChargableSettlement.setUpdateTime(time);
                nonChargableSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                NonChargableSettlementSQL.addNonChargableSettlement(nonChargableSettlement);
            }
        }
        return nonChargableSettlement;
    }

    // add:thread safe
    Object lock_getVoidSettlementByPayment = new Object();

    public VoidSettlement getVoidSettlementByPayment(Payment payment,
                                                     PaymentSettlement paymentSettlement, VoidSettlement mVoidSettlement) {
        VoidSettlement voidSettlement = null;

        synchronized (lock_getVoidSettlementByPayment) {
            voidSettlement = VoidSettlementSQL
                    .getVoidSettlementByPament(payment.getId(),
                            paymentSettlement.getId());
            if (voidSettlement == null) {
                voidSettlement = new VoidSettlement();
                voidSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.VoidSettlement));
                voidSettlement.setOrderId(payment.getOrderId());
                voidSettlement.setBillNo(payment.getBillNo());
                voidSettlement.setPaymentId(payment.getId());
                voidSettlement.setPaymentSettId(paymentSettlement.getId());
                voidSettlement.setReason(mVoidSettlement.getReason());
                voidSettlement.setAuthorizedUserId(mVoidSettlement
                        .getAuthorizedUserId());
                voidSettlement.setAmount(mVoidSettlement.getAmount());
                voidSettlement.setRestaurantId(payment.getRestaurantId());
                voidSettlement.setRevenueId(payment.getRevenueId());
                voidSettlement.setUserId(payment.getUserId());
                long time = System.currentTimeMillis();
                voidSettlement.setCreateTime(time);
                voidSettlement.setUpdateTime(time);
                voidSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                voidSettlement.setType(mVoidSettlement.getType());
                VoidSettlementSQL.addVoidSettlement(voidSettlement);
            }
        }
        return voidSettlement;
    }

    // add:thread safe
    Object lock_getNetsSettlementByPayment = new Object();

    public NetsSettlement getNetsSettlementByPayment(Payment payment,
                                                     PaymentSettlement paymentSettlement, int referenceNo,
                                                     String cashAmount) {
        NetsSettlement netsSettlement = null;

        synchronized (lock_getNetsSettlementByPayment) {
            netsSettlement = NetsSettlementSQL
                    .getNetsSettlementByPament(payment.getId(),
                            paymentSettlement.getId());
            long time = System.currentTimeMillis();
            if (netsSettlement == null) {
                netsSettlement = new NetsSettlement();
                netsSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.NetsSettlement));
                netsSettlement.setPaymentId(payment.getId());
                netsSettlement.setPaymentSettId(paymentSettlement.getId());
                netsSettlement.setBillNo(payment.getBillNo());
                netsSettlement.setReferenceNo(referenceNo);
                netsSettlement.setCashAmount(cashAmount);
                netsSettlement.setCreateTime(time);
                netsSettlement.setUpdateTime(time);
                netsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
            } else {
                netsSettlement.setReferenceNo(referenceNo);
                netsSettlement.setCashAmount(cashAmount);
                netsSettlement.setUpdateTime(time);
            }
            NetsSettlementSQL.addNetsSettlement(netsSettlement);
        }
        return netsSettlement;
    }

    public PrinterTitle getPrinterTitle(RevenueCenter revenue, Order order,
                                        String userName, String tableName, int copy, int trainType) {
        PrinterTitle printerTitle = new PrinterTitle();
        Restaurant restaurant = RestaurantSQL.getRestaurant();
        printerTitle.setRestaurantName(restaurant.getRestaurantPrint());
        printerTitle.setRevName(revenue.getRevName());
        printerTitle.setAddressDetail(restaurant.getAddressPrint());
        printerTitle.setTel(restaurant.getTelNo());
        printerTitle.setEmail(restaurant.getEmail());
        printerTitle.setWebAddress(restaurant.getWebsite());
        printerTitle.setOp(userName);
        printerTitle.setPos(revenue.getId().intValue() + "");
        printerTitle.setDate(TimeUtil.getPrintDate(order.getCreateTime()));
        printerTitle.setBill_NO(ParamHelper.getPrintOrderBillNo(OrderBillSQL.getOrderBillByOrder(order).getBillNo()));
        printerTitle.setTime(TimeUtil.getPrintTime(order.getCreateTime()));
        printerTitle.setTableName(tableName);
        printerTitle.setLogo(SettingDataSQL.getSettingDataByUrl(
                restaurant.getLogoUrl()).getLogoString());
        printerTitle.setOptions(restaurant.getOptions());
        printerTitle.setFooterOptions(restaurant.getFooterOptions());
        printerTitle.setIsTakeAway(order.getIsTakeAway());
        printerTitle.setBizDate(order.getBusinessDate().toString());
        printerTitle.setIsKiosk(revenue.getIsKiosk());
        printerTitle.setCopy(copy);
        if (revenue.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
            printerTitle.setOrderNo(IntegerUtils.formatLocale(revenue.getIndexId(), order.getOrderNo().toString()));
        } else {
            printerTitle.setOrderNo(order.getOrderNo().toString());
        }
        printerTitle.setTrainType(trainType);

        return printerTitle;
    }

    public PrinterTitle getPrinterTitleByOrderSplit(RevenueCenter revenue, Order order, OrderSplit orderSplit,
                                                    String userName, String tableName, OrderBill orderBill, String businessDate, int copy) {
        PrinterTitle printerTitle = new PrinterTitle();
        Restaurant restaurant = RestaurantSQL.getRestaurant();
        printerTitle.setRestaurantName(restaurant.getRestaurantPrint());

        printerTitle.setRevName(revenue.getRevName());
        printerTitle.setAddressDetail(restaurant.getAddressPrint());
        printerTitle.setTel(restaurant.getTelNo());
        printerTitle.setEmail(restaurant.getEmail());
        printerTitle.setWebAddress(restaurant.getWebsite());
        printerTitle.setOp(userName);
        printerTitle.setPos(revenue.getId() + "");
        printerTitle.setDate(TimeUtil.getPrintDate(orderSplit.getCreateTime()));
        printerTitle.setBill_NO(ParamHelper.getPrintOrderBillNo(orderBill.getBillNo()));
        printerTitle.setTime(TimeUtil.getPrintTime(orderSplit.getCreateTime()));
        printerTitle.setTableName(tableName);
        printerTitle.setLogo(SettingDataSQL.getSettingDataByUrl(
                restaurant.getLogoUrl()).getLogoString());
        printerTitle.setOptions(restaurant.getOptions());
        printerTitle.setFooterOptions(restaurant.getFooterOptions());
        printerTitle.setBizDate(businessDate);
        printerTitle.setGroupNum(orderSplit.getGroupId() + "");
        printerTitle.setIsKiosk(revenue.getIsKiosk());
        printerTitle.setCopy(copy);
        String trainString = "";
//        if(trainType==1){
//            trainString=".Training";
//        }
//		printerTitle.setOrderNo(orderSplit.getOrderId().toString());
        if (revenue.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
            printerTitle.setOrderNo(IntegerUtils.formatLocale(revenue.getIndexId(), order.getOrderNo().toString()));
        } else {
            printerTitle.setOrderNo(order.getOrderNo().toString());
        }
        return printerTitle;
    }

    public PrinterTitle getPrinterTitleForReport(int revenueId, String billNo,
                                                 String userName, String tableName, String businessDate, int trainType) {
        PrinterTitle printerTitle = new PrinterTitle();
        Restaurant restaurant = new Restaurant();
        restaurant = RestaurantSQL.getRestaurant();
        printerTitle.setRestaurantName(restaurant.getRestaurantPrint());
        printerTitle.setAddressDetail(restaurant.getAddressPrint());

        printerTitle.setAddress(restaurant.getCity() + " "
                + restaurant.getState() + " " + restaurant.getCountry() + " "
                + restaurant.getPostalCode());
        printerTitle.setTel(restaurant.getTelNo());
        printerTitle.setEmail(restaurant.getEmail());
        printerTitle.setWebAddress(restaurant.getWebsite());
        printerTitle.setOp(userName);
        long time = System.currentTimeMillis();
        printerTitle.setPos(revenueId + "");
        printerTitle.setDate(TimeUtil.getPrintDate(time));
        printerTitle.setBill_NO(billNo);
        printerTitle.setTime(TimeUtil.getPrintTime(time));
        printerTitle.setTableName(tableName);
        printerTitle.setLogo(SettingDataSQL.getSettingDataByUrl(
                restaurant.getLogoUrl()).getLogoString());
        printerTitle.setBizDate(businessDate);
        printerTitle.setOrderNo(billNo);
        printerTitle.setTrainType(trainType);

        return printerTitle;
    }

    //get item list to print
    public ArrayList<PrintOrderItem> getItemList(
            List<OrderDetail> orderDetails) {
        ArrayList<PrintOrderItem> list = new ArrayList<PrintOrderItem>();
        for (OrderDetail orderDetail : orderDetails) {
//            ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
//                    orderDetail.getItemId());
            // Double amount = Double.parseDouble(orderDetail.getItemPrice())
            // * orderDetail.getItemNum();
            String price = orderDetail.getItemPrice();
            BigDecimal amountBH = BH.getBD(ParamConst.DOUBLE_ZERO);
            if (orderDetail.getOrderDetailType() == ParamConst.ORDERDETAIL_TYPE_FREE)
                price = ParamConst.DOUBLE_ZERO;
//			BigDecimal amountBH = BH.mul(BH.getBD(price),
//					BH.getBD(orderDetail.getItemNum()), true);
            else
                amountBH = BH.getBD(orderDetail.getRealPrice());
            list.add(new PrintOrderItem(orderDetail.getId(), orderDetail.getIsTakeAway(), orderDetail.getItemId(),
                    orderDetail.getItemName(), BH.getBD(orderDetail.getItemPrice())
                    .toString(), orderDetail.getItemNum() + "", amountBH
                    .toString(), orderDetail.getWeight()));
        }
        return list;
    }


    public PrinterTitle getPrinterTitleForQRCode(RevenueCenter revenue,
                                                 String userName, String tableName) {
        PrinterTitle printerTitle = new PrinterTitle();
        Restaurant restaurant = RestaurantSQL.getRestaurant();
        printerTitle.setRestaurantName(restaurant.getRestaurantPrint());
        printerTitle.setAddressDetail(restaurant.getAddressPrint());
        printerTitle.setTel(restaurant.getTelNo());
        printerTitle.setEmail(restaurant.getEmail());
        printerTitle.setWebAddress(restaurant.getWebsite());
        printerTitle.setOp(userName);
        printerTitle.setPos(revenue.getId().intValue() + "");
        printerTitle.setTableName(tableName);
        printerTitle.setLogo(SettingDataSQL.getSettingDataByUrl(
                restaurant.getLogoUrl()).getLogoString());
        printerTitle.setOptions(restaurant.getOptions());
        printerTitle.setFooterOptions(restaurant.getFooterOptions());
        return printerTitle;
    }

    /* get order modifiers for print */
    public ArrayList<PrintOrderModifier> getItemModifierList(Order order, List<OrderDetail> orderDetails) {

        ArrayList<PrintOrderModifier> list = new ArrayList<PrintOrderModifier>();
        ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
                .getAllOrderModifierByOrderAndNormal(order);
        for (OrderModifier orm : orderModifiers) {
            Modifier mitem = CoreData.getInstance().getModifier(
                    orm.getModifierId());
            // Double amount = Double.parseDouble(mitem.getPrice())
            // * orm.getModifierNum();
            BigDecimal amountBH = BH.mul(BH.getBD(mitem.getPrice()),
                    BH.getBD(orm.getModifierNum()), true);
            list.add(new PrintOrderModifier(orm.getOrderDetailId(), mitem
                    .getModifierName(), mitem.getPrice(), mitem.getQty()
                    , amountBH.toString()));

            // ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
            // orderDetail.getItemId());
            // Double amount = Double.parseDouble(orderDetail.getRealPrice())
            // * orderDetail.getItemNum();
            // list.add(new
            // PrintOrderItem(orderDetail.getId(),itemDetail.getItemName(),
            // orderDetail
            // .getRealPrice(), orderDetail.getItemNum() + "", Double
            // .toString(amount)));
        }
        for (OrderDetail orderDetail : orderDetails) {
            if (!TextUtils.isEmpty(orderDetail.getSpecialInstractions())) {
                list.add(new PrintOrderModifier(orderDetail.getId(), orderDetail.getSpecialInstractions(), "0.00", 1, "0.00", 1));
            }
        }
        return list;
    }


    /* get order modifiers for print */
    public ArrayList<PrintOrderModifier> getItemModifierListByOrderDetail(List<OrderDetail> orderDetails) {

        ArrayList<PrintOrderModifier> list = new ArrayList<PrintOrderModifier>();

        for (OrderDetail orderDetail : orderDetails) {
            ArrayList<OrderModifier> orderModifiers = OrderModifierSQL.getOrderModifiers(orderDetail);
            for (OrderModifier orm : orderModifiers) {
                Modifier mitem = CoreData.getInstance().getModifier(
                        orm.getModifierId());
                // Double amount = Double.parseDouble(mitem.getPrice())
                // * orm.getModifierNum();
                BigDecimal amountBH = BH.mul(BH.getBD(mitem.getPrice()),
                        BH.getBD(orm.getModifierNum()), true);
                list.add(new PrintOrderModifier(orm.getOrderDetailId(), mitem
                        .getModifierName(), mitem.getPrice(), orm.getModifierNum().intValue()
                        , amountBH.toString()));

                // ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
                // orderDetail.getItemId());
                // Double amount = Double.parseDouble(orderDetail.getRealPrice())
                // * orderDetail.getItemNum();
                // list.add(new
                // PrintOrderItem(orderDetail.getId(),itemDetail.getItemName(),
                // orderDetail
                // .getRealPrice(), orderDetail.getItemNum() + "", Double
                // .toString(amount)));
            }
        }
        return list;
    }

    Object lock_getKotSummary = new Object();

    public KotSummary getKotSummary(String tableName, Order order,
                                    RevenueCenter revenueCenter, long businessDate) {

        KotSummary kotSummary = null;
        synchronized (lock_getKotSummary) {
            kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());
            long time = System.currentTimeMillis();
            if (kotSummary == null) {
                kotSummary = new KotSummary();
                Integer id = CommonSQL.getKotNextSeq(TableNames.KotSummary);
                kotSummary.setId(id);
                kotSummary.setUniqueId(CommonSQL.getUniqueId());
                kotSummary.setOrderId(order.getId());
                kotSummary.setOrderNo(order.getOrderNo());//流水号
                kotSummary.setRevenueCenterId(revenueCenter.getId());
                kotSummary.setRevenueCenterName(revenueCenter.getRevName());
                kotSummary.setCreateTime(time);
                kotSummary.setUpdateTime(time);
                kotSummary.setBusinessDate(businessDate);
                kotSummary.setRevenueCenterIndex(revenueCenter.getIndexId());
                kotSummary.setOrderRemark(order.getOrderRemark());
                kotSummary.setNumTag(order.getNumTag());

            }
            if (revenueCenter.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
                kotSummary.setTableName(order.getTableName());
            } else {
                kotSummary.setTableName(tableName);
            }
            kotSummary.setIsTakeAway(order.getIsTakeAway());
            KotSummarySQL.update(kotSummary);
        }
        return kotSummary;
    }


    public KotSummary getKotSummaryApporder(String tableName, Order order, AppOrder appOrder,
                                            RevenueCenter revenueCenter, long businessDate) {

        KotSummary kotSummary = null;
        synchronized (lock_getKotSummary) {
            kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());
            long time = System.currentTimeMillis();
            if (kotSummary == null) {
                kotSummary = new KotSummary();

                int next = CommonSQL.getKotNextSeq(TableNames.KotSummary);
//                int next = CommonSQL.getNextSeq(TableNames.KotSummary);
//                int nextWithRevId = Integer.parseInt(revenueCenter.getId() + "0");
//                if (next < nextWithRevId) {
//                    next = Integer.parseInt(order.getRevenueId() + "" + next);
//                } else {
//                    String n = "" + CommonSQL.getCurrentSeq(TableNames.KotSummary);
//                    String revId = "" + order.getRevenueId();
//                    int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                    next = Integer.parseInt(revId + "" + nxt);
//                }
                kotSummary.setId(next);
                kotSummary.setUniqueId(CommonSQL.getUniqueId());
                kotSummary.setOrderId(order.getId());
                kotSummary.setOrderNo(order.getOrderNo());//流水号

                kotSummary.setRevenueCenterId(revenueCenter.getId());
                kotSummary.setRevenueCenterName(revenueCenter.getRevName());
                kotSummary.setCreateTime(time);
                kotSummary.setUpdateTime(time);
                kotSummary.setBusinessDate(businessDate);
                kotSummary.setRevenueCenterIndex(revenueCenter.getIndexId());
                kotSummary.setOrderRemark(order.getOrderRemark());
                kotSummary.setNumTag(order.getNumTag());
                kotSummary.setEatType(appOrder.getEatType());
                kotSummary.setAppOrderId(appOrder.getId());
                if (appOrder.getEatType() == ParamConst.APP_ORDER_DELIVERY) {
                    kotSummary.setAddress(appOrder.getAddress());
                    kotSummary.setContact(appOrder.getContact());
                    kotSummary.setMobile(appOrder.getMobile());
                    kotSummary.setDeliveryTime(appOrder.getDeliveryTime());
                    kotSummary.setOrderRemark(appOrder.getOrderRemark());
                }
            }
            if (revenueCenter.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
                kotSummary.setTableName(order.getTableName());
            } else {
                kotSummary.setTableName(tableName);
            }
            kotSummary.setIsTakeAway(order.getIsTakeAway());
            KotSummarySQL.update(kotSummary);
        }
        return kotSummary;
    }


    //	Object lock_getKotSummary = new Object();
    public KotSummary getKotSummaryForPlace(String tableName, Order order,
                                            RevenueCenter revenueCenter, long businessDate) {

        KotSummary kotSummary = null;
        synchronized (lock_getKotSummary) {
            kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());
            long time = System.currentTimeMillis();
            if (kotSummary == null) {
                kotSummary = new KotSummary();
                int next = CommonSQL.getKotNextSeq(TableNames.KotSummary);
//                int next = CommonSQL.getNextSeq(TableNames.KotSummary);
//                int nextWithRevId = Integer.parseInt(revenueCenter.getId() + "0");
//                if (next < nextWithRevId) {
//                    next = Integer.parseInt(order.getRevenueId() + "" + next);
//                } else {
//                    String n = "" + CommonSQL.getCurrentSeq(TableNames.KotSummary);
//                    String revId = "" + order.getRevenueId();
//                    int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                    next = Integer.parseInt(revId + "" + nxt);
//
//                }
                kotSummary.setId(next);
                kotSummary.setUniqueId(CommonSQL.getUniqueId());
                kotSummary.setOrderId(order.getId());
                kotSummary.setOrderNo(order.getOrderNo());//流水号

                kotSummary.setRevenueCenterId(revenueCenter.getId());
                kotSummary.setRevenueCenterName(revenueCenter.getRevName());
                if (revenueCenter.getIsKiosk() == ParamConst.REVENUECENTER_IS_KIOSK) {
                    kotSummary.setTableName(order.getTableName());
                } else {
                    kotSummary.setTableName(tableName);
                }
                kotSummary.setCreateTime(time);
                kotSummary.setUpdateTime(time);
                kotSummary.setBusinessDate(businessDate);
                kotSummary.setIsTakeAway(order.getIsTakeAway());
                kotSummary.setRevenueCenterIndex(revenueCenter.getIndexId());
                kotSummary.setOrderRemark(order.getOrderRemark());
                kotSummary.setNumTag(order.getNumTag());
                KotSummarySQL.update(kotSummary);
            }
        }
        return kotSummary;
    }

    // add:thread safe
    Object lock_getKotItemDetail = new Object();

    public KotItemDetail getKotItemDetail(Order order, OrderDetail orderDetail,
                                          ItemDetail itemDetail, KotSummary kotSummary,
                                          SessionStatus sessionStatus, int categoryId) {

        KotItemDetail kotItemDetail = null;
        synchronized (lock_getKotItemDetail) {
            kotItemDetail = KotItemDetailSQL
                    .getMainKotItemDetailByOrderDetailId(kotSummary.getId(), orderDetail.getId());
            if (kotItemDetail == null) {
                long time = System.currentTimeMillis();
                kotItemDetail = new KotItemDetail();
                int next = CommonSQL.getKotNextSeq(TableNames.KotItemDetail);
//                int next = CommonSQL.getNextSeq(TableNames.KotItemDetail);
//                int nextWithRevId = Integer.parseInt(order.getRevenueId() + "0");
//                if (next < nextWithRevId) {
//                    next = Integer.parseInt(order.getRevenueId() + "" + next);
//                } else {
//                    String n = "" + CommonSQL.getCurrentSeq(TableNames.KotItemDetail);
//                    String revId = "" + order.getRevenueId();
//                    int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                    next = Integer.parseInt(revId + "" + nxt);
//                }

                kotItemDetail.setId(next);
                kotItemDetail.setUniqueId(CommonSQL.getUniqueId());
                kotItemDetail.setRestaurantId(order.getRestId());
                kotItemDetail.setRevenueId(order.getRevenueId());
                kotItemDetail.setOrderId(orderDetail.getOrderId());
                kotItemDetail.setOrderDetailId(orderDetail.getId());
                kotItemDetail.setPrinterGroupId(itemDetail.getPrinterId());
                kotItemDetail.setKotSummaryId(kotSummary.getId());
                kotItemDetail.setItemId(itemDetail.getId());
                kotItemDetail.setItemName(itemDetail.getItemName());
                kotItemDetail.setItemNum(orderDetail.getItemNum());
                kotItemDetail.setFinishQty(0); // 新创建的都0
                kotItemDetail.setSessionStatus(sessionStatus.getSession_status());
                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNSEND);
                kotItemDetail.setSpecialInstractions(orderDetail
                        .getSpecialInstractions());
                kotItemDetail.setVersion(0); // 没用
                kotItemDetail.setCreateTime(time);
                kotItemDetail.setUpdateTime(time);
                kotItemDetail.setUnFinishQty(orderDetail.getItemNum()); // 新创建的都是跟ItemNum一样
                kotItemDetail.setCategoryId(categoryId);
                kotItemDetail.setIsTakeAway(orderDetail.getIsTakeAway());
                kotItemDetail.setKotSummaryUniqueId(kotSummary.getUniqueId());
                KotItemDetailSQL.update(kotItemDetail);
            } else {
                kotItemDetail.setUnFinishQty(orderDetail.getItemNum());
                KotItemDetailSQL.update(kotItemDetail);
            }
        }
        return kotItemDetail;
    }

    public KotItemDetail cpKotItemDetail(KotItemDetail cpKotItemDetail, OrderDetail orderDetail) {
        KotItemDetail kotItemDetail = new KotItemDetail();
        synchronized (lock_getKotItemDetail) {
            int next = CommonSQL.getKotNextSeq(TableNames.KotItemDetail);
//            int next = CommonSQL.getNextSeq(TableNames.KotItemDetail);
//            int nextWithRevId = Integer.parseInt(cpKotItemDetail.getRevenueId() + "0");
//            if (next < nextWithRevId) {
//                next = Integer.parseInt(cpKotItemDetail.getRevenueId() + "" + next);
//            } else {
//                String n = "" + CommonSQL.getCurrentSeq(TableNames.KotItemDetail);
//                String revId = "" + cpKotItemDetail.getRevenueId();
//                int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                next = Integer.parseInt(revId + "" + nxt);
//
//            }
            kotItemDetail.setId(next);
            kotItemDetail.setUniqueId(CommonSQL.getUniqueId());
            kotItemDetail.setRestaurantId(cpKotItemDetail.getRestaurantId());
            kotItemDetail.setRevenueId(cpKotItemDetail.getRevenueId());
            kotItemDetail.setOrderId(orderDetail.getOrderId());
            kotItemDetail.setOrderDetailId(orderDetail.getId());
            kotItemDetail.setPrinterGroupId(cpKotItemDetail.getPrinterGroupId());
            kotItemDetail.setKotSummaryId(cpKotItemDetail.getKotSummaryId());
            kotItemDetail.setItemId(cpKotItemDetail.getId());
            kotItemDetail.setItemName(cpKotItemDetail.getItemName());
            kotItemDetail.setItemNum(orderDetail.getItemNum());
            kotItemDetail.setFinishQty(0); // 新创建的都0
            kotItemDetail.setSessionStatus(cpKotItemDetail.getSessionStatus());
            kotItemDetail.setKotStatus(cpKotItemDetail.getKotStatus());
            kotItemDetail.setSpecialInstractions(cpKotItemDetail.getSpecialInstractions());
            kotItemDetail.setVersion(cpKotItemDetail.getVersion()); // 没用
            kotItemDetail.setCreateTime(cpKotItemDetail.getCreateTime());
            kotItemDetail.setUpdateTime(cpKotItemDetail.getUpdateTime());
            kotItemDetail.setUnFinishQty(orderDetail.getItemNum()); // 新创建的都是跟ItemNum一样
            kotItemDetail.setCategoryId(cpKotItemDetail.getCategoryId());
            kotItemDetail.setIsTakeAway(cpKotItemDetail.getIsTakeAway());
            kotItemDetail.setKotSummaryUniqueId(cpKotItemDetail.getKotSummaryUniqueId());
            KotItemDetailSQL.update(kotItemDetail);
        }
        return kotItemDetail;
    }

    public KotItemDetail getSubKotItemDetail(KotItemDetail mainKotItemDetail) {
        KotItemDetail kotItemDetail = null;
        synchronized (lock_getKotItemDetail) {
            kotItemDetail = KotItemDetailSQL
                    .getSubKotItemDetailByMainKotItemDeail(mainKotItemDetail);
            if (kotItemDetail == null) {
                long time = System.currentTimeMillis();
                kotItemDetail = new KotItemDetail();
                int next = CommonSQL.getKotNextSeq(TableNames.KotItemDetail);
//                int next = CommonSQL.getNextSeq(TableNames.KotItemDetail);
//                int nextWithRevId = Integer.parseInt(mainKotItemDetail.getRevenueId() + "0");
//                if (next < nextWithRevId) {
//                    next = Integer.parseInt(mainKotItemDetail.getRevenueId() + "" + next);
//                } else {
//                    String n = "" + CommonSQL.getCurrentSeq(TableNames.KotItemDetail);
//                    String revId = "" + mainKotItemDetail.getRevenueId();
//                    int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                    next = Integer.parseInt(revId + "" + nxt);
//                }
                kotItemDetail.setId(next);
                kotItemDetail.setUniqueId(CommonSQL.getUniqueId());
                kotItemDetail.setRestaurantId(mainKotItemDetail.getRestaurantId());
                kotItemDetail.setRevenueId(mainKotItemDetail.getRevenueId());
                kotItemDetail.setOrderId(mainKotItemDetail.getOrderId());
                kotItemDetail.setOrderDetailId(mainKotItemDetail.getOrderDetailId());
                kotItemDetail.setPrinterGroupId(mainKotItemDetail.getPrinterGroupId());
                kotItemDetail.setKotSummaryId(mainKotItemDetail.getKotSummaryId());
                kotItemDetail.setItemId(mainKotItemDetail.getId());
                kotItemDetail.setItemName(mainKotItemDetail.getItemName());
                kotItemDetail.setItemNum(mainKotItemDetail.getItemNum());
                kotItemDetail.setFinishQty(mainKotItemDetail.getFinishQty());
                kotItemDetail.setSessionStatus(mainKotItemDetail.getSessionStatus());
                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNSEND);
                kotItemDetail.setSpecialInstractions(mainKotItemDetail
                        .getSpecialInstractions());
                kotItemDetail.setVersion(0); // 没用
                kotItemDetail.setCreateTime(time);
                kotItemDetail.setUpdateTime(time);
                kotItemDetail.setUnFinishQty(mainKotItemDetail.getUnFinishQty());
                kotItemDetail.setCategoryId(ParamConst.KOTITEMDETAIL_CATEGORYID_SUB);
                kotItemDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
                kotItemDetail.setFireStatus(kotItemDetail.getFireStatus());
                kotItemDetail.setKotSummaryUniqueId(mainKotItemDetail.getKotSummaryUniqueId());
                KotItemDetailSQL.update(kotItemDetail);
            }
        }
        return kotItemDetail;
    }

    // add:thread safe
    Object lock_getKotItemModifier = new Object();

    public KotItemModifier getKotItemModifier(KotItemDetail kotItemDetail,
                                              OrderModifier orderModifier, Modifier modifier) {

        KotItemModifier kotItemModifier = null;
        synchronized (lock_getKotItemModifier) {
            kotItemModifier = KotItemModifierSQL
                    .getKotItemModifier(kotItemDetail.getId(), modifier.getId());
            if (kotItemModifier == null) {
                kotItemModifier = new KotItemModifier();
                int next = CommonSQL.getKotNextSeq(TableNames.KotItemModifier);
//                int next = CommonSQL.getNextSeq(TableNames.KotItemModifier);
//                int nextWithRevId = Integer.parseInt(kotItemDetail.getRevenueId() + "0");
//                if (next < nextWithRevId) {
//                    next = Integer.parseInt(kotItemDetail.getRevenueId() + "" + next);
//                } else {
//                    String n = "" + CommonSQL.getCurrentSeq(TableNames.KotItemModifier);
//                    String revId = "" + kotItemDetail.getRevenueId();
//                    int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                    next = Integer.parseInt(revId + "" + nxt);
//                }

                kotItemModifier.setId(next);
                kotItemModifier.setUniqueId(CommonSQL.getUniqueId());
                kotItemModifier.setKotItemDetailId(kotItemDetail.getId());
                kotItemModifier.setModifierId(modifier.getId());
                kotItemModifier.setModifierName(modifier.getModifierName());
                kotItemModifier.setModifierNum(modifier.getQty());
                kotItemModifier.setStatus(ParamConst.KOT_STATUS_UNSEND);
                kotItemModifier.setPrinterId(orderModifier.getPrinterId());
                kotItemModifier.setKotItemDetailUniqueId(kotItemDetail.getUniqueId());
                KotItemModifierSQL.update(kotItemModifier);
            }
        }
        return kotItemModifier;
    }

    // add:thread safe
    Object lock_getKotNotification = new Object();

    public KotNotification getKotNotification(SessionStatus sessionStatus,
                                              KotSummary kotSummary, KotItemDetail kotItemDetail) {
        KotNotification kotNotification = null;

        synchronized (lock_getKotNotification) {
            kotNotification = KotNotificationSQL
                    .getKotNotification(kotItemDetail.getOrderDetailId(), kotItemDetail.getId());
            if (kotNotification == null) {
                kotNotification = new KotNotification();
                int next = CommonSQL.getKotNextSeq(TableNames.KotNotification);
//                int next = CommonSQL.getNextSeq(TableNames.KotNotification);
//                int nextWithRevId = Integer.parseInt(kotItemDetail.getRevenueId() + "0");
//                if (next < nextWithRevId) {
//                    next = Integer.parseInt(kotItemDetail.getRevenueId() + "" + next);
//                } else {
//                    String n = "" + CommonSQL.getCurrentSeq(TableNames.KotNotification);
//                    String revId = "" + kotItemDetail.getRevenueId();
//                    int nxt = Integer.parseInt(n.substring(revId.length())) + 1;
//                    next = Integer.parseInt(revId + "" + nxt);
//                }

                kotNotification.setId(next);
                kotNotification.setUniqueId(CommonSQL.getUniqueId());
                kotNotification.setItemName(kotItemDetail.getItemName());
                kotNotification.setOrderId(kotSummary.getOrderId());
                kotNotification.setOrderDetailId(kotItemDetail.getOrderDetailId());
                kotNotification.setQty(kotItemDetail.getFinishQty());
                kotNotification.setRevenueCenterId(kotSummary.getRevenueCenterId());
                kotNotification.setRevenueCenterName(kotSummary
                        .getRevenueCenterName());
                kotNotification.setSession(sessionStatus.getSession_status());
                kotNotification.setTableName(kotSummary.getTableName());
                kotNotification.setStatus(ParamConst.KOTNOTIFICATION_STATUS_NORMAL);
                kotNotification.setUnFinishQty(kotItemDetail.getUnFinishQty());
                kotNotification.setKotItemDetailId(kotItemDetail.getId());
                kotNotification.setKotItemNum(kotItemDetail.getItemNum());
                kotNotification.setKotItemDetailUniqueId(kotItemDetail.getUniqueId());
                KotNotificationSQL.update(kotNotification);
            } else {
                kotNotification.setStatus(ParamConst.KOTNOTIFICATION_STATUS_NORMAL);
                KotNotificationSQL.update(kotNotification);
            }
        }
        return kotNotification;
    }

    public UserTimeSheet getUserTimeSheet(long businessDate, User user,
                                          RevenueCenter revenueCenter) {
        UserTimeSheet userTimeSheet = new UserTimeSheet();
        userTimeSheet.setId(CommonSQL.getNextSeq(TableNames.UserTimeSheet));
        userTimeSheet.setBusinessDate(businessDate);
        userTimeSheet.setRestaurantId(revenueCenter.getRestaurantId());
        userTimeSheet.setRevenueId(revenueCenter.getId());
        userTimeSheet.setUserId(user.getId());
        userTimeSheet.setEmpId(user.getEmpId());
        userTimeSheet.setEmpName(user.getFirstName() + user.getLastName());
        userTimeSheet.setLoginTime(System.currentTimeMillis());
        userTimeSheet.setStatus(ParamConst.USERTIMESHEET_STATUS_LOGIN);
        return userTimeSheet;
    }

    // add:thread safe
    Object lock_getReportDiscount = new Object();

    public ReportDiscount getReportDiscount(TableInfo tables, Order order,
                                            User user, RevenueCenter revenueCenter, long businessDate) {

        ReportDiscount reportDiscount = null;
        int posId = 0;
        int placesId = 0;
        int pack = 4;
        if (tables != null) {
            if (!IntegerUtils.isEmptyOrZero(tables.getPosId())) {
                posId = tables.getPosId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPlacesId())) {
                placesId = tables.getPlacesId();
            }
            if (!IntegerUtils.isEmptyOrZero(tables.getPacks())) {
                pack = tables.getPacks();
            }
        }
        synchronized (lock_getReportDiscount) {
            reportDiscount = ReportDiscountSQL
                    .getReportDiscountByOrderId(order.getId());
            if (reportDiscount == null) {
                reportDiscount = new ReportDiscount();
                reportDiscount.setId(CommonSQL
                        .getNextSeq(TableNames.ReportDiscount));
                reportDiscount.setRestaurantId(revenueCenter.getId());
                reportDiscount.setRestaurantId(CoreData.getInstance()
                        .getRestaurant().getId());
                reportDiscount.setUserId(user.getId());
                reportDiscount.setOrderId(order.getId());
                reportDiscount.setRevenueName(revenueCenter.getRevName());
                reportDiscount.setBusinessDate(businessDate);
                // reportDiscount.setBillNumber(OrderBillSQL.getOrderBillByOrder(order).getBillNo());
                reportDiscount.setTableId(posId);
                reportDiscount.setTableName(tables.getName());
                reportDiscount.setActuallAmount("0");// TODO
                reportDiscount.setDiscount("0.00");// TODO
                reportDiscount.setGrandTotal("0.00");// TODO
                ReportDiscountSQL.update(reportDiscount);
            } else {
                reportDiscount.setUserId(user.getId());
                // reportDiscount.setActuallAmount("0");//TODO
                // reportDiscount.setDiscount("0");//TODO
                // reportDiscount.setGrandTotal("0");//TODO
                ReportDiscountSQL.update(reportDiscount);
            }
        }
        return reportDiscount;
    }

    public CashInOut getCashInOut(RevenueCenter revenueCenter,
                                  long businessDate, User user, int type, String cash, String comment) {
        CashInOut cashInOut = new CashInOut();
        cashInOut.setId(CommonSQL.getNextSeq(TableNames.CashInOut));
        cashInOut.setRestaurantId(CoreData.getInstance().getRestaurant().getId());
        cashInOut.setRevenueId(revenueCenter.getId());
        cashInOut.setUserId(user.getId());
        cashInOut.setEmpId(user.getEmpId());
        cashInOut.setEmpName(user.getUserName());
        cashInOut.setBusinessDate(businessDate);
        cashInOut.setType(type);
        cashInOut.setComment(comment);
        cashInOut.setCash(cash);
        cashInOut.setCreateTime(System.currentTimeMillis() + "");
        CashInOutSQL.update(cashInOut);
        return cashInOut;
    }

    //Add log
    public EventLog addEventLog(RevenueCenter revenueCenter, User user, long businessDate, String event) {
        EventLog eventLog = new EventLog();
        eventLog.setId(CommonSQL.getNextSeq(TableNames.EventLog));
        eventLog.setCustId(user.getId());
        eventLog.setCreatedDate(System.currentTimeMillis());
        eventLog.setEvent(event);
        EventLogSQL.update(eventLog);
        return eventLog;
    }

    Object lock_LocalDevice = new Object();

    public LocalDevice getLocalDevice(String name, String model, int type, int deviceId,
                                      String ip, String mac, String printerName, int intisLablePrinter) {
        LocalDevice localDevice = null;
        synchronized (lock_LocalDevice) {
            localDevice = LocalDeviceSQL.getLocalDeviceByDeviceId(deviceId);
            if (localDevice == null) {
                localDevice = new LocalDevice();
                localDevice.setId(CommonSQL.getNextSeq(TableNames.LocalDevice));
            }
            localDevice.setDeviceName(name);
            localDevice.setDeviceMode(model);
            localDevice.setConnected(1);
            localDevice.setDeviceType(type);
            localDevice.setDeviceId(deviceId);
            localDevice.setIp(ip);
            localDevice.setMacAddress(mac);
            localDevice.setPrinterName(printerName);
            localDevice.setIsLablePrinter(intisLablePrinter);
        }
        return localDevice;
    }

    // add:thread safe
    Object lock_getCardsSettlement = new Object();

    public CardsSettlement getCardsSettlement(Payment payment,
                                              PaymentSettlement paymentSettlement, int paymentTypeId,
                                              String cardNo, String cvvNo, String cardExpiryDateStr) {

        CardsSettlement cardsSettlement = null;

        synchronized (lock_getCardsSettlement) {
            cardsSettlement = CardsSettlementSQL
                    .getCardsSettlementByPament(payment.getId(),
                            paymentSettlement.getId());
            long times = System.currentTimeMillis();
            if (cardsSettlement == null) {
                cardsSettlement = new CardsSettlement();
                cardsSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.CardsSettlement));
                cardsSettlement.setPaymentId(payment.getId());
                cardsSettlement.setPaymentSettId(paymentSettlement.getId());
                cardsSettlement.setBillNo(payment.getBillNo());
                cardsSettlement.setCardNo(cardNo);
                cardsSettlement.setCardType(paymentTypeId);
                cardsSettlement.setCvvNo(Integer.parseInt(cvvNo));
                cardsSettlement.setCardExpiryDate(cardExpiryDateStr);
                cardsSettlement.setCreateTime(times);
                cardsSettlement.setUpdateTime(times);
                cardsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
            } else {
                cardsSettlement.setCardNo(cardNo);
                cardsSettlement.setCvvNo(Integer.parseInt(cvvNo));
                cardsSettlement.setCardExpiryDate(cardExpiryDateStr);
                cardsSettlement.setUpdateTime(times);
            }
            CardsSettlementSQL.addCardsSettlement(cardsSettlement);
        }
        return cardsSettlement;
    }

    public CardsSettlement getCardsSettlementForKPMG(int paymentId,
                                                     PaymentSettlement paymentSettlement, int paymentTypeId,
                                                     String cardNo, int billNo) {

        CardsSettlement cardsSettlement = null;

        synchronized (lock_getCardsSettlement) {
            cardsSettlement = CardsSettlementSQL
                    .getCardsSettlementByPament(paymentId,
                            paymentSettlement.getId());
            long times = System.currentTimeMillis();
            if (cardsSettlement == null) {
                cardsSettlement = new CardsSettlement();
                cardsSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.CardsSettlement));
                cardsSettlement.setPaymentId(paymentId);
                cardsSettlement.setPaymentSettId(paymentSettlement.getId());
                cardsSettlement.setBillNo(billNo);
                cardsSettlement.setCardNo(cardNo);
                cardsSettlement.setCardType(paymentTypeId);
                cardsSettlement.setCvvNo(0);
                cardsSettlement.setCardExpiryDate("11/88");
                cardsSettlement.setCreateTime(times);
                cardsSettlement.setUpdateTime(times);
                cardsSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
            } else {
                cardsSettlement.setCardNo(cardNo);
                cardsSettlement.setCvvNo(0);
                cardsSettlement.setCardExpiryDate("11/88");
                cardsSettlement.setUpdateTime(times);
            }
            CardsSettlementSQL.addCardsSettlement(cardsSettlement);
        }
        return cardsSettlement;
    }

    Object lock_getAlipaySettlement = new Object();

    public AlipaySettlement getAlipaySettlement(Payment payment,
                                                PaymentSettlement paymentSettlement, String tradeNo, String buyerEmail) {

        AlipaySettlement alipaySettlement = null;

        synchronized (lock_getAlipaySettlement) {
            alipaySettlement = AlipaySettlementSQL
                    .getAlipaySettlementByPament(payment.getId(),
                            paymentSettlement.getId());
            long times = System.currentTimeMillis();
            if (alipaySettlement == null) {
                alipaySettlement = new AlipaySettlement();
                alipaySettlement.setId(CommonSQL
                        .getNextSeq(TableNames.AlipaySettlement));
                alipaySettlement.setPaymentId(payment.getId());
                alipaySettlement.setPaymentSettId(paymentSettlement.getId());
                alipaySettlement.setBillNo(payment.getBillNo());
                alipaySettlement.setTradeNo(tradeNo);
                alipaySettlement.setBuyerEmail(buyerEmail);
                alipaySettlement.setCreateTime(times);
                alipaySettlement.setUpdateTime(times);
                alipaySettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                AlipaySettlementSQL.addAlipaySettlement(alipaySettlement);
            }

        }
        return alipaySettlement;
    }

    Object lock_getWeixinSettlement = new Object();

    public WeixinSettlement getWeixinSettlement(Payment payment,
                                                PaymentSettlement paymentSettlement, String tradeNo, String buyerEmail) {

        WeixinSettlement weixinSettlement = null;

        synchronized (lock_getAlipaySettlement) {
            weixinSettlement = WeixinSettlementSQL
                    .getWeixinSettlementByPament(payment.getId(),
                            paymentSettlement.getId());
            long times = System.currentTimeMillis();
            if (weixinSettlement == null) {
                weixinSettlement = new WeixinSettlement();
                weixinSettlement.setId(CommonSQL
                        .getNextSeq(TableNames.WeixinSettlement));
                weixinSettlement.setPaymentId(payment.getId());
                weixinSettlement.setPaymentSettId(paymentSettlement.getId());
                weixinSettlement.setBillNo(payment.getBillNo());
                weixinSettlement.setTradeNo(tradeNo);
                weixinSettlement.setBuyerEmail(buyerEmail);
                weixinSettlement.setCreateTime(times);
                weixinSettlement.setUpdateTime(times);
                weixinSettlement.setIsActive(ParamConst.PAYMENT_SETT_IS_ACTIVE);
                WeixinSettlementSQL.addWeixinSettlement(weixinSettlement);
            }

        }
        return weixinSettlement;
    }

    Object lock_getOrderSplit = new Object();

    /**
     * 只有在添加OrderDetail的groupId的时候才调用
     *
     * @param order
     * @return
     */
    public OrderSplit getOrderSplit(Order order, int groupId, Tax inclusiveTax) {
        OrderSplit orderSplit = null;
        synchronized (lock_getOrderSplit) {
            orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, groupId);
            long times = System.currentTimeMillis();
            if (orderSplit == null) {
                orderSplit = new OrderSplit();
                orderSplit.setId(CommonSQL.getNextSeq(TableNames.OrderSplit));
                orderSplit.setOrderId(order.getId());
                orderSplit.setOrderOriginId(order.getOrderOriginId());
                orderSplit.setUserId(order.getUserId());
                orderSplit.setPersons(1);
                orderSplit.setOrderStatus(ParamConst.ORDERSPLIT_ORDERSTATUS_OPEN);
                orderSplit.setSessionStatus(order.getSessionStatus());
                orderSplit.setRestId(order.getRestId());
                orderSplit.setRevenueId(order.getRevenueId());
                orderSplit.setTableId(order.getTableId());
                orderSplit.setCreateTime(times);
                orderSplit.setUpdateTime(times);
                orderSplit.setGroupId(groupId);
                orderSplit.setSplitByPax(ParamConst.SPLIT_BY_PAX_FALSE);
                if (inclusiveTax != null) {
                    orderSplit.setInclusiveTaxName(inclusiveTax.getTaxName());
                    orderSplit.setInclusiveTaxPercentage(inclusiveTax.getTaxPercentage());
                }
                OrderSplitSQL.add(orderSplit);
            } else {
                orderSplit.setUpdateTime(times);
            }
        }
        return orderSplit;
    }

    public List<OrderSplit> getOrderSplitListForPax(Order order, int pax) {
        List<OrderSplit> orderSplitList = new ArrayList<>();
        for (int i = 1; i <= pax; i++) {
            long times = System.currentTimeMillis();
            OrderSplit orderSplit = new OrderSplit();
            orderSplit.setId(CommonSQL.getNextSeq(TableNames.OrderSplit));
            orderSplit.setOrderId(order.getId());
            orderSplit.setOrderOriginId(order.getOrderOriginId());
            orderSplit.setUserId(order.getUserId());
            orderSplit.setPersons(1);
            orderSplit.setOrderStatus(ParamConst.ORDERSPLIT_ORDERSTATUS_OPEN);
            orderSplit.setSessionStatus(order.getSessionStatus());
            orderSplit.setRestId(order.getRestId());
            orderSplit.setRevenueId(order.getRevenueId());
            orderSplit.setTableId(order.getTableId());
            orderSplit.setCreateTime(times);
            orderSplit.setUpdateTime(times);
            orderSplit.setGroupId(i);
            orderSplit.setTotal(BH.div(BH.getBD(order.getTotal()), BH.getBD(pax), true).toString());
            orderSplit.setSubTotal(order.getSubTotal());
            orderSplit.setInclusiveTaxName(order.getInclusiveTaxName());
            orderSplit.setInclusiveTaxPercentage(BH.div(BH.getBD(order.getInclusiveTaxPercentage()), BH.getBD(pax), true).toString());
            orderSplit.setSplitByPax(pax);
            OrderSplitSQL.update(orderSplit);
            orderSplitList.add(orderSplit);
        }
        return orderSplitList;
    }

    public UserOpenDrawerRecord getUserOpenDrawerRecord(int restaurantId, int revenueCenterId, User openUser, int loginUserId, int sessionStatus) {
        UserOpenDrawerRecord userOpenDrawerRecord = new UserOpenDrawerRecord();
        userOpenDrawerRecord.setId(CommonSQL.getNextSeq(TableNames.UserOpenDrawerRecord));
        userOpenDrawerRecord.setRestaurantId(restaurantId);
        userOpenDrawerRecord.setRevenueCenterId(revenueCenterId);
        userOpenDrawerRecord.setSessionStatus(sessionStatus);
        userOpenDrawerRecord.setUserId(openUser.getId().intValue());
        userOpenDrawerRecord.setUserName(openUser.getFirstName() + openUser.getLastName());
        userOpenDrawerRecord.setLoginUserId(loginUserId);
        userOpenDrawerRecord.setOpenTime(System.currentTimeMillis());
        UserOpenDrawerRecordSQL.addUserOpenDrawerRecord(userOpenDrawerRecord);
        return userOpenDrawerRecord;
    }

    public void getPrintOrder(Order order) {
        order.setDiscountAmount(BH.formatMoney(order.getDiscountAmount()).toString());
        order.setDiscountPrice(BH.formatMoney(order.getDiscountPrice()).toString());
        order.setInclusiveTaxPercentage(BH.formatMoney(order.getInclusiveTaxPercentage()).toString());
        order.setInclusiveTaxPrice(BH.formatMoney(order.getInclusiveTaxPrice()).toString());
        order.setTotal(BH.formatMoney(order.getTotal()).toString());
        order.setOldTotal(BH.formatMoney(order.getOldTotal()).toString());
        order.setTaxAmount(BH.formatMoney(order.getTaxAmount()).toString());
        order.setSubTotal(BH.formatMoney(order.getSubTotal()).toString());
        order.setDiscountRate(BH.formatMoney(order.getDiscountRate()).toString());
        order.setPromotion(BH.formatMoney(order.getPromotion()).toString());
        //return order;
    }

    public PrintOrderItem getPrintOrderItem(PrintOrderItem printOrderItem) {
        printOrderItem.setAmount(BH.formatMoney(printOrderItem.getAmount()).toString());
        printOrderItem.setPrice(BH.formatMoney(printOrderItem.getPrice()).toString());
        return printOrderItem;
    }

    public PrintOrderModifier getPrintOrderModifier(PrintOrderModifier printOrderModifier) {
        printOrderModifier.setAmount(BH.formatMoney(printOrderModifier.getAmount()).toString());
        printOrderModifier.setPrice(BH.formatMoney(printOrderModifier.getPrice()).toString());
        return printOrderModifier;
    }

    public void getPrintModifier(Modifier modifier) {
        modifier.setPrice(BH.formatMoney(modifier.getPrice()).toString());

    }


    public void getReportDayTax(ReportDayTax reportDayTax) {
        reportDayTax.setTaxAmount(BH.formatMoney(reportDayTax.getTaxAmount()).toString());
        reportDayTax.setTaxPercentage(BH.formatMoney(reportDayTax.getTaxPercentage()).toString());

    }

    public void getPrintReceiptInfo(PrintReceiptInfo printReceiptInfo) {
        printReceiptInfo.setCashChange(BH.formatMoney(printReceiptInfo.getCashChange()).toString());
        printReceiptInfo.setPaidAmount(BH.formatMoney(printReceiptInfo.getPaidAmount()).toString());

    }

    public void getReportDayPayment(ReportDayPayment reportDayPayment) {
        reportDayPayment.setOverPaymentAmount(BH.formatMoney(reportDayPayment.getOverPaymentAmount()).toString());
        reportDayPayment.setPaymentAmount(BH.formatMoney(reportDayPayment.getPaymentAmount()).toString());
    }

    public void getReportSessionSales(ReportSessionSales reportSessionSales) {
        reportSessionSales.setActualAmount(BH.formatMoney(reportSessionSales.getActualAmount()).toString());
        reportSessionSales.setCash(BH.formatMoney(reportSessionSales.getCash()).toString());
        reportSessionSales.setDifference(BH.formatMoney(reportSessionSales.getDifference()).toString());
        reportSessionSales.setExpectedAmount(BH.formatMoney(reportSessionSales.getExpectedAmount()).toString());
        reportSessionSales.setCashTopup(BH.formatMoney(reportSessionSales.getCashTopup()).toString());
    }

    public void getReportPluDayItem(ReportPluDayItem reportPluDayItem) {
        reportPluDayItem.setBillFocPrice(BH.formatMoney(reportPluDayItem.getBillFocPrice()).toString());
        reportPluDayItem.setBillVoidPrice(BH.formatMoney(reportPluDayItem.getBillVoidPrice()).toString());
        reportPluDayItem.setItemAmount(BH.formatMoney(reportPluDayItem.getItemAmount()).toString());

        reportPluDayItem.setItemFocPrice(BH.formatMoney(reportPluDayItem.getItemFocPrice()).toString());
        reportPluDayItem.setItemHoldPrice(BH.formatMoney(reportPluDayItem.getItemHoldPrice()).toString());
        reportPluDayItem.setItemPrice(BH.formatMoney(reportPluDayItem.getItemPrice()).toString());
        reportPluDayItem.setItemVoidPrice(BH.formatMoney(reportPluDayItem.getItemVoidPrice()).toString());

    }

    public void getReportPluDayModifier(ReportPluDayModifier reportPluDayModifier) {
        reportPluDayModifier.setBillFocPrice(BH.formatMoney(reportPluDayModifier.getBillFocPrice()).toString());
        reportPluDayModifier.setBillVoidPrice(BH.formatMoney(reportPluDayModifier.getBillVoidPrice()).toString());
        reportPluDayModifier.setBohModifierPrice(BH.formatMoney(reportPluDayModifier.getBohModifierPrice()).toString());

        reportPluDayModifier.setFocModifierPrice(BH.formatMoney(reportPluDayModifier.getFocModifierPrice()).toString());
        reportPluDayModifier.setModifierItemPrice(BH.formatMoney(reportPluDayModifier.getModifierItemPrice()).toString());
        reportPluDayModifier.setModifierPrice(BH.formatMoney(reportPluDayModifier.getModifierPrice()).toString());
        reportPluDayModifier.setRealPrice(BH.formatMoney(reportPluDayModifier.getRealPrice()).toString());
        reportPluDayModifier.setVoidModifierPrice(BH.formatMoney(reportPluDayModifier.getVoidModifierPrice()).toString());

    }


    public void getReportHourly(ReportHourly reportHourly) {
        reportHourly.setAmountPrice(BH.formatMoney(reportHourly.getAmountPrice()).toString());
    }


    public void getPrintReportDaySales(ReportDaySales reportDaySales) {
        reportDaySales.setAlipay(BH.formatType(reportDaySales.getAlipay()).toString());
        reportDaySales.setAmex(BH.formatType(reportDaySales.getAmex()).toString());
        reportDaySales.setBillRefund(BH.formatType(reportDaySales.getBillRefund()).toString());
        reportDaySales.setBillVoid(BH.formatType(reportDaySales.getBillVoid()).toString());
        reportDaySales.setCash(BH.formatType(reportDaySales.getCash()).toString());
        reportDaySales.setCashInAmt(BH.formatType(reportDaySales.getCashInAmt()).toString());
        reportDaySales.setCashOutAmt(BH.formatType(reportDaySales.getCashOutAmt()).toString());
        reportDaySales.setDeliveroo(BH.formatType(reportDaySales.getDeliveroo()).toString());
        reportDaySales.setDiner(BH.formatType(reportDaySales.getDiner()).toString());
        reportDaySales.setDiscountAmt(BH.formatType(reportDaySales.getDiscountAmt()).toString());
        reportDaySales.setDiscount(BH.formatType(reportDaySales.getDiscount()).toString());
        reportDaySales.setDiscountPer(BH.formatType(reportDaySales.getDiscountPer()).toString());
        reportDaySales.setExpectedAmount(BH.formatType(reportDaySales.getExpectedAmount()).toString());
        reportDaySales.setFocBill(BH.formatType(reportDaySales.getFocBill()).toString());
        reportDaySales.setFoodpanda(BH.formatType(reportDaySales.getFoodpanda()).toString());
        reportDaySales.setInclusiveTaxAmt(BH.formatType(reportDaySales.getInclusiveTaxAmt()).toString());
        reportDaySales.setItemSales(BH.formatType(reportDaySales.getItemSales()).toString());
        reportDaySales.setItemVoid(BH.formatType(reportDaySales.getItemVoid()).toString());
        reportDaySales.setDifference(BH.formatType(reportDaySales.getDifference()).toString());
        reportDaySales.setJbl(BH.formatType(reportDaySales.getJbl()).toString());
        reportDaySales.setMc(BH.formatType(reportDaySales.getMc()).toString());
        reportDaySales.setFocItem(BH.formatType(reportDaySales.getFocItem()).toString());
        reportDaySales.setNets(BH.formatType(reportDaySales.getNets()).toString());
        reportDaySales.setNettSales(BH.formatType(reportDaySales.getNettSales()).toString());
        reportDaySales.setRefundTax(BH.formatType(reportDaySales.getRefundTax()).toString());
        reportDaySales.setStartDrawerAmount(BH.formatType(reportDaySales.getStartDrawerAmount()).toString());
        reportDaySales.setStoredCard(BH.formatType(reportDaySales.getStoredCard()).toString());
        reportDaySales.setPaypalpay(BH.formatType(reportDaySales.getPaypalpay()).toString());
        reportDaySales.setTakeawaySales(BH.formatType(reportDaySales.getTakeawaySales()).toString());
        reportDaySales.setTakeawayTax(BH.formatType(reportDaySales.getTakeawayTax()).toString());
        reportDaySales.setThirdParty(BH.formatType(reportDaySales.getThirdParty()).toString());
        reportDaySales.setTopUps(BH.formatType(reportDaySales.getTopUps()).toString());
        reportDaySales.setTotalBalancePrice(BH.formatType(reportDaySales.getTotalBalancePrice()).toString());
        reportDaySales.setTotalCard(BH.formatType(reportDaySales.getTotalCard()).toString());
        reportDaySales.setTotalCash(BH.formatType(reportDaySales.getTotalCash()).toString());
        reportDaySales.setTotalHour(BH.formatType(reportDaySales.getTotalHour()).toString());
        reportDaySales.setTotalSales(BH.formatType(reportDaySales.getTotalSales()).toString());
        reportDaySales.setTotalTax(BH.formatType(reportDaySales.getTotalTax()).toString());
        reportDaySales.setUbereats(BH.formatType(reportDaySales.getUbereats()).toString());
        reportDaySales.setUnionPay(BH.formatType(reportDaySales.getUnionPay()).toString());
        reportDaySales.setVarianceAmt(BH.formatType(reportDaySales.getVarianceAmt()).toString());
        reportDaySales.setVisa(BH.formatType(reportDaySales.getVisa()).toString());
        reportDaySales.setVoucher(BH.formatType(reportDaySales.getVoucher()).toString());
        reportDaySales.setWaiterAmount(BH.formatType(reportDaySales.getWaiterAmount()).toString());
        reportDaySales.setWeixinpay(BH.formatType(reportDaySales.getWeixinpay()).toString());
        reportDaySales.setPromotionTotal(BH.formatType(reportDaySales.getPromotionTotal()).toString());
    }
}
