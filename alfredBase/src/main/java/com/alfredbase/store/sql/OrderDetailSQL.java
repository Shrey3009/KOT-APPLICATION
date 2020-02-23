package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.OrderHelper;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailSQL {

    /**
     * 1.添加OrderDetail，
     * <p>
     * 2.添加送的OrderDetail
     * <p>
     * 3.刷新Order表
     */
    public static void addOrderDetailETC(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        if (order == null)
            return;
        if (order.getOrderStatus() == ParamConst.ORDER_STATUS_FINISHED) {
            return;
        }
        if(order.getIsTakeAway() ==  ParamConst.TAKE_AWAY)
        {
            orderDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
        }
        calculate(order, orderDetail);
        add(orderDetail);
        OrderHelper.addDefaultModifiers(order, orderDetail);
        updateFreeOrderDetail(order, orderDetail);
        OrderSQL.updateOrder(order);
    }

    public static void addOrderDetailETCFromWaiter(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        if (order == null)
            return;
        if (order.getOrderStatus() == ParamConst.ORDER_STATUS_FINISHED) {
            return;
        }
        if(order.getIsTakeAway() ==  ParamConst.TAKE_AWAY)
        {
            orderDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
        }
        calculate(order, orderDetail);
        add(orderDetail);
        updateFreeOrderDetail(order, orderDetail);
        OrderSQL.updateOrder(order);
    }

    public static void addOrderDetailETCForWaiter(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        if(order.getIsTakeAway() ==  ParamConst.TAKE_AWAY)
        {
            orderDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
        }
        calculate(order, orderDetail);
        updateOrderDetail(orderDetail);
        updateFreeOrderDetailForWaiter(order, orderDetail);
        OrderSQL.updateOrder(order);
    }

    public static void addOrderDetailETCForWaiterFirstAdd(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        if(order.getIsTakeAway() ==  ParamConst.TAKE_AWAY)
        {
            orderDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
        }
        calculate(order, orderDetail);
        updateOrderDetail(orderDetail);
        OrderHelper.addDefaultModifiers(order, orderDetail);
        updateFreeOrderDetailForWaiter(order, orderDetail);
        OrderSQL.updateOrder(order);
    }

    /**
     * 修改OrderDetail的Discount信息时调用
     *
     * @param orderDetail
     */
    public static void updateOrderDetailAndOrderByDiscount(
            OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        calculate(order, orderDetail);
        updateOrderDetail(orderDetail);

        updateFreeOrderDetail(order, orderDetail);
//		order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_BY_ORDERDETAIL);
        OrderSQL.updateOrder(order);
        if (orderDetail.getGroupId().intValue() > 0) {
            OrderSplit orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
            if (orderSplit != null) {
                OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
            }
        }
    }

    /**
     * 修改OrderDetail的非Discount信息时调用
     *
     * @param orderDetail
     */
    public static void updateOrderDetailAndOrder(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
//		orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
//		orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
//		orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        calculate(order, orderDetail);
        updateOrderDetail(orderDetail);

        updateFreeOrderDetail(order, orderDetail);
//		order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_BY_ORDERDETAIL);
        OrderSQL.updateOrder(order);
        if (orderDetail.getGroupId().intValue() > 0) {
            OrderSplit orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
            if (orderSplit != null) {
                OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
            }
        }
    }

    public static void updateOrderDetailAndOrderForWaiter(
            OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
//		orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
//		orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
//		orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        calculate(order, orderDetail);
        updateOrderDetail(orderDetail);

        updateFreeOrderDetailForWaiter(order, orderDetail);
//		order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_BY_ORDERDETAIL);
        OrderSQL.updateOrder(order);
    }

    public static void updateOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.OrderDetail
                    + "(id,orderId, orderOriginId, userId, itemId,itemName,itemNum, orderDetailStatus, orderDetailType,reason, printStatus, itemPrice,"
                    + " taxPrice, discountPrice, modifierPrice, realPrice, createTime, updateTime,discountRate,discountType,fromOrderDetailId,isFree,"
                    + " groupId,isOpenItem, specialInstractions, orderSplitId, isTakeAway, weight, isItemDiscount, isSet, appOrderDetailId, mainCategoryId,"
                    + " fireStatus,itemUrl,barCode,orderDetailRound)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{orderDetail.getId(),
                            orderDetail.getOrderId(),
                            orderDetail.getOrderOriginId(),
                            orderDetail.getUserId(), orderDetail.getItemId(),
                            orderDetail.getItemName(),
                            orderDetail.getItemNum(),
                            orderDetail.getOrderDetailStatus(),
                            orderDetail.getOrderDetailType(),
                            orderDetail.getReason(),
                            orderDetail.getPrintStatus(),
                            orderDetail.getItemPrice(),
                            orderDetail.getTaxPrice(),
                            orderDetail.getDiscountPrice(),
                            orderDetail.getModifierPrice(),
                            orderDetail.getRealPrice(),
                            orderDetail.getCreateTime(),
                            orderDetail.getUpdateTime(),
                            orderDetail.getDiscountRate(),
                            orderDetail.getDiscountType(),
                            orderDetail.getFromOrderDetailId(),
                            orderDetail.getIsFree(), orderDetail.getGroupId(),
                            orderDetail.getIsOpenItem(),
                            orderDetail.getSpecialInstractions(),
                            orderDetail.getOrderSplitId(),
                            orderDetail.getIsTakeAway(),
                            orderDetail.getWeight(),
                            orderDetail.getIsItemDiscount(),
                            orderDetail.getIsSet(),
                            orderDetail.getAppOrderDetailId(),
                            orderDetail.getMainCategoryId(),
                            orderDetail.getFireStatus(),
                            orderDetail.getItemUrl(),
                            orderDetail.getBarCode(),
                            orderDetail.getOrderDetailRound()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateOrderDetail(SQLiteDatabase db, OrderDetail orderDetail) {
        if (orderDetail == null) {
            return;
        }
        try {
            String sql = "replace into "
                    + TableNames.OrderDetail
                    + "(id,orderId, orderOriginId, userId, itemId,itemName,itemNum, orderDetailStatus, orderDetailType,reason, printStatus, itemPrice,"
                    + " taxPrice, discountPrice, modifierPrice, realPrice, createTime, updateTime,discountRate,discountType,fromOrderDetailId,isFree,"
                    + " groupId,isOpenItem, specialInstractions, orderSplitId, isTakeAway, weight, isItemDiscount, isSet, appOrderDetailId, mainCategoryId,"
                    + " fireStatus,itemUrl, barCode,orderDetailRound)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{orderDetail.getId(),
                            orderDetail.getOrderId(),
                            orderDetail.getOrderOriginId(),
                            orderDetail.getUserId(), orderDetail.getItemId(),
                            orderDetail.getItemName(),
                            orderDetail.getItemNum(),
                            orderDetail.getOrderDetailStatus(),
                            orderDetail.getOrderDetailType(),
                            orderDetail.getReason(),
                            orderDetail.getPrintStatus(),
                            orderDetail.getItemPrice(),
                            orderDetail.getTaxPrice(),
                            orderDetail.getDiscountPrice(),
                            orderDetail.getModifierPrice(),
                            orderDetail.getRealPrice(),
                            orderDetail.getCreateTime(),
                            orderDetail.getUpdateTime(),
                            orderDetail.getDiscountRate(),
                            orderDetail.getDiscountType(),
                            orderDetail.getFromOrderDetailId(),
                            orderDetail.getIsFree(), orderDetail.getGroupId(),
                            orderDetail.getIsOpenItem(),
                            orderDetail.getSpecialInstractions(),
                            orderDetail.getOrderSplitId(),
                            orderDetail.getIsTakeAway(),
                            orderDetail.getWeight(),
                            orderDetail.getIsItemDiscount(),
                            orderDetail.getIsSet(),
                            orderDetail.getAppOrderDetailId(),
                            orderDetail.getMainCategoryId(),
                            orderDetail.getFireStatus(),
                            orderDetail.getItemUrl(),
                            orderDetail.getBarCode(),
                            orderDetail.getOrderDetailRound()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateOrderDetailStatusById(int orderDetailStatus,
                                                   int id) {
        try {
            String sql = "update " + TableNames.OrderDetail
                    + " set orderDetailStatus = ? where id = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{orderDetailStatus, id});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateOrderDetailOrderIdById(int orderId,
                                                    int id) {
        try {
            String sql = "update " + TableNames.OrderDetail
                    + " set orderId = ? where id = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{orderId, id});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateOrderDetailFireStatus(int fireStatus,
                                                   int id) {
        try {
            String sql = "update " + TableNames.OrderDetail
                    + " set fireStatus = ? where id = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{fireStatus, id});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
	ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE = 5;
	public static final int ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB = 6;
	 */
    public static void updateDiscountTypeByMainCategoryId(int discountType, int mainCategoryId, int orderId) {
        try {
            String sql = "update " + TableNames.OrderDetail
                    + " set discountType = ? where orderId = ? and mainCategoryId = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{discountType, orderId, mainCategoryId});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateOrderDetailTaxById(String taxPrice, int orderDetailId) {
        try {
            String sql = "update " + TableNames.OrderDetail
                    + " set taxPrice = ? where id = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{taxPrice, orderDetailId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDiscountTypeBeforeByMainCategoryId(int orderId) {
        try {
            String sql = "update "
                    + TableNames.OrderDetail
                    + " set discountType = "
                    + ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL
                    + " where orderId = ?  and discountType <> "
                    + ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                    + " and discountType <> "
                    + ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB;
            SQLExe.getDB().execSQL(sql,
                    new Object[]{orderId});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateOrderDetailGroupId(OrderDetail orderDetail) {
        try {
            String sql = "update " + TableNames.OrderDetail
                    + " set groupId = ? where id = ?";
            SQLExe.getDB().execSQL(sql,
                    new Object[]{orderDetail.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void calculate(Order order, OrderDetail orderDetail) {
        orderDetail.setModifierPrice(OrderHelper.getOrderDetailModifierPrice(
                order, orderDetail).toString());

        orderDetail.setRealPrice(OrderHelper.getOrderDetailRealPrice(order,
                orderDetail).toString());
    }


    public static void addOrderDetailList(List<OrderDetail> orderDetailList) {
        if (orderDetailList == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.OrderDetail
                    + "(id, orderId, orderOriginId, userId, itemId, itemName, itemNum, orderDetailStatus, orderDetailType, reason, printStatus, itemPrice,"
                    + " taxPrice, discountPrice, modifierPrice, realPrice, createTime, updateTime,discountRate,discountType,fromOrderDetailId,isFree,"
                    + " groupId,isOpenItem, specialInstractions, orderSplitId, isTakeAway, weight, isItemDiscount, isSet, appOrderDetailId, mainCategoryId,"
                    + " fireStatus,itemUrl, barCode, orderDetailRound)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sql);
            for (OrderDetail orderDetail : orderDetailList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        orderDetail.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        orderDetail.getOrderId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        orderDetail.getOrderOriginId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        orderDetail.getUserId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
                        orderDetail.getItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 6,
                        orderDetail.getItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        orderDetail.getItemNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        orderDetail.getOrderDetailStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
                        orderDetail.getOrderDetailType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 10,
                        orderDetail.getReason());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 11,
                        orderDetail.getPrintStatus());
                SQLiteStatementHelper.bindString(sqLiteStatement, 12,
                        orderDetail.getItemPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
                        orderDetail.getTaxPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        orderDetail.getDiscountPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 15,
                        orderDetail.getModifierPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 16,
                        orderDetail.getRealPrice());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 17,
                        orderDetail.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 18,
                        orderDetail.getUpdateTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 19,
                        orderDetail.getDiscountRate());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        orderDetail.getDiscountType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 21,
                        orderDetail.getFromOrderDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
                        orderDetail.getIsFree());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
                        orderDetail.getGroupId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 24,
                        orderDetail.getIsOpenItem());
                SQLiteStatementHelper.bindString(sqLiteStatement, 25,
                        orderDetail.getSpecialInstractions());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 26,
                        orderDetail.getOrderSplitId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 27,
                        orderDetail.getIsTakeAway());
                SQLiteStatementHelper.bindString(sqLiteStatement, 28,
                        orderDetail.getWeight());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 29,
                        orderDetail.getIsItemDiscount());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 30,
                        orderDetail.getIsSet());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 31,
                        orderDetail.getAppOrderDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 32,
                        orderDetail.getMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 33,
                        orderDetail.getFireStatus());
                SQLiteStatementHelper.bindString(sqLiteStatement, 34,
                        orderDetail.getItemUrl());
                SQLiteStatementHelper.bindString(sqLiteStatement, 35,
                        orderDetail.getBarCode());
                SQLiteStatementHelper.bindString(sqLiteStatement,36,orderDetail.getOrderDetailRound());
                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static void addOrderDetailListFromWaiter(
            List<OrderDetail> orderDetailList) {
        if (orderDetailList == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "insert into "
                    + TableNames.OrderDetail
                    + "(orderId, orderOriginId, userId, itemId, itemName, itemNum, orderDetailStatus, orderDetailType, reason, printStatus, itemPrice,"
                    + " taxPrice, discountPrice, modifierPrice, realPrice, createTime, updateTime,discountRate,discountType,fromOrderDetailId,isFree,"
                    + " groupId,isOpenItem, specialInstractions, orderSplitId, isTakeAway, weight, isItemDiscount, isSet, appOrderDetailId, mainCategoryId,"
                    + " fireStatus,itemUrl, barCode,orderDetailRound)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sql);
            for (OrderDetail orderDetail : orderDetailList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        orderDetail.getOrderId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        orderDetail.getOrderOriginId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        orderDetail.getUserId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        orderDetail.getItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        orderDetail.getItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        orderDetail.getItemNum());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 7,
                        orderDetail.getOrderDetailStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 8,
                        orderDetail.getOrderDetailType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 9,
                        orderDetail.getReason());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
                        orderDetail.getPrintStatus());
                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
                        orderDetail.getItemPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 12,
                        orderDetail.getTaxPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
                        orderDetail.getDiscountPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        orderDetail.getModifierPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 15,
                        orderDetail.getRealPrice());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        orderDetail.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 17,
                        orderDetail.getUpdateTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 18,
                        orderDetail.getDiscountRate());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 19,
                        orderDetail.getDiscountType());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 20,
                        orderDetail.getFromOrderDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 21,
                        orderDetail.getIsFree());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 22,
                        orderDetail.getGroupId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 23,
                        orderDetail.getIsOpenItem());
                SQLiteStatementHelper.bindString(sqLiteStatement, 24,
                        orderDetail.getSpecialInstractions());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 25,
                        orderDetail.getOrderSplitId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 26,
                        orderDetail.getIsTakeAway());
                SQLiteStatementHelper.bindString(sqLiteStatement, 27,
                        orderDetail.getWeight());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 28,
                        orderDetail.getIsItemDiscount());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 29,
                        orderDetail.getIsSet());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 30,
                        orderDetail.getAppOrderDetailId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 31,
                        orderDetail.getMainCategoryId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 32,
                        orderDetail.getFireStatus());
                SQLiteStatementHelper.bindString(sqLiteStatement, 33,
                        orderDetail.getItemUrl());
                SQLiteStatementHelper.bindString(sqLiteStatement, 34,
                        orderDetail.getBarCode());
                SQLiteStatementHelper.bindString(sqLiteStatement,36,orderDetail.getOrderDetailRound());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static void add(OrderDetail orderDetail) {
        try {
            String sql = "insert into "
                    + TableNames.OrderDetail
                    + "(orderId, orderOriginId, userId, itemId, itemName, itemNum, orderDetailStatus, orderDetailType, reason, printStatus, itemPrice,"
                    + " taxPrice, discountPrice, modifierPrice, realPrice, createTime, updateTime,discountRate,discountType,fromOrderDetailId,isFree,"
                    + " groupId,isOpenItem,specialInstractions, orderSplitId, isTakeAway, weight, isItemDiscount, isSet, appOrderDetailId, mainCategoryId,"
                    + " fireStatus,itemUrl, barCode,orderDetailRound)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{orderDetail.getOrderId(),
                            orderDetail.getOrderOriginId(),
                            orderDetail.getUserId(), orderDetail.getItemId(),
                            orderDetail.getItemName(),
                            orderDetail.getItemNum(),
                            orderDetail.getOrderDetailStatus(),
                            orderDetail.getOrderDetailType(),
                            orderDetail.getReason(),
                            orderDetail.getPrintStatus(),
                            orderDetail.getItemPrice(),
                            orderDetail.getTaxPrice(),
                            orderDetail.getDiscountPrice(),
                            orderDetail.getModifierPrice(),
                            orderDetail.getRealPrice(),
                            orderDetail.getCreateTime(),
                            orderDetail.getUpdateTime(),
                            orderDetail.getDiscountRate(),
                            orderDetail.getDiscountType(),
                            orderDetail.getFromOrderDetailId(),
                            orderDetail.getIsFree(), orderDetail.getGroupId(),
                            orderDetail.getIsOpenItem(),
                            orderDetail.getSpecialInstractions(),
                            orderDetail.getOrderSplitId(),
                            orderDetail.getIsTakeAway(),
                            orderDetail.getWeight(),
                            orderDetail.getIsItemDiscount(),
                            orderDetail.getIsSet(),
                            orderDetail.getAppOrderDetailId(),
                            orderDetail.getMainCategoryId(),
                            orderDetail.getFireStatus(),
                            orderDetail.getItemUrl(),
                            orderDetail.getBarCode(),
                            orderDetail.getOrderDetailRound()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addForWaiter(OrderDetail orderDetail) {
        try {
            String sql = "insert into "
                    + TableNames.OrderDetail
                    + "(id, orderId, orderOriginId, userId, itemId, itemName,itemNum, orderDetailStatus, orderDetailType, reason, printStatus, itemPrice,"
                    + " taxPrice, discountPrice, modifierPrice, realPrice, createTime, updateTime,discountRate,discountType,fromOrderDetailId,isFree,"
                    + " groupId,isOpenItem,specialInstractions, orderSplitId, isTakeAway,weight, isItemDiscount, isSet, appOrderDetailId, mainCategoryId,"
                    + " fireStatus,itemUrl, barCode,orderDetailRound)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{orderDetail.getId(),
                            orderDetail.getOrderId(),
                            orderDetail.getOrderOriginId(),
                            orderDetail.getUserId(), orderDetail.getItemId(),
                            orderDetail.getItemName(),
                            orderDetail.getItemNum(),
                            orderDetail.getOrderDetailStatus(),
                            orderDetail.getOrderDetailType(),
                            orderDetail.getReason(),
                            orderDetail.getPrintStatus(),
                            orderDetail.getItemPrice(),
                            orderDetail.getTaxPrice(),
                            orderDetail.getDiscountPrice(),
                            orderDetail.getModifierPrice(),
                            orderDetail.getRealPrice(),
                            orderDetail.getCreateTime(),
                            orderDetail.getUpdateTime(),
                            orderDetail.getDiscountRate(),
                            orderDetail.getDiscountType(),
                            orderDetail.getFromOrderDetailId(),
                            orderDetail.getIsFree(), orderDetail.getGroupId(),
                            orderDetail.getIsOpenItem(),
                            orderDetail.getSpecialInstractions(),
                            orderDetail.getOrderSplitId(),
                            orderDetail.getIsTakeAway(),
                            orderDetail.getWeight(),
                            orderDetail.getIsItemDiscount(),
                            orderDetail.getIsSet(),
                            orderDetail.getAppOrderDetailId(),
                            orderDetail.getMainCategoryId(),
                            orderDetail.getFireStatus(),
                            orderDetail.getItemUrl(),
                            orderDetail.getBarCode(),
                            orderDetail.getOrderDetailRound()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<OrderDetail> getAllOrderDetail() {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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


    public static int getOrderDetailCountByGroupId(int groupId, int orderId) {
        String sql = "select count(*) from " + TableNames.OrderDetail + " where groupId = ? and orderId = ? and orderDetailType <> " + ParamConst.ORDERDETAIL_TYPE_VOID;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{groupId + "", orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    public static int getCreatedOrderDetailCountForWaiter(int orderId) {
        String sql = "select count(*) from " + TableNames.OrderDetail + " where orderDetailStatus > "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_ADD
                + " and orderId = ? and orderDetailType <> " + ParamConst.ORDERDETAIL_TYPE_VOID;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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


    public static int getCreatedOrderDetailCountForKpm(int orderId) {
        String sql = "select count(*) from " + TableNames.OrderDetail + " where orderDetailStatus >= "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_ADD
                + " and orderId = ? and orderDetailType <> " + ParamConst.ORDERDETAIL_TYPE_VOID;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    public static int getOrderDetailSplitCountByOrder(Order order) {
        String sql = "select count(*) from " + TableNames.OrderDetail + " where groupId <> 0 and orderId = ?";
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    public static int getOrderDetailCountByOrder(Order order) {
        String sql = "select count(*) from " + TableNames.OrderDetail + " where orderId = ? and orderDetailStatus > "
                + ParamConst.ORDERDETAIL_STATUS_ADDED;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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


    public static int getOrderDetailCountByOrderIdAndItemDetailId(int orderId, int itemDetailId) {
        String sql = "select sum(itemNum) from " + TableNames.OrderDetail + " where orderId = ? and itemId = ? and orderDetailStatus <="
                + ParamConst.ORDERDETAIL_STATUS_ADDED;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + "", itemDetailId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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


    //waiter菜单列表上的数量
    public static int getOrderNotSubmitDetailCountByOrderIdAndItemDetailId(int orderId, int itemDetailId) {
        String sql = "select sum(itemNum) from " + TableNames.OrderDetail + " where orderId = ? and itemId = ? and orderDetailStatus <="
                + ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + "", itemDetailId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    //菜单列表上的数量
    public static int getOrderAddDetailCountByOrderIdAndItemDetailId(int orderId, int itemDetailId) {
        String sql = "select sum(itemNum) from " + TableNames.OrderDetail + " where orderId = ? and itemId = ? and orderDetailStatus ="
                + ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + "", itemDetailId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    public static int getOrderDetailPlaceOrderCountByOrder(Order order) {
        String sql = "select count(*) from " + TableNames.OrderDetail + " where orderId = ? and orderDetailType <> "
                + ParamConst.ORDERDETAIL_TYPE_VOID
                + " and orderDetailStatus > "
                + ParamConst.ORDERDETAIL_STATUS_ADDED;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    public static ArrayList<OrderDetail> getAllOrderDetailByTime(
            long businessDate) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId in ( select id from " + TableNames.Order
                + " where businessDate = ? and orderStatus = "
                + ParamConst.ORDER_STATUS_FINISHED + " )";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    public static ArrayList<OrderDetail> getAllOrderDetailByTime(
            long businessDate, SessionStatus sessionStatus) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where orderId in ( select id from "
                + TableNames.Order
                + " where businessDate = ? and sessionStatus = ? and createTime > ? and orderStatus = "
                + ParamConst.ORDER_STATUS_FINISHED + " )";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(
                    sql,
                    new String[]{String.valueOf(businessDate),
                            String.valueOf(sessionStatus.getSession_status()),
                            String.valueOf(sessionStatus.getTime())});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    public static ArrayList<OrderDetail> getAllOrderDetailsByOrder(Order order) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    /**
     * 作为打印的时候调用的
     *
     * @param orderId
     * @return
     */
    public static ArrayList<OrderDetail> getOrderDetailsForPrint(int orderId) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailType <> "
                + ParamConst.ORDERDETAIL_TYPE_VOID + " order by createTime";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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


    public static ArrayList<OrderDetail> getCreatedOrderDetails(int orderId) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailStatus > "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_ADD
                + " and orderDetailType <> "
                + ParamConst.ORDERDETAIL_TYPE_VOID + " order by createTime";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    /**
     * 作为展示的时候调用的
     *
     * @param orderId
     * @return
     */
    public static ArrayList<OrderDetail> getOrderDetailsForFire(int orderId) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailStatus = "
                + ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD
                + " and orderDetailType <> "
                + ParamConst.ORDERDETAIL_TYPE_VOID + " order by fireStatus, id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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


    /**
     * 作为展示的时候调用的
     *
     * @param orderId
     * @return
     */
    public static ArrayList<OrderDetail> getOrderDetails(int orderId) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailType <> " +
                +ParamConst.ORDERDETAIL_TYPE_VOID
                + " and orderSplitId not in (select id from "
                + TableNames.OrderSplit
                + " where orderId = ? and orderStatus > "
                + ParamConst.ORDERSPLIT_ORDERSTATUS_UNPAY
                + ") order by groupId desc";
//        String sql = "select * from " + TableNames.OrderDetail
//                + " where orderId = ? and orderDetailType <> " +
//                +ParamConst.ORDERDETAIL_TYPE_VOID
//                + " and groupId not in (select groupId from "
//                + TableNames.OrderSplit
//                + " where orderId = ? and orderStatus > "
//                + ParamConst.ORDERSPLIT_ORDERSTATUS_UNPAY
//                + ") order by groupId desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + "", orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    /**
     * 作为算账的时候使用的
     *
     * @param orderId
     * @return
     */
    public static ArrayList<OrderDetail> getGeneralOrderDetails(int orderId) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    public static ArrayList<OrderDetail> getUnFreeOrderDetails(Order order) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and isFree = " + ParamConst.NOT_FREE;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    public static ArrayList<OrderDetail> getUnFreeOrderDetailsWithOutSplit(Order order, String orderSpliteIds) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ?  and orderSplitId not in (" + orderSpliteIds + ")and isFree = " + ParamConst.NOT_FREE;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                result.add(orderDetail);
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


    public static ArrayList<OrderDetail> getUnFreeOrderDetailsForWaiter(Order order) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderDetailStatus = "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_ADD
                + " and orderId = ? and isFree = " + ParamConst.NOT_FREE + " order by updateTime";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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


    public static ArrayList<OrderDetail> getUnFreeOrderDetailsForKpm(Order order) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderDetailStatus = "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_ADD
                + " and orderId = ? and isFree = " + ParamConst.NOT_FREE;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderDetail orderDetail = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

//	public static int getUnFreeOrderDetailsNumInKOTOrPOS(Order order,
//			ItemDetail itemDetail, Integer groupId) {
//		int sum = 0;
//		String sql = "select sum( itemNum) from "
//				+ TableNames.OrderDetail
//				+ " where orderId = ? and itemId = ? and groupId = ? and orderDetailStatus < "
//				+ ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD
//				+ " and isFree = " + ParamConst.NOT_FREE
//				+ " and orderDetailType = "
//				+ ParamConst.ORDERDETAIL_TYPE_GENERAL;
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(
//					sql,
//					new String[] { order.getId() + "", itemDetail.getId() + "",
//							groupId + "" });
//			if (cursor.moveToFirst()) {
//				sum = cursor.getInt(0);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return sum;
//	}

    public static OrderDetail getOrderDetail(Integer orderDetailID) {
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where id = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{orderDetailID + ""});
            if (cursor.moveToFirst()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetail;
    }

    public static int getMaxGroupId(Order order) {
        int groupId = 0;
        String sql = "select max(groupId) from " + TableNames.OrderDetail
                + " where orderId = ?";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{order.getId() + ""});
            if (cursor.moveToFirst()) {
                groupId = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return groupId;
    }

    public static OrderDetail getUnFreeOrderDetail(Order order,
                                                   ItemDetail itemDetail, Integer groupId, int orderDetailStatus) {
        OrderDetail orderDetail = null;
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where orderId = ? and itemId = ? and groupId = ? and orderDetailStatus = ? and isFree = "
                + ParamConst.NOT_FREE + " and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{order.getId() + "", itemDetail.getId() + "",
                            groupId + "", orderDetailStatus + ""});
            if (cursor.moveToFirst()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetail;
    }

    public static List<OrderDetail> getOrderDetails(Order order, Integer groupId) {
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and groupId = ? and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + "",
                    groupId + ""});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static List<OrderDetail> getOrderDetails(Order order,
                                                    Integer groupId, int orderDetailStatus) {
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where orderId = ? and groupId = ? and orderDetailStatus = ? and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + "",
                    groupId + "", orderDetailStatus + ""});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static List<OrderDetail> getOrderDetailsUnZero(Order order,
                                                          Integer groupId) {
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and groupId = ? and orderDetailStatus > "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE
                + " and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{order.getId() + "",
                    groupId + "",});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static int getOrderDetailsCountUnPlaceOrder(int orderId) {
        int orderDetailsCount = 0;
        String sql = "select count(itemNum) from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailStatus < "
                + ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD;
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{String.valueOf(orderId)});
            if (cursor.moveToFirst()) {
                orderDetailsCount = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetailsCount;
    }

    public static int getSumOrderDetailItemNumByTime(long businessDate) {
        int sumItemNum = 0;
        String sql = "select sum(itemNum) from " + TableNames.OrderDetail
                + " where orderId in ( select id from " + TableNames.Order
                + " where businessDate = ?)";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(sql,
                    new String[]{String.valueOf(businessDate)});
            if (cursor.moveToFirst()) {
                sumItemNum = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return sumItemNum;
    }

    public static Map<String, String> getSumOrderDetailDiscountByTimeAndType(
            int type, long businessDate) {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select sum(itemNum), count(*) from "
                + TableNames.OrderDetail
                + " where discountType = ? and orderId in ( select id from "
                + TableNames.Order + " where businessDate = ?)";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(type),
                            String.valueOf(businessDate)});
            if (cursor.moveToFirst()) {
                map.put("sumItemNum", cursor.getString(0) == null ? "0.00"
                        : cursor.getString(0));
                map.put("count", String.valueOf(cursor.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    public static Map<String, String> getSumOrderDetailDiscountByTimeAndVoidOrFreeType(
            int type, long businessDate) {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select sum(itemNum), sum(realPrice) from "
                + TableNames.OrderDetail
                + " where orderDetailType = ? and orderId in ( select id from "
                + TableNames.Order + " where businessDate = ?)";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(type),
                            String.valueOf(businessDate)});
            if (cursor.moveToFirst()) {
                map.put("sumItemNum", String.valueOf(cursor.getInt(0)));
                map.put("sumRealPrice", cursor.getString(1) == null ? "0.00"
                        : cursor.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    public static Map<String, String> getSumOrderDetailDiscountByTimeAndVoidOrFreeType(
            int type, long businessDate, SessionStatus sessionStatus) {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select sum(itemNum), sum(realPrice) from "
                + TableNames.OrderDetail
                + " where orderDetailType = ? and orderId in ( select id from "
                + TableNames.Order
                + " where businessDate = ? and sessionStatus = ? and createTime > ?)";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(type),
                            String.valueOf(businessDate),
                            String.valueOf(sessionStatus.getSession_status()),
                            String.valueOf(sessionStatus.getTime())});
            if (cursor.moveToFirst()) {
                map.put("sumItemNum", String.valueOf(cursor.getInt(0)));
                map.put("sumRealPrice", cursor.getString(1) == null ? "0.00"
                        : cursor.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    public static OrderDetail getOrderDetail(int orderId,
                                             OrderDetail fromOrderDetail) {
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and fromOrderDetailId = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB()
                    .rawQuery(
                            sql,
                            new String[]{orderId + "",
                                    fromOrderDetail.getId() + ""});
            if (cursor.moveToFirst()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetail;
    }

    public static OrderDetail getPromotionOrderDetail(int orderId,
                                                      int fromOrderDetailId) {
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and fromOrderDetailId = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB()
                    .rawQuery(
                            sql,
                            new String[]{orderId + "",
                                    fromOrderDetailId + ""});
            if (cursor.moveToFirst()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetail;
    }

    public static OrderDetail getOrderDetailByAppOrderDetailId(int appOrderDeailId) {
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where appOrderDetailId = ? ";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB()
                    .rawQuery(
                            sql,
                            new String[]{appOrderDeailId + ""});
            if (cursor.moveToFirst()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetail;
    }

    public static void deleteOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null)
            return;
        String sql = "delete from " + TableNames.OrderDetail + " where id = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{orderDetail.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 删除免费的菜
        sql = "delete from " + TableNames.OrderDetail
                + " where fromOrderDetailId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{orderDetail.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }

        OrderDetailTaxSQL.deleteOrderDetailTax(orderDetail);

        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        KotSummary kotSummary = KotSummarySQL.getKotSummary(order.getId(), order.getNumTag());
        OrderSQL.updateOrder(order);
        if (orderDetail.getGroupId().intValue() > 0) {
            OrderSplit orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
            if (orderSplit != null) {
                OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
            }
        }
        if (kotSummary != null) {
            KotItemDetailSQL.deleteKotItemDetail(kotSummary.getId(), orderDetail);
        }
    }

    public static void setOrderDetailToVoidOrFree(OrderDetail orderDetail,
                                                  int type) {
        if (orderDetail == null)
            return;
        orderDetail.setOrderDetailType(type);
        orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
        orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
        orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
        updateOrderDetail(orderDetail);
        OrderDetail freeOrderDetail = getOrderDetail(orderDetail.getOrderId(),
                orderDetail);
        if (freeOrderDetail != null) {
            freeOrderDetail.setOrderDetailType(type);
            updateOrderDetail(freeOrderDetail);
        }
        OrderDetailTaxSQL.deleteOrderDetailTax(orderDetail);
        Order order = OrderSQL.getOrder(orderDetail.getOrderId());
        OrderSQL.updateOrder(order);
        if (orderDetail.getGroupId().intValue() > 0) {
            OrderSplit orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
            if (orderSplit != null) {
                OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
            }
        }
    }

    /**
     * 已经结账的订单修改OrderDetail
     */
    public static void setOrderDetailToVoidOrFreeForClosedOrder(OrderDetail orderDetail, String oldTotal) {
        if (orderDetail != null) {
            orderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_VOID);
            orderDetail.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
            orderDetail.setDiscountPrice(ParamConst.DOUBLE_ZERO);
            orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
            updateOrderDetail(orderDetail);
            OrderDetail freeOrderDetail = getOrderDetail(orderDetail.getOrderId(),
                    orderDetail);
            if (freeOrderDetail != null) {
                freeOrderDetail.setOrderDetailType(ParamConst.ORDERDETAIL_TYPE_VOID);
                updateOrderDetail(freeOrderDetail);
            }
            OrderDetailTaxSQL.deleteOrderDetailTax(orderDetail);
            Order order = OrderSQL.getOrder(orderDetail.getOrderId());
            OrderSQL.updateOrder(order);
            if (orderDetail.getGroupId().intValue() > 0) {
                OrderSplit orderSplit = OrderSplitSQL.getOrderSplitByOrderAndGroupId(order, orderDetail.getGroupId());
                if (orderSplit != null) {
                    RoundAmount roundAmount = RoundAmountSQL.getRoundAmount(orderSplit);
                    if (roundAmount != null) {
                        OrderHelper.setOrderSplitTotalAlfterRound(orderSplit, roundAmount);
                        OrderSplitSQL.update(orderSplit);
                    }
                    OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
                    PaymentSQL.updateSplitOrderPaymentAmount(orderSplit.getTotal(), orderSplit.getId());
                    Payment payment = PaymentSQL.getPaymentByOrderSplitId(orderSplit.getId());
                    PaymentSettlementSQL.updateSplitOrderPaymentAmount(orderSplit.getTotal(), BH.sub(BH.getBD(oldTotal), BH.getBD(orderSplit.getTotal()), true).toString(), payment.getId());
                }
            } else {
                RoundAmount roundAmount = RoundAmountSQL.getRoundAmount(order);
                if (roundAmount != null) {
                    OrderHelper.setOrderTotalAlfterRound(order, roundAmount);
                    OrderSQL.update(order);
                }
                PaymentSQL.updatePaymentAmount(order.getTotal(), order.getId());
                Payment payment = PaymentSQL.getPaymentByOrderId(order.getId());
                PaymentSettlementSQL.updatePaymentAmount(order.getTotal(), BH.sub(BH.getBD(oldTotal), BH.getBD(order.getTotal()), true).toString(), payment.getId());
            }

        }
    }

    public static void deleteOrderDetailByOrder(Order order) {
        if (order == null)
            return;
        String sql = "delete from " + TableNames.OrderDetail
                + " where orderId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{order.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderDetailByOrderOutsideOrderSplit(int orderId, String orderSplitIds) {
        String sql = "delete from " + TableNames.OrderDetail
                + " where orderId = ? and orderSplitId not in(" + orderSplitIds + ")";
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{orderId});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateFreeOrderDetail(Order order,
                                              OrderDetail orderDetail) {
//        ItemHappyHour itemHappyHour = OrderHelper.getItemHappyHour(order,
//                orderDetail);
//        if (itemHappyHour != null && itemHappyHour.getFreeNum().intValue() > 0) {
//            ItemDetail itemDetail = CoreData.getInstance()
//                    .getItemDetailByTemplateId(itemHappyHour.getFreeItemId());
//            if (itemDetail == null) {
//                return;
////            }
//        ItemPromotion itemPromotion = OrderHelper.getItemPromotion(order, orderDetail);
//        if (itemPromotion != null && itemPromotion.getFreeNum().intValue() > 0&&OrderHelper.hasWeek(itemPromotion.getPromotionId())) {
//            ItemDetail itemDetail = CoreData.getInstance()
//                    .getItemDetailByTemplateId(itemPromotion.getFreeItemId());
//            if (itemDetail == null) {
//                return;
//            }
//            OrderDetail freeOrderDetail = ObjectFactory.getInstance()
//                    .getItemFreeOrderDetail(order, orderDetail, itemDetail,
//                            itemPromotion);
//            updateOrderDetail(freeOrderDetail);
//        }
    }

    private static void updateFreeOrderDetailForWaiter(Order order,
                                                       OrderDetail orderDetail) {
        ItemHappyHour itemHappyHour = OrderHelper.getItemHappyHour(order,
                orderDetail);
        if (itemHappyHour != null && itemHappyHour.getFreeNum().intValue() > 0) {
            ItemDetail itemDetail = CoreData.getInstance()
                    .getItemDetailByTemplateId(itemHappyHour.getFreeItemId());
            if (itemDetail == null) {
                return;
            }
            OrderDetail freeOrderDetail = ObjectFactory.getInstance()
                    .getFreeOrderDetailForWaiter(order, orderDetail,
                            itemDetail, itemHappyHour);
            freeOrderDetail
                    .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
            updateOrderDetail(freeOrderDetail);
        }
    }

    public static int getOrderDetailCountByItemIdAndBusinessDate(
            long businessDate, int itemType) {
        int count = 0;
        String sql = "select sum(itemNum) from " + TableNames.OrderDetail
                + " where orderId in ( select id from " + TableNames.Order
                + " where businessDate = ? ) and itemId in (select id from "
                + TableNames.ItemDetail + " where itemType = ?)";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(businessDate),
                            String.valueOf(itemType)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    public static int getOrderDetailCountByItemIdAndBusinessDate(
            long businessDate, SessionStatus sessionStatus, int itemType) {
        int count = 0;
        String sql = "select sum(itemNum) from "
                + TableNames.OrderDetail
                + " where orderId in ( select id from "
                + TableNames.Order
                + " where businessDate = ? and sessionStatus = ? and createTime > ? ) and itemId in (select id from "
                + TableNames.ItemDetail + " where itemType = ?)";
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(businessDate),
                            String.valueOf(sessionStatus.getSession_status()),
                            String.valueOf(sessionStatus.getTime()),
                            String.valueOf(itemType)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count;
    }

    public static int getNoVoidCountByOrderId(int orderId) {
        String sql = "select count(*) from "
                + TableNames.OrderDetail
                + " where orderId = ? and orderDetailType <> " + ParamConst.ORDERDETAIL_TYPE_VOID;
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderId + ""});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
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

    public static ArrayList<OrderDetail> getOrderDetailByOrderIdAndOrderOriginId(
            int orderId, int orderOriginId) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where orderId = ? and orderOriginId <> ? and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(orderId),
                            String.valueOf(orderOriginId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static ArrayList<OrderDetail> getOrderDetailByOrderIdAndOrderDetailStatus(
            int orderId, int orderDetailStatus) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where orderId = ? and orderDetailStatus = ? and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(orderId),
                    String.valueOf(orderDetailStatus)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static ArrayList<OrderDetail> getOrderDetailByOrderIdAndOrderDetailStatus(
            int orderId) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailStatus > "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE
                + " and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(orderId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static ArrayList<OrderDetail> getPartOrderDetailByOrderIdAndOrderDetailStatus(
            int orderId) {
        ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from " + TableNames.OrderDetail
                + " where orderId = ? and orderDetailStatus = "
                + ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE
                + " or orderDetailStatus = "
                + ParamConst.ORDERDETAIL_STATUS_ADDED
                + " and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{String.valueOf(orderId)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }


    public static List<OrderDetail> getOrderDetailsByOrderAndOrderSplit(OrderSplit orderSplit) {
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        OrderDetail orderDetail = null;
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where orderId = ? and groupId = ? and orderDetailType <> "
                + ParamConst.ORDERDETAIL_TYPE_VOID;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{orderSplit.getOrderId() + "", orderSplit.getGroupId() + ""});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                orderDetails.add(orderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetails;
    }

    public static OrderDetail getOrderDetailByCreateTime(long createTime,
                                                         int orderId) {
        OrderDetail orderDetail = null;
        String sql = "select * from "
                + TableNames.OrderDetail
                + " where createTime = ? and orderId = ? and orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        try {
            cursor = SQLExe.getDB().rawQuery(
                    sql,
                    new String[]{String.valueOf(createTime),
                            String.valueOf(orderId)});
            if (cursor.moveToFirst()) {
                orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderDetail;
    }

    /*
     * Include void Item and item in void Bill
     */
    public static ArrayList<OrderDetail> getVoidOrderDetailByBusinessDate(
            long businessDate) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        String sql1 = "select * from " + TableNames.OrderDetail
                + " where orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_VOID
                + " and orderId in (select id from " + TableNames.Order
                + " where businessDate = ? )";
        String sql2 = "SELECT t.* FROM "
                + TableNames.Payment
                + " p, "
                + TableNames.PaymentSettlement
                + " s, "
                + TableNames.OrderDetail
                + " t, "
                + TableNames.Order
                + " o "
                + "WHERE p.id = s.paymentId AND s.paymentTypeId = "
                + ParamConst.SETTLEMENT_TYPE_VOID
                + " and p.type = "
                + ParamConst.BILL_TYPE_UN_SPLIT
                + " AND p.orderId = t.orderId AND p.orderId = o.id AND s.isActive = "
                + ParamConst.PAYMENT_SETT_IS_ACTIVE
                + " AND p.businessDate = ? AND t.orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;

        String sql3 = "SELECT t.* FROM "
                + TableNames.Payment
                + " p, "
                + TableNames.PaymentSettlement
                + " s, "
                + TableNames.OrderDetail
                + " t, "
                + TableNames.OrderSplit
                + " o "
                + "WHERE p.id = s.paymentId AND s.paymentTypeId = "
                + ParamConst.SETTLEMENT_TYPE_VOID
                + " and p.type = "
                + ParamConst.BILL_TYPE_SPLIT
                + " AND p.orderId = t.orderId and t.groupId = o.groupId AND s.isActive = "
                + ParamConst.PAYMENT_SETT_IS_ACTIVE
                + " AND p.orderSplitId = o.id AND p.businessDate = ? AND t.orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql1,
                    new String[]{String.valueOf(businessDate)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
            }

            cursor = db.rawQuery(sql2,
                    new String[]{String.valueOf(businessDate)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
            }
            cursor = db.rawQuery(sql3,
                    new String[]{String.valueOf(businessDate)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    /*
     * Get void Items from ItemDetails,items in void bill are not included
     */
    public static Map<Integer, Map<String, String>> getVoidItemsByBusinessDate(
            long businessDate) {
        Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.query(TableNames.OrderDetail,
                    new String[]{"itemId, sum(itemNum), sum(realPrice)"},
                    "orderDetailType = " + ParamConst.ORDERDETAIL_TYPE_VOID
                            + " and orderId in (select id from "
                            + TableNames.Order + " where businessDate = ? and orderStatus ="
                            + ParamConst.ORDER_STATUS_FINISHED + ")",
                    new String[]{String.valueOf(businessDate)}, "itemId",
                    "", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                Integer itemId = cursor.getInt(0);
                Integer sumItemNum = cursor.getInt(1);
                String sumRealPrice = cursor.getString(2) == null ? "0.00"
                        : cursor.getString(2);
                map.put("sumItemNum", sumItemNum + "");
                map.put("sumRealPrice", sumRealPrice);
                result.put(itemId, map);
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


    /*
     * Get void Items from ItemDetails,items in void bill are not included
     */
    public static Map<Integer, Map<String, String>> getVoidItemsByBusinessDateAndSession(
            long businessDate, SessionStatus sessionStatus) {
        Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.query(TableNames.OrderDetail,
                    new String[]{"itemId, sum(itemNum), sum(realPrice)"},
                    "orderDetailType = " + ParamConst.ORDERDETAIL_TYPE_VOID
                            + " and orderId in (select id from "
                            + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ? and orderStatus ="
                            + ParamConst.ORDER_STATUS_FINISHED + ")",
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())}, "itemId",
                    "", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                Integer itemId = cursor.getInt(0);
                Integer sumItemNum = cursor.getInt(1);
                String sumRealPrice = cursor.getString(2) == null ? "0.00"
                        : cursor.getString(2);
                map.put("sumItemNum", sumItemNum + "");
                map.put("sumRealPrice", sumRealPrice);
                result.put(itemId, map);
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


    /*
     * Get foc Items from ItemDetails,items in foc bill are not included
     */
    public static Map<Integer, Map<String, String>> getFocItemsByBusinessDate(
            long businessDate) {
        Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.query(TableNames.OrderDetail,
                    new String[]{"itemId, sum(itemNum), sum(realPrice)"},
                    "orderDetailType = " + ParamConst.ORDERDETAIL_TYPE_FREE
                            + " and orderId in (select id from "
                            + TableNames.Order + " where businessDate = ? and orderStatus ="
                            + ParamConst.ORDER_STATUS_FINISHED + ")",
                    new String[]{String.valueOf(businessDate)}, "itemId",
                    "", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                Integer itemId = cursor.getInt(0);
                Integer sumItemNum = cursor.getInt(1);
                String sumRealPrice = cursor.getString(2) == null ? "0.00"
                        : cursor.getString(2);
                map.put("sumItemNum", sumItemNum + "");
                map.put("sumRealPrice", sumRealPrice);
                result.put(itemId, map);
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

    public static Map<Integer, Map<String, String>> getFocItemsByBusinessDateAndSession(
            long businessDate, SessionStatus sessionStatus) {
        Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.query(TableNames.OrderDetail,
                    new String[]{"itemId, sum(itemNum), sum(realPrice)"},
                    "orderDetailType = " + ParamConst.ORDERDETAIL_TYPE_FREE
                            + " and orderId in (select id from "
                            + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ? and orderStatus ="
                            + ParamConst.ORDER_STATUS_FINISHED + ")",
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())}, "itemId",
                    "", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                Integer itemId = cursor.getInt(0);
                Integer sumItemNum = cursor.getInt(1);
                String sumRealPrice = cursor.getString(2) == null ? "0.00"
                        : cursor.getString(2);
                map.put("sumItemNum", sumItemNum + "");
                map.put("sumRealPrice", sumRealPrice);
                result.put(itemId, map);
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

    public static ArrayList<OrderDetail> getFreeOrderDetailByBusinessDate(
            long businessDate) {
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        // String sql = "select * from " + TableNames.OrderDetail
        // + " where orderDetailType = "
        // + ParamConst.ORDERDETAIL_TYPE_FREE
        // + " or orderId in (select orderId from " + TableNames.Payment
        // + " where businessDate = ? and id in (select paymentId from "
        // + TableNames.PaymentSettlement + " where paymentTypeId = "
        // + ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT + "))";

        String sql1 = "select * from " + TableNames.OrderDetail
                + " where orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_FREE
                + " and orderId in (select id from " + TableNames.Order
                + " where businessDate = ?)";
        String sql2 = "SELECT t.* FROM "
                + TableNames.Payment
                + " p, "
                + TableNames.PaymentSettlement
                + " s, "
                + TableNames.OrderDetail
                + " t, "
                + TableNames.Order
                + " o "
                + "WHERE p.id = s.paymentId AND s.paymentTypeId = "
                + ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
                + " and p.type = "
                + ParamConst.BILL_TYPE_UN_SPLIT
                + " AND p.orderId = t.orderId AND p.orderId = o.id AND s.isActive = "
                + ParamConst.PAYMENT_SETT_IS_ACTIVE
                + " AND p.businessDate = ? AND t.orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;

        String sql3 = "SELECT t.* FROM "
                + TableNames.Payment
                + " p, "
                + TableNames.PaymentSettlement
                + " s, "
                + TableNames.OrderDetail
                + " t, "
                + TableNames.OrderSplit
                + " o "
                + "WHERE p.id = s.paymentId AND s.paymentTypeId = "
                + ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT
                + " and p.type = "
                + ParamConst.BILL_TYPE_SPLIT
                + " AND p.orderId = t.orderId and t.groupId = o.groupId AND s.isActive = "
                + ParamConst.PAYMENT_SETT_IS_ACTIVE
                + " AND p.orderSplitId = o.id AND p.businessDate = ? AND t.orderDetailType = "
                + ParamConst.ORDERDETAIL_TYPE_GENERAL;

        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql1,
                    new String[]{String.valueOf(businessDate)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
            }

            cursor = db.rawQuery(sql2,
                    new String[]{String.valueOf(businessDate)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
            }
            cursor = db.rawQuery(sql3,
                    new String[]{String.valueOf(businessDate)});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(cursor.getInt(0));
                orderDetail.setOrderId(cursor.getInt(1));
                orderDetail.setOrderOriginId(cursor.getInt(2));
                orderDetail.setUserId(cursor.getInt(3));
                orderDetail.setItemId(cursor.getInt(4));
                orderDetail.setItemName(cursor.getString(5));
                orderDetail.setItemNum(cursor.getInt(6));
                orderDetail.setOrderDetailStatus(cursor.getInt(7));
                orderDetail.setOrderDetailType(cursor.getInt(8));
                orderDetail.setReason(cursor.getString(9));
                orderDetail.setPrintStatus(cursor.getInt(10));
                orderDetail.setItemPrice(cursor.getString(11));
                orderDetail.setTaxPrice(cursor.getString(12));
                orderDetail.setDiscountPrice(cursor.getString(13));
                orderDetail.setModifierPrice(cursor.getString(14));
                orderDetail.setRealPrice(cursor.getString(15));
                orderDetail.setCreateTime(cursor.getLong(16));
                orderDetail.setUpdateTime(cursor.getLong(17));
                orderDetail.setDiscountRate(cursor.getString(18));
                orderDetail.setDiscountType(cursor.getInt(19));
                orderDetail.setFromOrderDetailId(cursor.getInt(20));
                orderDetail.setIsFree(cursor.getInt(21));
                orderDetail.setGroupId(cursor.getInt(22));
                orderDetail.setIsOpenItem(cursor.getInt(23));
                orderDetail.setSpecialInstractions(cursor.getString(24));
                orderDetail.setOrderSplitId(cursor.getInt(25));
                orderDetail.setIsTakeAway(cursor.getInt(26));
                orderDetail.setWeight(cursor.getString(27));
                orderDetail.setIsItemDiscount(cursor.getInt(28));
                orderDetail.setIsSet(cursor.getInt(29));
                orderDetail.setAppOrderDetailId(cursor.getInt(30));
                orderDetail.setMainCategoryId(cursor.getInt(31));
                orderDetail.setFireStatus(cursor.getInt(32));
                orderDetail.setItemUrl(cursor.getString(33));
                orderDetail.setBarCode(cursor.getString(34));
                orderDetail.setOrderDetailRound(cursor.getString(35));
                result.add(orderDetail);
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

    public static Map<String, String> getItemCountAndItemAmountByBusinessDateAndItemId(int itemId,
                                                                                       long businessDate) {
        Map<String, String> result = new HashMap<String, String>();
        String sql = "select sum(itemNum), sum(realPrice) from "
                + TableNames.OrderDetail + " where itemId = ? and orderId in (select id from "
                + TableNames.Order + " where businessDate = ?) ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(itemId), String.valueOf(businessDate)});
            if (cursor.moveToFirst()) {
                Integer sumItemNum = cursor.getInt(0);
                String sumRealPrice = cursor.getString(1) == null ? "0.00" : cursor.getString(1);
                result.put("sumItemNum", sumItemNum + "");
                result.put("sumRealPrice", sumRealPrice);
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

    public static Map<Integer, Map<String, String>> getItemCountAndItemAmountByBusinessDate(
            long businessDate) {
        Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.query(TableNames.OrderDetail,
                    new String[]{"itemId, sum(itemNum), sum(realPrice)"},
                    "orderId in (select id from "
                            + TableNames.Order + " where businessDate = ? and orderStatus ="
                            + ParamConst.ORDER_STATUS_FINISHED + ")",
                    new String[]{String.valueOf(businessDate)}, "itemId",
                    "", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                Integer itemId = cursor.getInt(0);
                Integer sumItemNum = cursor.getInt(1);
                String sumRealPrice = cursor.getString(2) == null ? "0.00"
                        : cursor.getString(2);
                map.put("sumItemNum", sumItemNum + "");
                map.put("sumRealPrice", sumRealPrice);
                result.put(itemId, map);
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

    public static Map<String, String> getItemCountAndItemAmountByBusinessDateAndItemIdAndSession(int itemId,
                                                                                                 long businessDate, SessionStatus sessionStatus) {
        Map<String, String> result = new HashMap<String, String>();
        String sql = "select sum(itemNum), sum(realPrice) from "
                + TableNames.OrderDetail + " where itemId = ? and orderId in (select id from "
                + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ?) ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(itemId), String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())});
            if (cursor.moveToFirst()) {
                Integer sumItemNum = cursor.getInt(0);
                String sumRealPrice = cursor.getString(1) == null ? "0.00" : cursor.getString(1);
                result.put("sumItemNum", sumItemNum + "");
                result.put("sumRealPrice", sumRealPrice);
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


    public static Map<Integer, Map<String, String>> getItemCountAndItemAmountByBusinessDateAndSession(
            long businessDate, SessionStatus sessionStatus) {
        Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.query(TableNames.OrderDetail,
                    new String[]{"itemId, sum(itemNum), sum(realPrice)"},
                    "orderId in (select id from "
                            + TableNames.Order + " where businessDate = ? and sessionStatus = ? and createTime > ? and orderStatus ="
                            + ParamConst.ORDER_STATUS_FINISHED + ")",
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getSession_status()), String.valueOf(sessionStatus.getTime())}, "itemId",
                    "", "", "");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                Integer itemId = cursor.getInt(0);
                Integer sumItemNum = cursor.getInt(1);
                String sumRealPrice = cursor.getString(2) == null ? "0.00"
                        : cursor.getString(2);
                map.put("sumItemNum", sumItemNum + "");
                map.put("sumRealPrice", sumRealPrice);
                result.put(itemId, map);
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

    public static String getOrderDetailRealPriceWhenDiscountBySelf(Order order) {
        String sql = "select SUM(realPrice) from "
                + TableNames.OrderDetail + " where (discountType = "
                + ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                + " or discountType = "
                + ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB
                + ") and orderId = ?";
        String sumRealPrice1 = "0.00";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{order.getId() + ""});
            if (cursor.moveToFirst()) {
                sumRealPrice1 = cursor.getString(0) == null ? "0.00" : cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        sql = "select SUM(subTotal) from "
                + TableNames.OrderSplit + " where (orderStatus = "
                + ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED
                + ") and orderId = ?";
        String sumRealPrice2 = "0.00";
        cursor = null;
        db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{order.getId() + ""});
            if (cursor.moveToFirst()) {
                sumRealPrice2 = cursor.getString(0) == null ? "0.00" : cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return String.valueOf(BH.add(new BigDecimal(sumRealPrice1), new BigDecimal(sumRealPrice2), false));
    }

    public static void deleteAllOrderDetail() {
        String sql = "delete from " + TableNames.OrderDetail;
        try {
            SQLExe.getDB().execSQL(sql, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
