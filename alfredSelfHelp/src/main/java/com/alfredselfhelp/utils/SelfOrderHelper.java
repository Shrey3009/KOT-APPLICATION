package com.alfredselfhelp.utils;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.OrderHelper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SelfOrderHelper {
    private static SelfOrderHelper instance;

    private SelfOrderHelper() {

    }

    public static SelfOrderHelper getInstance() {
        if (instance == null) {
            instance = new SelfOrderHelper();
        }
        return instance;
    }

    Object lock_order = new Object();

    public Order getOrder(Integer orderOriginId, int subPosBeanId, RevenueCenter revenueCenter, User user,
                          SessionStatus sessionStatus, long businessDate, int orderNOTitle,
                          int orderStatus, Tax inclusiveTax, int appOrderId) {

        Order order = null;
        synchronized (lock_order) {
            order = new Order();
            order.setId(CommonSQL.getNextSeq(TableNames.Order));
            order.setOrderOriginId(orderOriginId);
            order.setUserId(user.getId());
            order.setPersons(1);
            order.setOrderStatus(orderStatus);
            order.setDiscountRate(ParamConst.DOUBLE_ZERO);
            order.setSessionStatus(sessionStatus.getSession_status());
            order.setRestId(CoreData.getInstance().getRestaurant().getId());
            order.setRevenueId(revenueCenter.getId());
            order.setPlaceId(0);
            order.setTableId(0);
            long time = System.currentTimeMillis();
            order.setCreateTime(time);
            order.setUpdateTime(time);
            order.setBusinessDate(businessDate);
            order.setOrderNo(com.alfredbase.utils.OrderHelper.calculateOrderNo(businessDate));//流水号
            order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_NULL);
            order.setAppOrderId(appOrderId);
            if (inclusiveTax != null) {
                order.setInclusiveTaxName(inclusiveTax.getTaxName());
                order.setInclusiveTaxPercentage(inclusiveTax.getTaxPercentage());
            }
            order.setSubPosBeanId(0);
        }
        return order;
    }

    public OrderDetail getOrderDetailFromList(ItemDetail itemDetail, List<OrderDetail> orderDetails) {
        if (orderDetails != null && orderDetails.size() > 0) {
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getItemId().intValue() == itemDetail.getId().intValue()) {
                    return orderDetail;
                }
            }
        }
        return null;
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
        orderDetail.setItemUrl(itemDetail.getImgUrl());
        orderDetail.setItemDesc(itemDetail.getItemDesc());
        return orderDetail;
    }

    public void addOrderDetailETCForWaiterFirstAdd(Order order, OrderDetail orderDetail, List<OrderDetail> orderDetails) {
        if (orderDetail == null) {
            return;
        }
//        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        calculateOrderDetail(order, orderDetail);
        orderDetails.add(orderDetail);
        if (updateFreeOrderDetailForWaiter(order, orderDetail, orderDetails)) {
            calculateOrderDetail(order, orderDetail);
        }
        calculate(order, orderDetails);
    }

    public void calculateOrderDetail(Order order, OrderDetail orderDetail) {
        orderDetail.setModifierPrice(OrderHelper.getOrderDetailModifierPrice(
                order, orderDetail).toString());

        orderDetail.setRealPrice(OrderHelper.getOrderDetailRealPrice(order,
                orderDetail).toString());
    }

    private boolean updateFreeOrderDetailForWaiter(Order order,
                                                   OrderDetail orderDetail, List<OrderDetail> orderDetails) {
        ItemHappyHour itemHappyHour = OrderHelper.getItemHappyHour(order,
                orderDetail);
        if (itemHappyHour != null && itemHappyHour.getFreeNum().intValue() > 0) {
            ItemDetail itemDetail = CoreData.getInstance()
                    .getItemDetailByTemplateId(itemHappyHour.getFreeItemId());
            if (itemDetail == null) {
                return false;
            }
            OrderDetail freeOrderDetail = getFreeOrderDetailForWaiter(order, orderDetail,
                    itemDetail, itemHappyHour);
            freeOrderDetail
                    .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
            orderDetails.add(freeOrderDetail);
            return true;
        }
        return false;
    }

    public OrderDetail getFreeOrderDetailForWaiter(Order order,
                                                   OrderDetail fromOrderDetail, ItemDetail itemDetail,
                                                   ItemHappyHour itemHappyHour) {

        OrderDetail orderDetail = null;
        synchronized (lock_order) {
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
        }
        return orderDetail;
    }

    public void calculate(Order order, List<OrderDetail> orderDetailList) {

        OrderHelper.setOrderSubTotal(order, orderDetailList);
        updateOrderDetail(order, orderDetailList);
        OrderHelper.setOrderDiscount(order, orderDetailList);
        OrderHelper.setOrderTax(order, orderDetailList);
        OrderHelper.setOrderTotal(order, orderDetailList);
        OrderHelper.setOrderInclusiveTaxPrice(order);
    }


    public void updateOrderDetailAndOrderForWaiter(Order order, List<OrderDetail> orderDetails,
                                                   OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        calculateOrderDetail(order, orderDetail);
        calculate(order, orderDetails);
    }

    private void updateOrderDetail(Order order, List<OrderDetail> orderDetails) {
        if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_ORDER) {
            for (OrderDetail orderDetail : orderDetails) {
                // 本身是送的，不参与打折
                if (orderDetail.getIsFree() == ParamConst.FREE) {
                    continue;
                }
                if (orderDetail.getIsItemDiscount() == ParamConst.ITEM_NO_DISCOUNT) {
                    continue;
                }
                if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
                    continue;
                }
                if (orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                        && orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
                    orderDetail.setDiscountRate(order.getDiscountRate());
                    orderDetail
                            .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE);
                    orderDetail.setDiscountPrice(BH.mul(BH.getBDNoFormat(order.getDiscountRate()), BH.getBD(orderDetail.getRealPrice()), false).toString());
//                    OrderDetailSQL.updateOrderDetail(orderDetail);
                }
            }
        } else if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_ORDER) {
//            String sumRealPrice = OrderDetailSQL.getOrderDetailRealPriceWhenDiscountBySelf(order);
            String sumRealPrice = getSunRealPrice(orderDetails);
            if (BH.compare(BH.getBD(order.getSubTotal()), BH.getBD(sumRealPrice))) {
                String discount_rate = BH.div(BH.getBD(order.getDiscountPrice()),
                        BH.sub(BH.getBD(order.getSubTotal()), BH.getBD(sumRealPrice), false), false).toString();
                for (OrderDetail orderDetail : orderDetails) {
                    // 本身是送的，不参与打折
                    if (orderDetail.getIsFree() == ParamConst.FREE) {
                        continue;
                    }
                    if (orderDetail.getIsItemDiscount() == ParamConst.ITEM_NO_DISCOUNT) {
                        continue;
                    }
                    if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
                        continue;
                    }
                    if (orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                            && orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
                        orderDetail.setDiscountRate(discount_rate);
                        orderDetail
                                .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB);
                        orderDetail.setDiscountPrice(BH.mul(BH.getBDNoFormat(discount_rate), BH.getBD(orderDetail.getRealPrice()), false).toString());
//                        OrderDetailSQL.updateOrderDetail(orderDetail);
                    }
                }
            }
        } else if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_CATEGORY) {
            List<String> categoryId = Arrays.asList(order.getDiscountCategoryId().split(","));
            for (OrderDetail orderDetail : orderDetails) {
                // 本身是送的，不参与打折
                if (orderDetail.getIsFree() == ParamConst.FREE) {
                    continue;
                }
                if (orderDetail.getIsItemDiscount() == ParamConst.ITEM_NO_DISCOUNT) {
                    continue;
                }
                if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
                    continue;
                }
                if (categoryId != null && categoryId.size() > 0) {
                    if (categoryId.contains(orderDetail.getMainCategoryId() + "")) {
                        if (orderDetail.getMainCategoryId() != 0
                                && categoryId.contains(orderDetail.getMainCategoryId() + "")
                                && (orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE
                                || orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)) {
                            orderDetail.setDiscountRate(order.getDiscountRate());
                            orderDetail
                                    .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE);
                            orderDetail.setDiscountPrice(BH.mul(BH.getBDNoFormat(order.getDiscountRate()), BH.getBD(orderDetail.getRealPrice()), false).toString());
//                            OrderDetailSQL.updateOrderDetail(orderDetail);
                        }
                    } else if (orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                            && orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
                        orderDetail.setDiscountRate("");
                        orderDetail.setDiscountPrice("0.00");
                        orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
//                        OrderDetailSQL.updateOrderDetail(orderDetail);
                    }
                }
            }
        } else if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_CATEGORY) {
            List<String> categoryId = Arrays.asList(order.getDiscountCategoryId().split(","));
            BigDecimal sumRatePrice = BH.getBD(ParamConst.DOUBLE_ZERO);

//				String discount_rate = BH.div(BH.getBD(order.getDiscountPrice()),
//						BH.sub(BH.getBD(order.getSubTotal()), BH.getBD(sumRealPrice), false), false).toString();
//				BigDecimal discount_rate = BH.getBD(ParamConst.DOUBLE_ZERO);
//				BigDecimal
            for (OrderDetail orderDetail : orderDetails) {
                // 本身是送的，不参与打折
                if (orderDetail.getIsFree() == ParamConst.FREE) {
                    continue;
                }
                if (orderDetail.getIsItemDiscount() == ParamConst.ITEM_NO_DISCOUNT) {
                    continue;
                }
                if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
                    continue;
                }
                if (categoryId != null && categoryId.size() > 0) {
                    if (orderDetail.getMainCategoryId() != 0
                            && categoryId.contains(orderDetail.getMainCategoryId() + "")
                            && (orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB
                            || orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)) {
                        sumRatePrice = BH.add(sumRatePrice, BH.getBD(orderDetail.getRealPrice()), false);
                    }
                }
            }

            if (sumRatePrice.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) == 1) {
                BigDecimal discount_rate = BH.div(BH.getBD(order.getDiscountPrice()),
                        sumRatePrice, false);
                for (OrderDetail orderDetail : orderDetails) {
                    // 本身是送的，不参与打折
                    if (orderDetail.getIsFree() == ParamConst.FREE) {
                        continue;
                    }
                    if (orderDetail.getIsItemDiscount() == ParamConst.ITEM_NO_DISCOUNT) {
                        continue;
                    }
                    if (!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())) {
                        continue;
                    }
                    if (categoryId != null && categoryId.size() > 0) {
                        if (categoryId.contains(orderDetail.getMainCategoryId() + "")) {
                            if (orderDetail.getMainCategoryId() != 0
                                    && (orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB
                                    || orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)) {
                                orderDetail.setDiscountRate(discount_rate.toString());
                                orderDetail
                                        .setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB);
                                orderDetail.setDiscountPrice(BH.mul(discount_rate, BH.getBD(orderDetail.getRealPrice()), false).toString());
//                                OrderDetailSQL.updateOrderDetail(orderDetail);
                            }
                        } else if (orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                                && orderDetail.getDiscountType().intValue() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
                            orderDetail.setDiscountRate("");
                            orderDetail.setDiscountPrice("0.00");
                            orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
//                            OrderDetailSQL.updateOrderDetail(orderDetail);
                        }
                    }
                }
            }
        }
    }

    private String getSunRealPrice(List<OrderDetail> orderDetails) {
        BigDecimal sum = BH.getBD(ParamConst.DOUBLE_ZERO);
        if (orderDetails != null && orderDetails.size() > 0) {
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                        || orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {

                    sum = BH.add(sum, BH.getBD(orderDetail.getRealPrice()), true);
                }
            }
        }
        return sum.toString();
    }
    //修改RemainingStock数量
    public void updateRemainingStockNum(Order order){

        List<OrderDetail> orderDetailList = OrderDetailSQL.getOrderDetails(order.getId());

        for(OrderDetail orderDetail: orderDetailList) {
            int itemTempId = CoreData.getInstance().getItemDetailById(orderDetail.getItemId(),orderDetail.getItemName()).getItemTemplateId();
            RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);

            if (remainingStock != null) {
                RemainingStockSQL.updateRemainingById(orderDetail.getItemNum(), itemTempId);
            }
        }
    }
}
