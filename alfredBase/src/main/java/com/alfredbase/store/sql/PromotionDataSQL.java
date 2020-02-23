package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderPromotion;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PromotionDataSQL {

    public static void addPromotionData(OrderPromotion orderPromotion) {
        if (orderPromotion == null) {
            return;
        }

        try {
            String sql = "insert into "
                    + TableNames.OrderPromotion
                    + "(id, promotionId, promotionName, promotionType, promotionAmount,discountPercentage," +
                    "itemId,itemName,freeNum," +
                    "freeItemId,freeItemName,createTime,updateTime,orderId,orderDetailId,discountPrice,businessDate,itemNum,sessionStatus,sysCreateTime,sysUpdateTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

         
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { orderPromotion.getId(), orderPromotion.getPromotionId(),
                            orderPromotion.getPromotionName(), orderPromotion.getPromotionType(),
                            orderPromotion.getPromotionAmount(), orderPromotion.getDiscountPercentage(),
                            orderPromotion.getItemId(), orderPromotion.getItemName(),
                            orderPromotion.getFreeNum(), orderPromotion.getFreeItemId(),
                            orderPromotion.getFreeItemName(), orderPromotion.getCreateTime(), orderPromotion.getUpdateTime(),
                            orderPromotion.getOrderId(),orderPromotion.getOrderDetailId(),orderPromotion.getDiscountPrice(),
                            orderPromotion.getBusinessDate(),orderPromotion.getItemNum(),orderPromotion.getSessionStatus(),orderPromotion.getSysCreateTime(),
                            orderPromotion.getSysUpdateTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updatePromotionData(OrderPromotion orderPromotion) {
        if (orderPromotion == null) {
            return;
        }

        try {
            String sql = "replace into "
                    + TableNames.OrderPromotion
                    + "(id, promotionId, promotionName, promotionType, promotionAmount,discountPercentage," +
                    "itemId,itemName,freeNum," +
                    "freeItemId,freeItemName,createTime,updateTime,orderId,orderDetailId,discountPrice,businessDate,itemNum,sessionStatus,sysCreateTime,sysUpdateTime)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


            SQLExe.getDB().execSQL(
                    sql,
                    new Object[] { orderPromotion.getId(), orderPromotion.getPromotionId(),
                            orderPromotion.getPromotionName(), orderPromotion.getPromotionType(),
                            orderPromotion.getPromotionAmount(), orderPromotion.getDiscountPercentage(),
                            orderPromotion.getItemId(), orderPromotion.getItemName(),
                            orderPromotion.getFreeNum(), orderPromotion.getFreeItemId(),
                            orderPromotion.getFreeItemName(), orderPromotion.getCreateTime(), orderPromotion.getUpdateTime(),
                            orderPromotion.getOrderId(),orderPromotion.getOrderDetailId(),orderPromotion.getDiscountPrice(),
                            orderPromotion.getBusinessDate(),orderPromotion.getItemNum(),orderPromotion.getSessionStatus(),orderPromotion.getSysCreateTime(),
                            orderPromotion.getSysUpdateTime()
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void addPromotionItem(List<ItemPromotion> itemPromotions) {
//        if (itemPromotions == null) {
//            return;
//        }
//        SQLiteDatabase db = SQLExe.getDB();
//        try {
//            db.beginTransaction();
//
//            String sql = "insert into "
//                    + TableNames.PromotionItem
//                    + "(id, promotionId, itemMainCategoryId, itemCategoryId, itemId,type," +
//                    "discountPrice,discountPercentage,freeNum,freeItemId,itemMainCategoryName," +
//                    "itemCategoryName,itemName,freeItemName,createTime,updateTime)"
//                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//
//            SQLiteStatement sqLiteStatement = db.compileStatement(
//                    sql);
//            for (ItemPromotion itemPromotion : itemPromotions) {
//
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
//                        itemPromotion.getId());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
//                        itemPromotion.getPromotionId());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
//                        itemPromotion.getItemMainCategoryId());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
//                        itemPromotion.getItemCategoryId());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 5,
//                        itemPromotion.getItemId());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
//                        itemPromotion.getType());
//                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
//                        itemPromotion.getDiscountPrice());
//                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
//                        itemPromotion.getDiscountPercentage());
//
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 9,
//                        itemPromotion.getFreeNum());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 10,
//                        itemPromotion.getFreeItemId());
//                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
//                        itemPromotion.getItemMainCategoryName());
//                SQLiteStatementHelper.bindString(sqLiteStatement, 12,
//                        itemPromotion.getItemCategoryName());
//                SQLiteStatementHelper.bindString(sqLiteStatement, 13,
//                        itemPromotion.getItemName());
//                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
//                        itemPromotion.getFreeItemName());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 15,
//                        itemPromotion.getCreateTime());
//                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
//                        itemPromotion.getUpdateTime());
//
//                sqLiteStatement.executeInsert();
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
//    }




    public static String  getPromotionDataSum(Order order)
    {

        String sql = "select sum(promotionAmount) from " + TableNames.OrderPromotion
                + " where orderId=? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        String promotionTotal= null;
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(order.getId())});
            int count = cursor.getCount();
//            if (count < 1) {
//                return result;
//            }

            if (cursor.moveToFirst()) {
                promotionTotal=cursor.getString(0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return promotionTotal;
    }

    public static String  getPromotionDataXSum(long businessDate,SessionStatus sessionStatus, long nowTime)
    {

        String sql = "select sum(promotionAmount)  from " + TableNames.OrderPromotion
                + " where businessDate=? and createTime > ? and updateTime < ? and sessionStatus = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        String promotionTotal= null;
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getTime()), String.valueOf(nowTime),String.valueOf(sessionStatus.getTime())});
            int count = cursor.getCount();
//            if (count < 1) {
//                return result;
//            }

            if (cursor.moveToFirst()) {
                promotionTotal=cursor.getString(0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return promotionTotal;
    }

    public static Map<String, String> getPromotionTotalAndQty(long businessDate,SessionStatus sessionStatus, long nowTime)
    {
        Map<String, String> map = new HashMap<String, String>();
        String sql = "select sum(promotionAmount) , COUNT (*) from " + TableNames.OrderPromotion
                + " where businessDate=?  and createTime > ? and updateTime < ? and sessionStatus = ?" ;
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();

        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getTime()), String.valueOf(nowTime),String.valueOf(sessionStatus.getSession_status())});
            int count = cursor.getCount();
//            if (count < 1) {
//                return result;
//            }

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                map.put("promotionTotal", cursor.getString(0));
                map.put("qty", String.valueOf(cursor.getInt(1)));
            }


//            if (cursor.moveToFirst()) {
//                promotionTotal=cursor.getString(0);
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }



    public static ArrayList<OrderPromotion> getOrderPromotionData(long businessDate, SessionStatus sessionStatus, long nowTime, int type)
          {
        ArrayList<OrderPromotion> result = new ArrayList<OrderPromotion>();
        String sql = "select * from " + TableNames.OrderPromotion
                + " where businessDate=? and createTime > ? and updateTime < ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getTime()), String.valueOf(nowTime)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderPromotion orderPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setItemNum(cursor.getInt(17));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                result.add(orderPromotion);
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

    public static ArrayList<OrderPromotion> getItemPromotionData(long businessDate, SessionStatus sessionStatus, long nowTime)
    {
        ArrayList<OrderPromotion> result = new ArrayList<OrderPromotion>();
        String sql = "select * from " + TableNames.OrderPromotion
                + " where businessDate=? and createTime > ? and updateTime < ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate), String.valueOf(sessionStatus.getTime()), String.valueOf(nowTime)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderPromotion orderPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                result.add(orderPromotion);
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


    public static ArrayList<OrderPromotion> getOrderPromotionData(long businessDate, int type)
    {
        ArrayList<OrderPromotion> result = new ArrayList<OrderPromotion>();
        String sql = "select * from " + TableNames.OrderPromotion
                + " where businessDate=? and createTime > ? and updateTime < ? and  promotionType= ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate),String.valueOf(type)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderPromotion orderPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                result.add(orderPromotion);
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

    public static ArrayList<OrderPromotion> getItemPromotionData(long businessDate, int type)
    {
        ArrayList<OrderPromotion> result = new ArrayList<OrderPromotion>();
        String sql = "select * from " + TableNames.OrderPromotion
                + " where businessDate=?  and  promotionType= ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(businessDate),String.valueOf(type)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderPromotion orderPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                result.add(orderPromotion);
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
    public static ArrayList<OrderPromotion> getPromotionDataOrOrderid(int orderId)
    {
        ArrayList<OrderPromotion> result = new ArrayList<OrderPromotion>();
        String sql = "select * from " + TableNames.OrderPromotion
                + " where orderId = ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(orderId)});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderPromotion orderPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                result.add(orderPromotion);
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

    public static OrderPromotion getPromotionData(int orderId, int orderDetailId)
    {
       OrderPromotion orderPromotion = null;
        String sql = "select * from " + TableNames.OrderPromotion
                + " where orderId = ? and orderDetailId= ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(orderId),String.valueOf(orderDetailId)});
            int count = cursor.getCount();
            if (count < 1) {
                return orderPromotion;
            }

            if (cursor.moveToFirst()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                return orderPromotion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderPromotion;
    }


    public static OrderPromotion getPromotionDataOrType(int orderId, int type)
    {
        OrderPromotion orderPromotion = null;
        String sql = "select * from " + TableNames.OrderPromotion
                + " where orderId = ? and promotionType = ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(orderId),String.valueOf(type)});
            int count = cursor.getCount();
            if (count < 1) {
                return orderPromotion;
            }

            if (cursor.moveToFirst()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                return orderPromotion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderPromotion;
    }


    public static OrderPromotion getPromotionDataOrId(int orderId, int promotionId)
    {
        OrderPromotion orderPromotion = null;
        String sql = "select * from " + TableNames.OrderPromotion
                + " where orderId = ? and promotionId = ? ";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(orderId),String.valueOf(promotionId)});
            int count = cursor.getCount();
            if (count < 1) {
                return orderPromotion;
            }

            if (cursor.moveToFirst()) {
                orderPromotion = new OrderPromotion();
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                return orderPromotion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return orderPromotion;
    }


    public static ArrayList<OrderPromotion> getAllPromotionData() {
        ArrayList<OrderPromotion> result = new ArrayList<OrderPromotion>();
        String sql = "select * from " + TableNames.OrderPromotion + " order by id desc";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[] {});
            int count = cursor.getCount();
            if (count < 1) {
                return result;
            }
            OrderPromotion orderPromotion = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                orderPromotion = new OrderPromotion();
//                id, promotionId, promotionName, promotionType, promotionAmount,discountPercentage," +
//                "itemId,itemName,freeNum," +
//                        "freeItemId,freeItemName,createTime,updateTime,orderId,orderDetailId,discountPrice,businessDate
                orderPromotion.setId(cursor.getInt(0));
                orderPromotion.setPromotionId(cursor.getInt(1));
                orderPromotion.setPromotionName(cursor.getString(2));
                orderPromotion.setPromotionType(cursor.getInt(3));
                orderPromotion.setPromotionAmount(cursor.getString(4));
                orderPromotion.setDiscountPercentage(cursor.getString(5));
                orderPromotion.setItemId(cursor.getInt(6));
                orderPromotion.setItemName(cursor.getString(7));
                orderPromotion.setFreeNum(cursor.getInt(8));
                orderPromotion.setFreeItemId(cursor.getInt(9));
                orderPromotion.setFreeItemName(cursor.getString(10));
                orderPromotion.setCreateTime(cursor.getLong(11));
                orderPromotion.setUpdateTime(cursor.getLong(12));
                orderPromotion.setOrderId(cursor.getInt(13));
                orderPromotion.setOrderDetailId(cursor.getInt(14));
                orderPromotion.setDiscountPrice(cursor.getString(15));
                orderPromotion.setBusinessDate(cursor.getLong(16));
                orderPromotion.setSessionStatus(cursor.getInt(18));
                orderPromotion.setSysCreateTime(cursor.getLong(19));
                orderPromotion.setSysUpdateTime(cursor.getLong(20));
                result.add(orderPromotion);
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





    public static void deletePromotionAndFree(OrderPromotion orderPromotion) {
        String delePromotionData = "delete from " + TableNames.OrderPromotion + " where id = ?";
        // 删除免费菜的信息
        String deleteFree = "delete from "+ TableNames.OrderDetail + " where id = ?";
        Cursor cursor = null;
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            db.execSQL(delePromotionData,
                    new Object[] { String.valueOf(orderPromotion.getId())});
            db.execSQL(deleteFree,
                    new Object[] { String.valueOf(orderPromotion.getOrderDetailId())});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
    }

    public static void deletePromotionDataOrderId(Order order) {
        String sql = "delete from " + TableNames.OrderPromotion + " where orderId = ?";
        try {
            SQLExe.getDB().execSQL(sql, new Object[] { order.getId() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteAllPromotionData() {
        String sql = "delete from " + TableNames.OrderPromotion;
        try {
            SQLExe.getDB().execSQL(sql, new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
